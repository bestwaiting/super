package com.bestwaiting.utils.restful;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by bestwaiting on 17/5/25.
 */
public class RestfulUtils {

    /**
     * 执行请求
     *
     * @param url          请求地址
     * @param method       请求方式
     * @param responseType 返回的数据类型
     * @param uriVariables url自动匹配替换的参数，如url为api/{a}/{b},参数为["1","2"],则解析的url为api/1/2，使用Map参数时，遵循按key匹配
     * @return 结果对象
     * @throws RestClientException RestClient异常，包含状态码和非200的返回内容
     */
    public static <T> T exchange(String url, HttpMethod method, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return exchange(url, method, null, null, responseType, uriVariables);
    }

    /**
     * 执行请求
     *
     * @param url          请求地址
     * @param method       请求方式
     * @param headers      设置的头信息
     * @param responseType 返回的数据类型
     * @param uriVariables url自动匹配替换的参数，如url为api/{a}/{b},参数为["1","2"],则解析的url为api/1/2，使用Map参数时，遵循按key匹配
     * @return 结果对象
     * @throws RestClientException RestClient异常，包含状态码和非200的返回内容
     */
    public static <T> T exchange(String url, HttpMethod method, HttpHeaders headers, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return exchange(url, method, headers, null, responseType, uriVariables);
    }

    /**
     * 执行请求
     *
     * @param url          请求地址
     * @param method       请求方式
     * @param body         要提交的数据
     * @param responseType 返回数据类型
     *                     返回bean时指定Class
     * @param uriVariables url自动匹配替换的参数，如url为api/{a}/{b},参数为["1","2"],则解析的url为api/1/2，使用Map参数时，遵循按key匹配
     * @return 结果对象
     * @throws RestClientException RestClient异常，包含状态码和非200的返回内容
     */
    public static <T> T exchange(String url, HttpMethod method, Object body, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return exchange(url, method, null, body, responseType, uriVariables);
    }

    /**
     * 执行请求
     *
     * @param url          请求地址
     * @param method       请求方式
     * @param httpHeaders  请求头
     * @param body         要提交的数据
     * @param responseType 返回数据类型
     *                     返回bean时指定Class
     * @param uriVariables url自动匹配替换的参数，如url为api/{a}/{b},参数为["1","2"],则解析的url为api/1/2，使用Map参数时，遵循按key匹配
     * @return 结果对象
     * @throws RestClientException RestClient异常，包含状态码和非200的返回内容
     */
    public static <T> T exchange(String url, HttpMethod method, HttpHeaders httpHeaders, Object body, Class<T> responseType, Object... uriVariables) throws RestClientException {
        try {
            HttpEntity<?> requestEntity = new HttpEntity(body, httpHeaders);

            if (uriVariables.length == 1 && uriVariables[0] instanceof Map) {
                Map<String, ?> _uriVariables = (Map<String, ?>) uriVariables[0];
                return getClient().exchange(url, method, requestEntity, responseType, _uriVariables).getBody();
            }

            return getClient().exchange(url, method, requestEntity, responseType, uriVariables).getBody();
        } catch (Exception e) {
            throw new RestClientException(e);
        }
    }

    /**
     * 执行请求
     *
     * @param url          请求地址
     * @param method       请求方式
     * @param responseType 返回的数据类型，例：new ParameterizedTypeReference<List<Bean>>(){}
     * @param uriVariables url自动匹配替换的参数，如url为api/{a}/{b},参数为["1","2"],则解析的url为api/1/2，使用Map参数时，遵循按key匹配
     * @return 结果对象
     * @throws RestClientException RestClient异常，包含状态码和非200的返回内容
     */
    public static <T> T exchange(String url, HttpMethod method, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
        return exchange(url, method, null, null, responseType, uriVariables);
    }

    /**
     * 执行请求
     *
     * @param url          请求地址
     * @param method       请求方式
     * @param headers      设置的头信息
     * @param responseType 返回的数据类型，例：new ParameterizedTypeReference<List<Bean>>(){}
     * @param uriVariables url自动匹配替换的参数，如url为api/{a}/{b},参数为["1","2"],则解析的url为api/1/2，使用Map参数时，遵循按key匹配
     * @return 结果对象
     * @throws RestClientException RestClient异常，包含状态码和非200的返回内容
     */
    public static <T> T exchange(String url, HttpMethod method, HttpHeaders headers, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
        return exchange(url, method, headers, null, responseType, uriVariables);
    }

    /**
     * 执行请求
     *
     * @param url          请求地址
     * @param method       请求方式
     * @param body         要提交的数据
     * @param responseType 返回数据类型，例：new ParameterizedTypeReference<List<Bean>>(){}
     *                     返回bean时指定Class
     * @param uriVariables url自动匹配替换的参数，如url为api/{a}/{b},参数为["1","2"],则解析的url为api/1/2，使用Map参数时，遵循按key匹配
     * @return 结果对象
     * @throws RestClientException RestClient异常，包含状态码和非200的返回内容
     */
    public static <T> T exchange(String url, HttpMethod method, Object body, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
        return exchange(url, method, null, body, responseType, uriVariables);
    }

    /**
     * 执行请求
     *
     * @param url          请求地址
     * @param method       请求方式
     * @param httpHeaders  请求头
     * @param body         要提交的数据
     * @param responseType 返回数据类型，例：new ParameterizedTypeReference<List<Bean>>(){}
     *                     返回bean时指定Class
     * @param uriVariables url自动匹配替换的参数，如url为api/{a}/{b},参数为["1","2"],则解析的url为api/1/2，使用Map参数时，遵循按key匹配
     * @return 结果对象
     * @throws RestClientException RestClient异常，包含状态码和非200的返回内容
     */
    public static <T> T exchange(String url, HttpMethod method, HttpHeaders httpHeaders, Object body, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
        try {
            HttpEntity<?> requestEntity = new HttpEntity(body, httpHeaders);

            if (uriVariables.length == 1 && uriVariables[0] instanceof Map) {
                Map<String, ?> _uriVariables = (Map<String, ?>) uriVariables[0];
                return getClient().exchange(url, method, requestEntity, responseType, _uriVariables).getBody();
            }

            return getClient().exchange(url, method, requestEntity, responseType, uriVariables).getBody();
        } catch (Exception e) {
            throw new RestClientException(e);
        }
    }

    /**
     * 获得一个RestTemplate客户端
     *
     * @return
     */
    public static RestTemplate getClient() {
        return new RestTemplate();
    }

    /**
     * 获取一个application/x-www-form-urlencoded头
     *
     * @return
     */
    public static HttpHeaders buildBasicFORMHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    /**
     * 获取一个application/json头
     *
     * @return
     */
    public static HttpHeaders buildBasicJSONHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * 获取一个text/html头
     *
     * @return
     */
    public static HttpHeaders buildBasicHTMLHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return headers;
    }

    /**
     * 构建一个json头
     *
     * @param arrays
     * @return
     */
    public static HttpHeaders buildJSONHeaders(Object... arrays) {
        if (arrays.length % 2 != 0) {
            throw new RuntimeException("arrays 长度 必须为偶数");
        }

        HttpHeaders headers = buildBasicJSONHeaders();

        for (int i = 0; i < arrays.length; i++) {
            headers.add(arrays[i].toString(), arrays[++i].toString());
        }

        return headers;
    }

}

