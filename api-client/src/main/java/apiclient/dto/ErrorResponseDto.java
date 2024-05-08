package apiclient.dto;

public record ErrorResponseDto(Integer statusCode,
                               String errorCode,
                               String message) {
}
