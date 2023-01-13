package fi.develon.vsm;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fi.develon.vsm.adapter.out.db.repository.CompanyRepositoryJpaInterface;
import fi.develon.vsm.adapter.out.db.repository.StationRepositoryJpaInterface;
import fi.develon.vsm.domain.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = "classpath:00_initial_scripts.sql")
@ContextConfiguration(initializers = {TestApplication.Initializer.class})
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext
@Slf4j
public class TestApplication {

    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    private CompanyRepositoryJpaInterface repositoryJpaInterface;

    @Autowired
    private StationRepositoryJpaInterface stationRepositoryJpaInterface;

    @Autowired
    protected ObjectMapper json;

    @Autowired
    protected MockMvc mvc;

    private static Gson gson;

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.1")
        .withDatabaseName("integration-tests-db")
        .withUsername("sa")
        .withPassword("sa");

    static class Initializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeEach
    void beforeEach() {
        repositoryJpaInterface.deleteAll();
        stationRepositoryJpaInterface.deleteAll();
    }



}
