package com.bnext.agenda.util;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
public abstract class BaseIntegrationTest {
	
	static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("agendatest")
            .withUsername("teste")
            .withPassword("123")
            .withReuse(true);

    static {
        mySQLContainer.start();
    }
    
	
	/*
	 static final MySQLContainer DATABASE = new MySQLContainer();

	    static {
	        DATABASE.start();
	    }

	    @DynamicPropertySource
	    static void databaseProperties(DynamicPropertyRegistry registry) {
	        registry.add("spring.datasource.url", DATABASE::getJdbcUrl);
	        registry.add("spring.datasource.username", DATABASE::getUsername);
	        registry.add("spring.datasource.password", DATABASE::getPassword);
	    }
	  */  

}
