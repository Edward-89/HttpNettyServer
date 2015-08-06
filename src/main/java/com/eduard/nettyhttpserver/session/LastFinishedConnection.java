package com.eduard.nettyhttpserver.session;

public class LastFinishedConnection {
	
	private String ip;
	private String uri;
	private String timestamp;
	private int sendBytes;
	private int receivedBytes;
	private double speed;
	
	public LastFinishedConnection(String ip, String uri, String timestamp,
			int sendBytes, int receivedBytes, double speed) {
		super();
		this.ip = ip;
		this.uri = uri;
		this.timestamp = timestamp;
		this.sendBytes = sendBytes;
		this.receivedBytes = receivedBytes;
		this.speed = speed;
	}
	
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
		long pre = (long)(speed * 100);
		double speedOk = ((double) pre)/100;
		
		return String.valueOf(speedOk);
	}
	
	
	
	
	
	

}
