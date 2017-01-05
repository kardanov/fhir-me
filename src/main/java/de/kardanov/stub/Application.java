package de.kardanov.stub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application initialization.
 * 
 * @author Ruslan Kardanov
 * @version 0.0.1
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    
    /** Logger. */
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    
    /**
     * Spring application initialization.
     * 
     * @param args
     *            arguments
     * @throws Exception
     *             in case of exception
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        logger.info("spring-boot-stub :: app started.");
    }
}