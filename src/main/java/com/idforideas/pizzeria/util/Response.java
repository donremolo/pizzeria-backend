package com.idforideas.pizzeria.util;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.http.HttpStatus;

import lombok.Builder;


@JsonInclude(NON_NULL)
@Builder
public record Response(LocalDateTime timeStamp,
    int statusCode,
    HttpStatus status,
    String reason,
    String message,
    String exception,
    String developerMessage,
    String path,
    Map<?, ?> errors,
    Map<?, ?> data) {    
}
