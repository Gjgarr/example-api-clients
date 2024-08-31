package apiclient.resttemplate;

import apiclient.ApiClient;
import apiclient.dto.RequestDto;
import apiclient.dto.ResponseDto;
import apiclient.exceptions.ErrorResponseException;
import apiclient.exceptions.ResourceConflictException;
import apiclient.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static apiclient.enums.ErrorCode.CONFLICTING;
import static apiclient.enums.ErrorCode.NOT_FOUND;

@Component
public class CoolRestTemplateApiClient implements ApiClient {
    private final RestTemplate restTemplate;

    public CoolRestTemplateApiClient(@Qualifier("coolRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseDto fetch(Integer resourceId) {
        try {
            return restTemplate.getForObject("/{resourceId}", ResponseDto.class, resourceId);
        } catch (ErrorResponseException e) {
            if (e.hasError(NOT_FOUND)) {
                throw new ResourceNotFoundException();
            }
            throw e;
        }
    }

    public ResponseDto create(RequestDto requestDto) {
        try {
            return restTemplate.postForObject("/", requestDto, ResponseDto.class);
        } catch (ErrorResponseException e) {
            if (e.hasError(CONFLICTING)) {
                throw new ResourceConflictException();
            }
            throw e;
        }
    }
}
