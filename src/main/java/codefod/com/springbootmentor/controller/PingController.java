package codefod.com.springbootmentor.controller;

import codefod.com.springbootmentor.aop.CodefodRateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @CodefodRateLimit(limit = 10, period = 120)
    @GetMapping("/ping")
    public String ping() {
        return "Pong";
    }
}
