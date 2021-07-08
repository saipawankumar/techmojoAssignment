package com.techmojo.ratelimiting.contoller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
public class RateLimitingController {
	@PostMapping(value = "/rateLimitTest", produces = MediaType.APPLICATION_JSON_VALUE)
    public String rectangle() {

        return "Hey you are good to access the API!!!!";
    }
}
