package apiclient.resttemplate;

import apiclient.dto.ErrorResponseDto;
import apiclient.exceptions.ErrorResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class ErrorHandler extends DefaultResponseErrorHandler {
    private final ObjectMapper objectMapper;

    public ErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (HttpStatus.BAD_REQUEST.equals(response.getStatusCode())) {
            var body = this.getResponseBody(response);
            ErrorResponseDto errorResponseDto;
            try {
                errorResponseDto = objectMapper.readValue(body, ErrorResponseDto.class);
            } catch (Exception e) {
                super.handleError(response);
                return;
            }
            throw new ErrorResponseException(errorResponseDto);
        }
        super.handleError(response);
    }
}
