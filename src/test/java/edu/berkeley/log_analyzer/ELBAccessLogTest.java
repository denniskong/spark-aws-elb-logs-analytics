package edu.berkeley.log_analyzer;

import com.databricks.apps.logs.ELBAccessLog;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ELBAccessLogTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ELBAccessLogTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ELBAccessLogTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testParseNormalLine()
    {
    	String logline = "2016-02-16T01:00:50.309216Z ArabGT-Load-Balancer 105.198.242.106:15610 172.31.14.182:80 0.000042 0.000882 0.000023 304 304 0 0 \"GET http://www.arabgt.com:80/sites/default/files/js/js_pcG4nozC_WcLiZGYbq1zfRAv88ony8ZmiwgBXaRG1ik.js HTTP/1.1\" \"Mozilla/5.0 (iPhone; CPU iPhone OS 9_2_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13D15 [FBAN/FBIOS;FBAV/48.0.0.64.162;FBBV/21316239;FBDV/iPhone8,1;FBMD/iPhone;FBSN/iPhone OS;FBSV/9.2.1;FBSS/2; FBCR/Vodafone;FBID/phone;FBLC/en_US;FBOP/5]\" - -";
    	ELBAccessLog log = ELBAccessLog.parseFromLogLine(logline);
    	assertEquals(log.getSslProtocol(), "-");	
    	assertEquals(log.getUserAgent(), "Mozilla/5.0 (iPhone; CPU iPhone OS 9_2_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13D15 [FBAN/FBIOS;FBAV/48.0.0.64.162;FBBV/21316239;FBDV/iPhone8,1;FBMD/iPhone;FBSN/iPhone OS;FBSV/9.2.1;FBSS/2; FBCR/Vodafone;FBID/phone;FBLC/en_US;FBOP/5]");
        
    }
}
