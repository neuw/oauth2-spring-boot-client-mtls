package in.neuw.oauth2.web.controller;

import in.neuw.oauth2.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Karanbir Singh on 07/18/2020
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("test")
    public Mono<Object> response(@RequestParam(required = false, defaultValue = "User") final String name) {
        return testService.getTestContent(name);
    }

}
