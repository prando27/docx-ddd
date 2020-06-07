package com.example.docxddd.application.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class DocumentDto {

    @JsonUnwrapped
    private final Object document;
}
