package apiclient.resttemplate;

import apiclient.ApiKeyService;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class AuthRequestInterceptor implements ClientHttpRequestInterceptor {
    private final ApiKeyService apiKeyService;

    public AuthRequestInterceptor(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        apiKeyService.setApiKeyHeader(request.getHeaders());

        return execution.execute(request, body);
    }
}
