package de.kardanov.stub;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service REST controller.
 * 
 * @author Ruslan Kardanov
 * @version 0.0.1
 */
@RestController
public class Service {
    
    /**
     * Test method to say "hello!".
     * 
     * @return "hello!"
     */
    @RequestMapping(value = "/api/hello", method = RequestMethod.GET)
    public String sayHello() {
        return "hello!";
    }
}