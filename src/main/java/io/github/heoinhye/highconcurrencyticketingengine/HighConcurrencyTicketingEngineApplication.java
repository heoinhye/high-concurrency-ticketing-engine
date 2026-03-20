package io.github.heoinhye.highconcurrencyticketingengine;

import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// We are temporarily excluding RedissonAutoConfiguration
// to start the server without a running Redis instance during the initial setup.
@SpringBootApplication(exclude = {RedissonAutoConfigurationV2.class})
public class HighConcurrencyTicketingEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighConcurrencyTicketingEngineApplication.class, args);
    }

}
