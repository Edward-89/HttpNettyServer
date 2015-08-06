package com.eduard.nettyhttpserver.session;

/**
 * Class repository by last finished connection
 * @author Eduard Voronkov
 *
 */
public class LastFinishedConnection {
	
	private String ip;
	private String uri;
	private String timestamp;
	private long sendBytes;
	private long receivedBytes;
	private long speed;
	
	/**
	 * One constructor with parameters
	 * @param ip
	 * @param uri
	 * @param timestamp
	 * @param sendBytes
	 * @param receivedBytes
	 * @param speed
	 */
	public LastFinishedConnection(String ip, String uri, String timestamp,
			long sendBytes, long receivedBytes, long speed) {
		super();
		this.ip = ip;
		this.uri = uri;
		this.timestamp = timestamp;
		this.sendBytes = sendBytes;
		this.receivedBytes = receivedBytes;
		this.speed = speed;
	}
	
	/*
	 * Getters.
	 * The method provides access to private instance fields
	 */
	
	public String getIP(){
		return ip;
	}
	
	public String getURI(){
		return uri;
	}
	
	public String getTimestamp(){
		return timestamp;
	}
	
	public String getSendBytes(){
		return String.valueOf(sendBytes);
	}
	
	public String getReceivedBytes(){
		return String.valueOf(receivedBytes);
	}
	
	public String getSpeed(){
		
		return String.valueOf(speed);
	}
	
	
	
	
	
	

}
