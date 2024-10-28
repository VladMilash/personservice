package com.mvo.personservice.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@TestConfiguration(proxyBeanMethods = false)
public class PostgreTestcontainerConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
                .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("testcontainers")));
        container.start();
        return container;

    }

    @Bean
    public ConnectionFactory connectionFactory(PostgreSQLContainer<?> postgreSQLContainer) {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(postgreSQLContainer.getHost())
                        .port(postgreSQLContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT))
                        .database(postgreSQLContainer.getDatabaseName())
                        .username(postgreSQLContainer.getUsername())
                        .password(postgreSQLContainer.getPassword())
                        .build()
        );
    }
}
