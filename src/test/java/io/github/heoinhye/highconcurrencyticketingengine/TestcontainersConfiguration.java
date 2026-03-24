package io.github.heoinhye.highconcurrencyticketingengine;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * The core of this configuration is to provide
 * a fully isolated Redis environment for integration testing.
 * By using @ServiceConnection and dynamic port mapping,
 * I ensured that the tests are not dependent on the local host's infrastructure,
 * making the entire CI/CD pipeline more robust.
 * */

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection(name = "redis")
    GenericContainer<?> redisContainer() {
        /**
         * Use "redis:latest" image for testing.
         * ExposedPort 6379 will be mapped to a random ephemeral port on the host
         * to avoid conflicts with any local Redis instances.
         * */
        return new GenericContainer<>(DockerImageName.parse("redis:latest"))
                .withExposedPorts(6379);
    }

}
