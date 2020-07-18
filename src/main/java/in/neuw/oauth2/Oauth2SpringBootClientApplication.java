package in.neuw.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = ReactiveUserDetailsServiceAutoConfiguration.class)
public class Oauth2SpringBootClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2SpringBootClientApplication.class, args);
    }

}
