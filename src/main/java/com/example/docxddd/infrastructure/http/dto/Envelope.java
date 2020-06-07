package com.example.docxddd.infrastructure.http.dto;

import lombok.Getter;

// TODO - Criar um texto com o motivo de uso da classe Envelope
// TODO - Ver no curso Anemic towards Rich domain model
@Getter
public class Envelope<T> {

    private final T result;

    private final String errorMessage;

    private Envelope(T result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public static <T> Envelope<T> ok(T result) {
        return new Envelope<>(result, null);
    }

    public static <T> Envelope<T> error(String errorMessage) {
        return new Envelope<>(null, errorMessage);
    }
}
