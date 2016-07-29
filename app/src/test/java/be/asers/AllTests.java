package be.asers;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

/**
 * Test suite for all tests include and under this package.
 * 
 * @author chesteric31
 */
public class AllTests extends TestSuite {
    
    /**
     * @return the built suite
     */
    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
    }
}
