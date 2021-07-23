package com.bnext.agenda.exception;

public class ResourceAlreadyExist extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExist(String mensagem) {
        super(mensagem);
    }
}