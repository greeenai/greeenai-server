package com.greeenai.greeenai.global.error;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.global.error.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ActiveProfiles({"test"})
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleMethodArgumentNotValid() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("objectName", "field", "defaultMessage"));
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(METHOD_ARGUMENT_INVALID.getMessage() + " {field=defaultMessage}", errorResponse.message());
    }

    @Test
    void handleHttpRequestMethodNotSupported() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleHttpRequestMethodNotSupported(
                ex, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, webRequest);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(METHOD_NOT_SUPPORTED.getMessage(), errorResponse.message());
    }

    @Test
    void handleHandlerMethodValidationException() {
        HandlerMethodValidationException ex = mock(HandlerMethodValidationException.class);

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleHandlerMethodValidationException(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(QUERY_PARAM_INVALID.getMessage(), errorResponse.message());
    }

    @Test
    void handleMissingServletRequestParameter() {
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("param", "String");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMissingServletRequestParameter(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(QUERY_PARAM_NOT_FOUND.getMessage(), errorResponse.message());
    }

    @Test
    void handleExceptionInternal() {
        Exception ex = new Exception("Internal error");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleExceptionInternal(
                ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(INTERNAL_SERVER_ERROR.getMessage(), errorResponse.message());
    }

    @Test
    void handleHttpMessageNotReadable() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Message not readable");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleHttpMessageNotReadable(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(HTTP_MESSAGE_NOT_READABLE.getMessage(), errorResponse.message());
    }

    @Test
    void handleCustomException() {
        ErrorCode errorCode = INTERNAL_SERVER_ERROR;
        CustomException ex = new CustomException(errorCode);

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleCustomException(ex);

        assertEquals(errorCode.getHttpStatus(), responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(errorCode.getMessage(), errorResponse.message());
    }

    @Test
    void handleMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentTypeMismatchException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(QUERY_TYPE_MISMATCH.getMessage(), errorResponse.message());
    }

    @Test
    void handleHttpClientErrorException() {
        HttpClientErrorException ex =
                HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Bad Request", new HttpHeaders(), null, null);

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleClientErrorException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(ex.getResponseBodyAsString(), errorResponse.message());
    }

    @Test
    void handleException() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(ex.getClass().getSimpleName(), errorResponse.className());
        assertEquals(INTERNAL_SERVER_ERROR.getMessage(), errorResponse.message());
    }
}
