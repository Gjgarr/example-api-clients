package apiclient.httpexchange;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class EnhancedApiClientsConfig {
    @Bean
    public CoolApiInterface enhancedApiClientRestTemplate(@Qualifier("coolRestTemplate") RestTemplate restTemplate) {
        var adapter = RestTemplateAdapter.create(restTemplate);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CoolApiInterface.class);
    }

    @Bean
    public CoolApiInterface enhancedApiClientWebClient(@Qualifier("coolWebClient") WebClient webClient) {
        var adapter = WebClientAdapter.create(webClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CoolApiInterface.class);
    }

    @Bean
    public CoolApiInterface enhancedApiClientRestClient(@Qualifier("coolRestClient") RestClient restClient) {
        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CoolApiInterface.class);
    }
}
