package apiclient.httpexchange;

import apiclient.ApiClient;
import apiclient.dto.RequestDto;
import apiclient.dto.ResponseDto;
import apiclient.exceptions.ErrorResponseException;
import apiclient.exceptions.ResourceConflictException;
import apiclient.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static apiclient.enums.ErrorCode.*;

@Component
public class EnhancedCoolWebClientApiClient implements ApiClient {
    private final CoolApiInterface coolApiInterface;

    public EnhancedCoolWebClientApiClient(@Qualifier("enhancedApiClientWebClient") CoolApiInterface coolApiInterface) {
        this.coolApiInterface = coolApiInterface;
    }

    public ResponseDto fetch(Integer resourceId) {
        try {
            return coolApiInterface.fetch(resourceId);
        } catch (ErrorResponseException e) {
            if (e.hasError(NOT_FOUND)) {
                throw new ResourceNotFoundException();
            }
            throw e;
        }
    }

    public ResponseDto create(RequestDto requestDto) {
        try {
            return coolApiInterface.create(requestDto);
        } catch (ErrorResponseException e) {
            if (e.hasError(CONFLICTING)) {
                throw new ResourceConflictException();
            }
            throw e;
        }
    }
}
