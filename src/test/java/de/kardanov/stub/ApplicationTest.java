package de.kardanov.stub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import de.kardanov.stub.Application;

/**
 * Test class for {@link Application}.
 * 
 * @author Ruslan Kardanov
 * @version 0.0.1
 */
@RunWith(SpringRunner.class)
public class ApplicationTest {
    
    /**
     * Test method checking basic behavior.
     * 
     * @throws Exception
     *             in case of exception during test run
     */
    @Test
    public void checkBasicBehavior() throws Exception {
        // Simply running main() method.
        Application.main(new String[0]);
    }
}