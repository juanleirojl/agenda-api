package com.bnext.agenda.exception.handler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bnext.agenda.exception.BusinessException;
import com.bnext.agenda.exception.ResourceAlreadyExist;
import com.bnext.agenda.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MSG_ERROR_GENERIC_USER_FINAL = "An unexpected internal system error has occurred. "
			+ "Please try again and if the error persists, contact your system administrator.";
	
	public static final String ERROR_MESSAGE_TEMPLATE = "message: %s %n method: %s %n requested uri: %s %n status: %s";

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(status).headers(headers).build();
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
	}

	private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers, HttpStatus status,
			WebRequest request, BindingResult bindingResult) {
		ErrorType errorType = ErrorType.INVALID_DATA;
		String detail = "One or more fields are invalid. Fix it and try again.";

		List<Error.Detail> erroObjects = bindingResult.getAllErrors().stream().map(objectError -> {
			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

			String name = objectError.getObjectName();

			if (objectError instanceof FieldError) {
				name = ((FieldError) objectError).getField();
			}

			return Error.Detail.builder().name(name).userMessage(message).build();
		}).collect(Collectors.toList());

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		
		Error erro = createErroBuilder(ex,status, errorType, detail,webRequest).userMessage(detail).errors(erroObjects).build();
		return handleExceptionInternal(ex, erro, headers, status, request);
	}

	@ExceptionHandler(ResourceAlreadyExist.class)
	public ResponseEntity<Object> handleUncaught(ResourceAlreadyExist ex, WebRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		ErrorType errorType = ErrorType.ENTITY_ALREADY_EXIST;
		String detailUser = "Resource already exist";
		String detail = ex.getMessage();

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		Error erro = createErroBuilder(ex, status, errorType,detail,webRequest).userMessage(detailUser).build();
		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorType errorType = ErrorType.SYSTEM_ERROR;
		String detailUser = MSG_ERROR_GENERIC_USER_FINAL;
		String detail = ex.getCause()!=null && ex.getCause().getCause()!=null ? ex.getCause().getCause().getMessage() : ex.getStackTrace()[0]!=null ? ex.getStackTrace()[0].toString() : detailUser;

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		Error erro = createErroBuilder(ex, status, errorType,detail,webRequest).userMessage(detailUser).build();
		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ErrorType errorType = ErrorType.RESOURCE_NOT_FOUND;
		String detail = String.format("Resource  %s doesn't exist", ex.getRequestURL());

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		Error erro = createErroBuilder(ex, status, errorType, detail,webRequest).userMessage(MSG_ERROR_GENERIC_USER_FINAL).build();
		return handleExceptionInternal(ex, erro, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}
		return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorType errorType = ErrorType.INVALID_PARAMETER;

		String detail = String.format(
				"URL '%s' received the value '%s', "
						+ "that is a invalid type. Fix and enter a compatible with type %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		Error erro = createErroBuilder(ex, status, errorType, detail,webRequest).userMessage(MSG_ERROR_GENERIC_USER_FINAL).build();
		return handleExceptionInternal(ex, erro, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		}

		ErrorType errorType = ErrorType.INCOMPREHENSIBLE_MESSAGE;
		String detail = "Request body is invalid. Check syntax error.";

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		Error erro = createErroBuilder(ex, status, errorType, detail,webRequest).userMessage(MSG_ERROR_GENERIC_USER_FINAL).build();
		return handleExceptionInternal(ex, erro, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		ErrorType errorType = ErrorType.INCOMPREHENSIBLE_MESSAGE;
		String detail = String.format(
				"Field '%s' doesn't exist. Fix or remove the field and try again.", path);

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		Error erro = createErroBuilder(ex, status, errorType, detail,webRequest).userMessage(MSG_ERROR_GENERIC_USER_FINAL).build();
		return handleExceptionInternal(ex, erro, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());

		ErrorType errorType = ErrorType.INCOMPREHENSIBLE_MESSAGE;
		String detail = String.format(
				"Field '%s' received the value '%s', "
						+ "is a invalid type. Fix with a compatible value %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		Error erro = createErroBuilder(ex, status, errorType, detail,webRequest).userMessage(MSG_ERROR_GENERIC_USER_FINAL).build();
		return handleExceptionInternal(ex, erro, headers, status, request);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		ErrorType errorType = ErrorType.RESOURCE_NOT_FOUND;
		String detail = ex.getMessage();

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		Error erro = createErroBuilder(ex, status, errorType, detail,webRequest).userMessage(detail).build();
		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}


	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBusinessException(BusinessException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorType errorType = ErrorType.BUSINESS_EXCEPTION_VIOLATION;
		String detail = ex.getMessage();

		ServletWebRequest webRequest = ((ServletWebRequest)request);
		Error erro = createErroBuilder(ex, status, errorType, detail,webRequest).userMessage(detail).build();
		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = Error.builder().timestamp(Instant.now()).title(status.getReasonPhrase()).status(status.value())
					.userMessage(MSG_ERROR_GENERIC_USER_FINAL).build();
		} else if (body instanceof String) {
			body = Error.builder().timestamp(Instant.now()).title((String) body).status(status.value())
					.userMessage(MSG_ERROR_GENERIC_USER_FINAL).build();
		}
		
		log.error(String.format(ERROR_MESSAGE_TEMPLATE, ex.getClass().getSimpleName(),
				((ServletWebRequest)request).getRequest().getMethod(),
				((ServletWebRequest)request).getRequest().getRequestURI(),
				status.value()), 
				ex);

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Error.ErrorBuilder createErroBuilder(Exception ex, HttpStatus status, ErrorType errorType, String detail, ServletWebRequest webRequest) {

		String path = webRequest.getRequest().getRequestURI()!=null ? webRequest.getRequest().getRequestURI().toString() : "";
		return Error.builder().timestamp(Instant.now()).status(status.value()).type(ex.getClass().getSimpleName())
				.path(path)
				.title(errorType.getTitle()).detail(detail);
	}

	private String joinPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}
}