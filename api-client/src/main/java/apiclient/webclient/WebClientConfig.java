package apiclient.webclient;

import apiclient.ApiKeyService;
import apiclient.dto.ErrorResponseDto;
import apiclient.exceptions.ErrorResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient coolWebClient(@Value("${externalApi.url}" + "${externalApi.path}") String baseUrl,
                                   ApiKeyService apiKeyService) {
        var httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(30L));

        var objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

        var strategies = ExchangeStrategies.builder()
            .codecs(configurer -> {
                configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
                configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
            })
            .build();

        return WebClient.builder()
            // Timeout
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            // Base URL
            .baseUrl(baseUrl)
            // casing
            .exchangeStrategies(strategies)
            // headers
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            // Error handler
            .filter(errorHandler())
            // Auth
            .filter((request, next) -> {
                var newRequest = ClientRequest.from(request)
                    .headers(apiKeyService::setApiKeyHeader)
                    .build();
                return next.exchange(newRequest);
            })
            .build();
    }

    private ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (HttpStatus.BAD_REQUEST.equals(clientResponse.statusCode())) {
                return clientResponse
                    .bodyToMono(ErrorResponseDto.class)
                    .flatMap(responseDto -> Mono.error(new ErrorResponseException(responseDto)));
            }
            return Mono.just(clientResponse);
        });
    }
}
