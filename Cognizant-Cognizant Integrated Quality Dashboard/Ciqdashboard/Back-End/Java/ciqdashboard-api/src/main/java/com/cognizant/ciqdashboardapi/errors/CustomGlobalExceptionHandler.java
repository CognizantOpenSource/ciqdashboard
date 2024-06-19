/*
 *   © [2021] Cognizant. All rights reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.cognizant.ciqdashboardapi.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

/**
 * CustomGlobalExceptionHandler
 * @author Cognizant
 */

@ControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String RESOURCE_NOT_FOUND = "resource-not-found";
    private static final String INVALID_DETAILS = "invalid-details";
    private static final String INVALID_AUTHENTICATION_DETAILS = "invalid-authentication-details";
    private static final String REQUEST_LOG = "Request: ";
    private static final String INVALID_REQUEST = "invalid-request";
    private static final String REQUEST_CUSTOM_LOG = "Request:: Custom >> ";
    private static final String RESOURCE_CONFLICT = "resource-conflict";

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse> resourceNotFound(Exception exception, HttpServletRequest request) {
        logger.error(REQUEST_LOG + request.getRequestURL(), exception);
        ApiResponse response = new ApiResponse(LocalDateTime.now(),
                NOT_FOUND.value(),
                RESOURCE_NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<ApiResponse> resourceExists(Exception exception, HttpServletRequest request) {
        log.error(REQUEST_LOG + request.getRequestURL(), exception);
        ApiResponse response = new ApiResponse(LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                RESOURCE_CONFLICT,
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InvalidDetailsException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse> invalidDetails(Exception exception, HttpServletRequest request) {
        logger.error(REQUEST_LOG + request.getRequestURL(), exception);
        ApiResponse response = new ApiResponse(LocalDateTime.now(),
                INTERNAL_SERVER_ERROR.value(),
                INVALID_DETAILS,
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidAuthenticationException.class})
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<ApiResponse> invalidAuthenticationDetails(Exception exception, HttpServletRequest request) {
        logger.error(REQUEST_LOG + request.getRequestURL(), exception);
        ApiResponse response = new ApiResponse(LocalDateTime.now(),
                UNAUTHORIZED.value(),
                INVALID_AUTHENTICATION_DETAILS,
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(REQUEST_CUSTOM_LOG + request.getDescription(false), exception);
        List<String> errors = new ArrayList<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }
        log.error(REQUEST_LOG + request.getDescription(false), exception);

        ApiResponse response = new ApiResponse(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                INVALID_REQUEST,
                errors.stream().map(error -> error).collect(Collectors.joining(",")),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}

