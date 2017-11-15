package com.bestwaiting.utils.restful;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 * Created by bestwaiting on 17/5/25.
 */
public class RestClientException extends NestedRuntimeException {
    /**
     * 状态码
     */
    private HttpStatus statusCode;
    /**
     * 状态码文本
     */
    private String statusText;
    /**
     * 异常时返回的内容
     */
    private String responseBody;
    /**
     * 返回的头
     */
    private HttpHeaders responseHeaders;

    public RestClientException(Exception exception) {

        super(exception.getMessage(), exception);

        if (exception instanceof HttpServerErrorException) {

            HttpServerErrorException e = (HttpServerErrorException) exception;
            this.statusCode = e.getStatusCode();
            this.statusText = e.getStatusText();
            this.responseBody = e.getResponseBodyAsString();
            this.responseHeaders = e.getResponseHeaders();
        } else if (exception instanceof HttpClientErrorException) {

            HttpClientErrorException e = (HttpClientErrorException) exception;
            this.statusCode = e.getStatusCode();
            this.statusText = e.getStatusText();
            this.responseBody = e.getResponseBodyAsString();
            this.responseHeaders = e.getResponseHeaders();
        } else {
            this.statusText = exception.getMessage();
        }
    }
}
