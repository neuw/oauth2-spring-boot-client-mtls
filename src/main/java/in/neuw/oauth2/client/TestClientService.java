package in.neuw.oauth2.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Karanbir Singh on 07/18/2020
 */
@Service
public class TestClientService {

    @Autowired
    private WebClient testWebClient;

    @Value("${test.client.test.path}")
    private String testPath;

    private String welcome = "Welcome";

    public Mono<Object> getTestMessage(String name) {
        if (StringUtils.isEmpty(name)) {
            name = "User";
        }
        String message = welcome + " " + name;

        return testWebClient
                .get()
                .uri(t -> t.queryParam("message", message).path(testPath).build())
                .exchange()
                .flatMap(r -> r.bodyToMono(Object.class));
    }


}
