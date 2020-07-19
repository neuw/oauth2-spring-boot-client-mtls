package in.neuw.oauth2;

import in.neuw.oauth2.config.OAuth2ClientSSLPropertiesConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = ReactiveUserDetailsServiceAutoConfiguration.class)
@EnableConfigurationProperties(OAuth2ClientSSLPropertiesConfigurer.class)
public class Oauth2SpringBootClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2SpringBootClientApplication.class, args);
    }

}
