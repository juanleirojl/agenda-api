package com.bnext.agenda.data.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class BaseDTO {
	@Schema(description = "Id.", example = "1", required = false)
	private Long id;

	@Schema(description = "Created Date.", example = "", required = false)
	private LocalDateTime createdDate;
	
	@Schema(description = "Modified Date.", example = "null", required = false)
	private LocalDateTime modifiedDate;
	
	@Schema(description = "null", example = "null", required = false)
	private Integer version;
}
