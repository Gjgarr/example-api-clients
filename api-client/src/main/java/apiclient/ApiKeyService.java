package apiclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyService {
    private final String apiKeyHeaderName;
    private final String apiKey;

    public ApiKeyService(@Value("${externalApi.apiKeyHeaderName}") String apiKeyHeaderName,
                         @Value("${externalApi.apiKey}") String apiKey) {
        this.apiKeyHeaderName = apiKeyHeaderName;
        this.apiKey = apiKey;
    }

    public void setApiKeyHeader(HttpHeaders headers) {
        headers.set(apiKeyHeaderName, apiKey);
    }
}
