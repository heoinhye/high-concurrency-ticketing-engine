package io.github.heoinhye.highconcurrencyticketingengine;

import org.springframework.boot.SpringApplication;

public class TestHighConcurrencyTicketingEngineApplication {

    public static void main(String[] args) {
        SpringApplication.from(HighConcurrencyTicketingEngineApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
