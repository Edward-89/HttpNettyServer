package com.eduard.nettyhttpserver.session;

import java.net.InetAddress;
import java.util.*;
/**
 * The class work with session information 
 * @author Eduard Voronkov
 * @version 1.6
 */

public class SessionHandler{
	
	public SessionHandler(){
		
	}
	
	private List<LastFinishedConnection> listLastFinishedConnections =
			new LinkedList<LastFinishedConnection>();
	
	/**
	 * Total count all request's
	 */
	private int requestTotalCount = 0;
	
	/**
	 * Count active client
	 */
	private int activeClientCount = 0;
	
	
	/**
	 * Count redirect
	 */
	private Map<String, Integer> countRedirect = new HashMap<String, Integer>();
	
	/**
	 * Counter request for unique ip
	 */
	private CounterRequest counterRequest = new CounterRequest();
	
	/**
	 * Method performs requests by ip
	 * @param {@link InetAddress} address
	 * @param {@link long} lastTime
	 */
	public synchronized void setCounterRequestOfIP(InetAddress address, long lastTime){
		counterRequest.setPerformsRequestByIP(address, lastTime);
	}
	
	/**
	 * Method former table counter request's by IP
	 * @return {@link String}
	 */
	public synchronized String getCounterRequest(){
		return counterRequest.getInfoCounterRequestToHTML();
	}
	
	/**
	 * Method add new redirect or new count if redirect equals
	 * @param url
	 */
	public synchronized void addRedirect(String url){
		url = url.toLowerCase();
		Integer value = 0;
		if(countRedirect.containsKey(url)){
			value = countRedirect.get(url);
		} 
		countRedirect.put(url, ++value);
	}
	
	/**
	 * Get part of status html page about count redirect's
	 * @return {@link String}
	 */
	public synchronized String getInfoAboutCountRedirect(){
		
		StringBuilder tableRedirect = new StringBuilder();
		tableRedirect.append("<style> table, td, th {"
									+ "border: 1px solid blue;"
									+"}"
									+ "th {"
									+ "background-color: magenta;"
									+ "color: white;} "
									+ "</style>");
		tableRedirect.append("<p><h3>Table of count redirect's</h3></p>"
				 					+ "<table><tr>"
									+ "<th>URL</th>"
									+ "<th>Count Redirect's</th></tr>");
		
		for(String key : countRedirect.keySet()){
			tableRedirect.append("<tr>"
									+ "<td>" + key + "</td>"
									+ "<td>" + countRedirect.get(key) + "</td>"
									+ "</tr>");
		}
		
		tableRedirect.append("</table>");
		
		
		return tableRedirect.toString();
	}
	
	/**
	 * Method for depended count active channel's
	 * <pre>
	 * if parameter charset : 
	 * '+' then add
	 * '-' then remove
	 * </pre>
	 * @param charset
	 */
	public synchronized void setActiveConnectionCount(char charset){
		if(charset == '+') activeClientCount++;
		if(charset == '-') activeClientCount--;
	}
	
	/**
	 * Method answer : 
	 * <pre>
	 * How many active channel's ?
	 * </pre>
	 * @return all active channel's
	 */
	public synchronized int getActiveClientCount(){
		return activeClientCount;
	}
	
	/**
	 * Method answer : 
	 * <pre>
	 * How many total count request's ?
	 * </pre> 
	 * @return all count request's
	 */
	public synchronized int getRequestTotalCount() {
		return requestTotalCount;
	}
	
	/**
	 * Add count request's
	 */
	public synchronized void addRequestCount() {
		requestTotalCount++;
	}
	
	/**
	 * Method instance {@link LastFinishedConnection} and adds to repository {@link LinkedList}.
	 * If rpository has 16 elements that first element removed
	 * @param ip
	 * @param uri
	 * @param lastTime
	 * @param sentBytes
	 * @param receivedBytes
	 * @param speed
	 */
	public synchronized void setNoteAboutConnection(InetAddress ip, String uri,
			long lastTime, int sentBytes, int receivedBytes, double speed) {
		
		LastFinishedConnection lfcs = 	new LastFinishedConnection(ip.toString(),
				uri.toLowerCase(), new Date(lastTime).toString(), sentBytes, receivedBytes, speed);
		
		if(listLastFinishedConnections.size() == 16){
			listLastFinishedConnections.remove(0);
		}
		listLastFinishedConnections.add(lfcs);
	}
	
	public synchronized String getLastNoteFinishedConnections(){
		StringBuilder table = new StringBuilder();
		table.append("<p><h3>Last 16 finished connections : </h3></p>");
		table.append("<table><tr>")
			 .append("<th>IP</th>")
			 .append("<th>URI</th>")
			 .append("<th>Timestamp</th>")
			 .append("<th>Received_Bytes</th>")
			 .append("<th>SendBytes</th>")
			 .append("<th>Speed (bytes/sec)</th>")
			 .append("</tr>");
		
		for(LastFinishedConnection lfc : listLastFinishedConnections){
			table.append("<tr>")
				 .append("<td>" + lfc.getIP() 			 + "</td>")
				 .append("<td>" + lfc.getURI() 			 + "</td>")
				 .append("<td>" + lfc.getTimestamp() 	 + "</td>")
				 .append("<td>" + lfc.getReceivedBytes() + "</td>")
				 .append("<td>" + lfc.getSendBytes() 	 + "</td>")
				 .append("<td>" + lfc.getSpeed() 		 + "</td>")
				 .append("</tr>");
		}
		
		table.append("</table>");
		
		
		return table.toString();
	}
	
	
	
	

}
