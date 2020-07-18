package in.neuw.oauth2.service;

import in.neuw.oauth2.client.TestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Karanbir Singh on 07/18/2020
 */
@Service
public class TestService {

    @Autowired
    private TestClientService testClientService;

    public Mono<Object> getTestContent(final String name) {
        return testClientService.getTestMessage(name);
    }

}
