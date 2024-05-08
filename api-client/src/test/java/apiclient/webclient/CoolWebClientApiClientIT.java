package apiclient.webclient;

import apiclient.dto.RequestDto;
import apiclient.dto.ResponseDto;
import apiclient.exceptions.ErrorResponseException;
import apiclient.exceptions.ResourceConflictException;
import apiclient.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CoolWebClientApiClientIT {
    @Autowired
    private CoolWebClientApiClient apiClient;

    @Nested
    class FetchResource {
        @Test
        void success() {
            // Given
            Integer resourceId = 1;

            // When
            ResponseDto response = apiClient.fetch(resourceId);

            // Then
            assertNotNull(resourceId);
            assertEquals("a", response.firstName());
            assertEquals(1, response.userId());
        }

        @Test
        void notFound() {
            // Given
            Integer resourceId = 2;

            // When
            assertThrows(ResourceNotFoundException.class, () -> apiClient.fetch(resourceId));

            // Then
        }

        @Test
        void error() {
            // Given
            Integer resourceId = 3;

            // When
            var exception = assertThrows(ErrorResponseException.class, () -> apiClient.fetch(resourceId));

            // Then
            var errorResponse = exception.getErrorResponseDto();
            assertNotNull(errorResponse);
            assertEquals("BAD_REQUEST", errorResponse.errorCode());
            assertEquals(400, errorResponse.statusCode());
            assertEquals("Generic bad request for resource '3'", errorResponse.message());
        }
    }

    @Nested
    class CreateResource {
        @Test
        void success() {
            // Given
            RequestDto request = new RequestDto("a");

            // When
            ResponseDto response = apiClient.create(request);

            // Then
            assertNotNull(response);
            assertEquals(1, response.userId());
            assertEquals("a", response.firstName());
        }

        @Test
        void conflict() {
            // Given
            RequestDto request = new RequestDto("b");

            // When
            assertThrows(ResourceConflictException.class, () -> apiClient.create(request));

            // Then
        }

        @Test
        void error() {
            // Given
            RequestDto request = new RequestDto("c");

            // When
            var exception = assertThrows(ErrorResponseException.class, () -> apiClient.create(request));

            // Then
            var errorResponse = exception.getErrorResponseDto();
            assertNotNull(errorResponse);
            assertEquals("BAD_REQUEST", errorResponse.errorCode());
            assertEquals(400, errorResponse.statusCode());
            assertEquals("Generic bad request for resource with first_name 'c'", errorResponse.message());
        }
    }
}
