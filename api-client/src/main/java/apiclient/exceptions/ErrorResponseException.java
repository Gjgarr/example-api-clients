package apiclient.exceptions;

import apiclient.dto.ErrorResponseDto;
import apiclient.enums.ErrorCode;

public class ErrorResponseException extends RuntimeException {
    private final ErrorResponseDto errorResponseDto;

    public ErrorResponseException(ErrorResponseDto errorResponseDto) {
        this.errorResponseDto = errorResponseDto;
    }

    public ErrorResponseDto getErrorResponseDto() {
        return errorResponseDto;
    }

    public boolean hasError(ErrorCode errorCode) {
        return errorCode.name().equals(errorResponseDto.errorCode());
    }
}
