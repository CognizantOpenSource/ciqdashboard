/*
 *  © [2021] Cognizant. All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.cognizant.authapi.base.error;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * CustomGlobalExceptionHandler - constants added
 *
 * @author Cognizant
 */

@ControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_ERROR = "unexpected-error";
    private static final String RESOURCE_NOT_FOUND = "resource-not-found";
    private static final String RESOURCE_CONFLICT = "resource-conflict";
    private static final String INVALID_CREDENTIAL_ERROR = "invalid-cred";
    private static final String INVALID_INPUT = "invalid-input";
    private static final String INVALID_DATA = "invalid-data";
    private static final String INVALID_REQUEST = "invalid-request";
    private static final String JWT_TOKEN_EXPIRED = "token-expired";
    private static final String INVALID_DETAILS = "invalid-details";

    private static final String REQUEST_LOG = "Request: ";
    private static final String REQUEST_CUSTOM_LOG = "Request:: Custom >> ";

    @ExceptionHandler({ResourceNotFoundException.class, FieldValueNullException.class})
    public ResponseEntity<ErrorResponse> resourceNotFound(Exception exception, HttpServletRequest request) {
        log.error(REQUEST_LOG + request.getRequestURL(), exception);
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                RESOURCE_NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<ErrorResponse> resourceExists(Exception exception, HttpServletRequest request) {
        log.error(REQUEST_LOG + request.getRequestURL(), exception);
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                RESOURCE_CONFLICT,
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidResourceException.class)
    public ResponseEntity<ErrorResponse> invalidValue(Exception exception, HttpServletRequest request) {
        log.error(REQUEST_LOG + request.getRequestURL(), exception);
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                String.join("/", RESOURCE_CONFLICT, INVALID_INPUT),
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomInvalidCredentialException.class)
    public ResponseEntity<ErrorResponse> badCredentials(Exception exception, HttpServletRequest request) {
        log.error(REQUEST_LOG + request.getRequestURL(), exception);
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                INVALID_CREDENTIAL_ERROR,
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> badInput(ConstraintViolationException exception, HttpServletRequest request) {
        log.debug(REQUEST_LOG + request.getRequestURL(), exception);
        Optional<ConstraintViolation<?>> violation = exception.getConstraintViolations().stream().findFirst();
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                INVALID_INPUT,
                violation.isPresent() ? violation.get().getMessage() : INVALID_INPUT,
                request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidDetailsException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponse> invalidDetails(Exception exception, HttpServletRequest request) {
        logger.error(REQUEST_LOG + request.getRequestURL(), exception);
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                NOT_FOUND.value(),
                INVALID_DETAILS,
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(response, NOT_FOUND);
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

        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                INVALID_REQUEST,
                errors.stream().map(error -> error).collect(Collectors.joining(",")),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(REQUEST_CUSTOM_LOG + request.getDescription(false), exception);
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                status.value(),
                INVALID_REQUEST,
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, status);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(REQUEST_CUSTOM_LOG + request.getDescription(false), exception);
        log.info("\n " + exception.getMessage());

        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                status.value(),
                INTERNAL_ERROR,
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(REQUEST_CUSTOM_LOG + request.getDescription(false), exception);
        log.info("\n " + exception.getMessage());

        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                status.value(),
                INVALID_DATA,
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*Default Exception Handler*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(HttpServletRequest request, Exception exception) {
        log.error(REQUEST_CUSTOM_LOG + request.getRequestURL().toString(), exception);
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_ERROR,
                exception.getMessage(),
                request.getRequestURL().toString()
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        if (ex.getMessage().toLowerCase().contains("access is denied")) {
            ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                    HttpStatus.FORBIDDEN.value(),
                    HttpStatus.FORBIDDEN.toString(),
                    ex.getMessage(),
                    request.getDescription(false)
            );
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<Object> handleExpiredJwtException(Exception exception, WebRequest request) {
        log.error(REQUEST_CUSTOM_LOG + request.getDescription(false), exception);
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                JWT_TOKEN_EXPIRED,
                exception.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({/*HttpException.class, GHFileNotFoundException.class,*/ ThrowException.class})
    public ResponseEntity<Object> handleThrowException(Exception genException, WebRequest request) {
        log.error("ThrowException :: Request:: Custom >> " + request.getDescription(false), genException);
        ErrorResponse response;
        HttpStatus status;
        status = HttpStatus.INTERNAL_SERVER_ERROR;
        response = new ErrorResponse(LocalDateTime.now(),
                status.value(),
                INTERNAL_ERROR,
                genException.getCause().getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

}
