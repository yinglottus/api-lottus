package api.loja.lotus.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class TestContainers {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQL =
            new PostgreSQLContainer<>("postgres:16.8")
                    .withDatabaseName("lottusdb")
                    .withUsername("lottus")
                    .withPassword("09181212");

}
