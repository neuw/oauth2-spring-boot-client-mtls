package in.neuw.oauth2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author Karanbir Singh on 07/18/2020
 */
@Configuration
public class TestClientConfig {

    @Value("${test.client.base.url}")
    private String testClientBaseUrl;

    private Logger testWebClientLogger = LoggerFactory.getLogger("TEST_WEB_CLIENT");

    /**
     * The authorizedClientManager for required by the webClient
     */
    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(final ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                                         final ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager = new DefaultReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    /**
     * The Oauth2 based WebClient bean for the web service
     */
    @Bean("testWebClient")
    public WebClient webClient(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {

        String registrationId = "local";

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        // for telling which registration to use for the webclient
        oauth.setDefaultClientRegistrationId(registrationId);
        return WebClient.builder()
                // base path of the client, this way we need to set the complete url again
                .baseUrl(testClientBaseUrl)
                .filter(oauth)
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    /*
     * Log request details for the downstream web service calls
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(c -> {
            testWebClientLogger.info("Request: {} {}", c.method(), c.url());
            c.headers().forEach((n, v) -> {
                if (!n.equalsIgnoreCase(AUTHORIZATION)) {
                    testWebClientLogger.info("request header {}={}", n, v);
                } else {
                    // as the AUTHORIZATION header is something security bounded
                    // will show up when the debug level logging is enabled
                    // for example using property - logging.level.root=DEBUG
                    testWebClientLogger.debug("request header {}={}", n, v);
                }
            });
            return Mono.just(c);
        });
    }

    /*
     * Log response details for the downstream web service calls
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(c -> {
            testWebClientLogger.info("Response: {}", c.statusCode());
            // if want to show the response headers in the log by any chance?
            /*c.headers().asHttpHeaders().forEach((n, v) -> {
                testWebClientLogger.info("response header {}={}", n, v);
            });*/
            return Mono.just(c);
        });
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.oauth2Client().and().build();
    }

}
