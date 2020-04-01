package com.wjt;

import com.wjt.common.Constants;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppTest
        /*extends TestCase*/ {

    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    /*   *//**
     * Create the test case
     *
     * @param testName name of the test case
     *//*
    public AppTest(String testName) {
        super(testName);
    }

    *//**
     * @return the suite of tests being tested
     *//*
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    */

    /**
     * Rigourous Test :-)
     *//*
    public void testApp() {
        assertTrue(true);
    }

    @Test
    public void tank1() {
        TankClient TANK = new TankClient();
        TANK.lauchFrame();
        log.info("TANK finish!TANK={};", TANK);
    }
*/


    @Test
    public void random(){

        int m=31, n=1000,less=0,more=0;
        for (int i=0;i<n;i++){
            int t=Constants.RANDOM.nextInt(771);
            if(t<m){
                less++;
            }else {
                more++;
            }
        }

        log.info("less={};more={};",less,more);

    }


}
