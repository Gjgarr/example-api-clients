package apiclient.resttemplate;

import apiclient.ApiKeyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate coolRestTemplate(@Value("${externalApi.url}" + "${externalApi.path}") String baseUrl,
                                         ApiKeyService apiKeyService,
                                         ObjectMapper objectMapper) {
        var snakeMapper = objectMapper.copy()
            .setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

        return new RestTemplateBuilder()
            // Timeout
            .setConnectTimeout(Duration.ofSeconds(30L))
            .setReadTimeout(Duration.ofSeconds(30L))
            // Base URL
            .rootUri(baseUrl)
            // casing
            .messageConverters(new MappingJackson2HttpMessageConverter(snakeMapper))
            // headers
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            // Error handler
            .errorHandler(new ErrorHandler(snakeMapper))
            // Auth
            .interceptors(new AuthRequestInterceptor(apiKeyService))
            .build();
    }
}
