package apiclient.restclient;

import apiclient.ApiClient;
import apiclient.dto.RequestDto;
import apiclient.dto.ResponseDto;
import apiclient.exceptions.ErrorResponseException;
import apiclient.exceptions.ResourceConflictException;
import apiclient.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static apiclient.enums.ErrorCode.*;

@Component
public class CoolRestClientApiClient implements ApiClient {
    private final RestClient restClient;

    public CoolRestClientApiClient(@Qualifier("coolRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseDto fetch(Integer resourceId) {
        try {
            return restClient
                .get()
                .uri("/{resourceId}", resourceId)
                .retrieve()
                .body(ResponseDto.class);
        } catch (ErrorResponseException e) {
            if (e.hasError(NOT_FOUND)) {
                throw new ResourceNotFoundException();
            }
            throw e;
        }
    }

    public ResponseDto create(RequestDto requestDto) {
        try {
            return restClient
                .post()
                .uri("/")
                .body(requestDto)
                .retrieve()
                .body(ResponseDto.class);
        } catch (ErrorResponseException e) {
            if (e.hasError(CONFLICTING)) {
                throw new ResourceConflictException();
            }
            throw e;
        }
    }
}
