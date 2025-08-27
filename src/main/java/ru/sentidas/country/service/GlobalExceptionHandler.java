package ru.sentidas.country.service;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sentidas.country.controller.error.ApiError;
import ru.sentidas.country.ex.CountryNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${api.version}")
    private String apiVersion;

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CountryNotFoundException.class)
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleCountryNotFoundException(CountryNotFoundException ex, HttpServletRequest request) {
        //    LOG.error("Error while founding country", ex);
        LOG.error(request.getRequestURI(), ex);
        return new ResponseEntity<>(
                new ApiError(
                        apiVersion,
                        HttpStatus.NOT_FOUND.toString(),
                        "Country not found",
                        request.getRequestURI(),
                        ex.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );
    }
}
