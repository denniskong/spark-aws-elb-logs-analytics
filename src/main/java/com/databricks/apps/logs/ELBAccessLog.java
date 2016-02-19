package com.databricks.apps.logs;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * timestamp 
 * elb 
 * client:port 
 * backend:port 
 * request_processing_time 
 * backend_processing_time 
 * response_processing_time 
 * elb_status_code 
 * backend_status_code 
 * received_bytes 
 * sent_bytes 
 * "request" 
 * "user_agent"
 * ssl_cipher 
 * ssl_protocol
 * 
 */
public class ELBAccessLog implements Serializable { 
	private static final Logger logger = Logger.getLogger("Access");

	private String dateTimeString;	
	
	private String elbName;
	
	private String clientIpAddress;
	private int clientPort;
	
	private String backendIpAddress;
	private int backendPort;
	
	private String sourceIpAddress;
	private int sourcePort;
	
	private float requestProcessingTime;
	private float backendProcessingTime;
	private float responseProcessingTime;
	
	private float elbStatusCode;
	private float backendStatusCode;
	
	private long receivedBytes;
	private long sentBytes;
	
	private String request;
	private String userAgent;
	
	private String sslCipher;
	private String sslProtocol;
	
	//timestamp elb client:port backend:port request_processing_time backend_processing_time response_processing_time elb_status_code backend_status_code received_bytes sent_bytes "request" "user_agent" ssl_cipher ssl_protocol

	private static final String LOG_ENTRY_PATTERN =
//    "^(\\S+) (\\S+) (\\S+):(\\S+) (\\S+):(\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) \"([^\"])\" \"([^\"])\" (\\S+) (\\S+)$";
	//"^(\\S+) (\\S+) (\\S+):(\\S+) (\\S+):(\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) \"([^\"])\" \"([^\"])\" - -$";
			//"2016-02-16T01:00:50.309216Z ArabGT-Load-Balancer 105.198.242.106:15610 172.31.14.182:80 0.000042 0.000882 0.000023 304 304 0 0 \"GET http://www.arabgt.com:80/sites/default/files/js/js_pcG4nozC_WcLiZGYbq1zfRAv88ony8ZmiwgBXaRG1ik.js HTTP/1.1\" \"Mozilla/5.0 (iPhone; CPU iPhone OS 9_2_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13D15 [FBAN/FBIOS;FBAV/48.0.0.64.162;FBBV/21316239;FBDV/iPhone8,1;FBMD/iPhone;FBSN/iPhone OS;FBSV/9.2.1;FBSS/2; FBCR/Vodafone;FBID/phone;FBLC/en_US;FBOP/5]\" - -";
			"^(\\S+) (\\S+) (\\S+):(\\S+) (\\S+):(\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) (\\S+) \"([^\"]*)\" \"([^\"]*)\" (\\S+) (\\S+)$";
	private static final Pattern PATTERN = Pattern.compile(LOG_ENTRY_PATTERN);
	
	public ELBAccessLog(
			String timestamp, 
			String elb,
			String clientIpAddress, 
			String clientPort, 
			String backendIpAddress, 
			String backendPort,
			String requestProcessingTime,
			String backendProcessingTime, 
			String responseProcessingTime,
			String elbStatusCode,
			String backendStatusCode,
			String receivedBytes,
			String sentBytes, 
			String request, 
			String userAgent,
			String sslCipher,
			String sslProtocol
			) {


		this.dateTimeString = timestamp;
		this.elbName = elb;
		this.clientIpAddress = clientIpAddress;
		this.clientPort = Integer.parseInt( clientPort ); 
		this.backendIpAddress = backendIpAddress;
		this.backendPort = Integer.parseInt( backendPort );
		this.requestProcessingTime = Float.parseFloat(requestProcessingTime);
		this.backendProcessingTime = Float.parseFloat(backendProcessingTime);
		this.responseProcessingTime = Float.parseFloat(responseProcessingTime);
		this.elbStatusCode = Integer.parseInt( elbStatusCode );
		this.backendStatusCode = Integer.parseInt( backendStatusCode );
		this.receivedBytes = Long.parseLong( receivedBytes );
		this.setSentBytes(Long.parseLong( sentBytes ));
		this.setRequest(request);
		this.setUserAgent(userAgent);
		this.setSslCipher(sslCipher);
		this.setSslProtocol(sslProtocol);
	}

	public static ELBAccessLog parseFromLogLine(String logline) {
		Matcher m = PATTERN.matcher(logline);
		    if (!m.find()) {
		    	System.out.println("Cannot parse logline:  " + logline);
		      logger.log(Level.ALL, "Cannot parse logline  " + logline);
		      throw new RuntimeException("Error parsing logline");
		    }

		    return new ELBAccessLog(
		    		m.group(1),
		    		m.group(2), 
		    		m.group(3), 
		    		m.group(4),
		    		m.group(5),		
		    		m.group(6), 
		    		m.group(7),
		    		m.group(8), 
		    		m.group(9),
		    		m.group(10),
		    		m.group(11),
		    		m.group(12),
		    		m.group(13),
		    		m.group(14),
		    		m.group(15),
		    		m.group(16),
		    		m.group(17)
		    	);
		  }

	public String getSslProtocol() {
		return sslProtocol;
	}

	public void setSslProtocol(String sslProtocol) {
		this.sslProtocol = sslProtocol;
	}

	public String getSslCipher() {
		return sslCipher;
	}

	public void setSslCipher(String sslCipher) {
		this.sslCipher = sslCipher;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public long getSentBytes() {
		return sentBytes;
	}

	public void setSentBytes(long sentBytes) {
		this.sentBytes = sentBytes;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

}
