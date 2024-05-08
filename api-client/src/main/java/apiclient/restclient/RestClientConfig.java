package apiclient.restclient;

import apiclient.ApiKeyService;
import apiclient.resttemplate.ErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient coolRestClient(@Value("${externalApi.url}" + "${externalApi.path}") String baseUrl,
                                     ApiKeyService apiKeyService) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(30L));
        factory.setReadTimeout(Duration.ofSeconds(30L));

        var objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

        return RestClient.builder()
            // Timeout
            .requestFactory(factory)
            // Base URL
            .baseUrl(baseUrl)
            // casing
            .messageConverters(converters -> {
                converters.clear();
                converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
            })
            // headers
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            // Error handler
            .defaultStatusHandler(new ErrorHandler(objectMapper))
            // Auth
            .requestInitializer(request -> apiKeyService.setApiKeyHeader(request.getHeaders()))
            .build();
    }

    @Bean
    public RestClient coolRestClientFromExisting(@Qualifier("coolRestTemplate") RestTemplate restTemplate) {
        return RestClient.create(restTemplate);
    }
}
