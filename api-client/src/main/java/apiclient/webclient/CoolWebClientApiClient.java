package apiclient.webclient;

import apiclient.ApiClient;
import apiclient.dto.RequestDto;
import apiclient.dto.ResponseDto;
import apiclient.exceptions.ErrorResponseException;
import apiclient.exceptions.ResourceConflictException;
import apiclient.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static apiclient.enums.ErrorCode.CONFLICTING;
import static apiclient.enums.ErrorCode.NOT_FOUND;

@Component
public class CoolWebClientApiClient implements ApiClient {
    private final WebClient webClient;

    public CoolWebClientApiClient(@Qualifier("coolWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseDto fetch(Integer resourceId) {
        return webClient
            .get()
            .uri("/{resourceId}", resourceId)
            .retrieve()
            .bodyToMono(ResponseDto.class)
            .onErrorMap(ErrorResponseException.class, e -> e.hasError(NOT_FOUND) ? new ResourceNotFoundException() : e)
            .block();
    }

    public ResponseDto create(RequestDto requestDto) {
        return webClient
            .post()
            .uri("/")
            .bodyValue(requestDto)
            .retrieve()
            .bodyToMono(ResponseDto.class)
            .onErrorMap(ErrorResponseException.class, e -> e.hasError(CONFLICTING) ? new ResourceConflictException() : e)
            .block();
    }
}
