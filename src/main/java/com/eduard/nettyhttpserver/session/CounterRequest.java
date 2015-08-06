package com.eduard.nettyhttpserver.session;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The class acts as the counter of requests for each IP
 * @author Eduard Voronkov
 *
 */
public class CounterRequest {
	
	public CounterRequest(){
		
	}
	
	/**
	 * This inner class saver attribute
	 * @param <V>
	 * @param <T>
	 */
	private class Attribute<V, T> {
		V v;
		T t;
	}
	
	/**
	 * Method setter attribute
	 * @param value
	 * @param time
	 * @return {@link Attribute}
	 */
	private <V,T> Attribute<V, T> setAttribute(V value, T time){
		Attribute<V, T> result = new Attribute<V, T>();
		result.v = value;
		result.t = time;
		return result;
	}
	
	/**
	 * Data repository
	 */
	private Map<String, Attribute<Integer, Date>> countReqestOfIP =
						new HashMap<String, Attribute<Integer, Date>>();
	
	/**
	 * Method performs requests by ip
	 * @param {@link InetAddress} address
	 * @param {@link long} lastTime
	 */
	public void setPerformsRequestByIP(InetAddress address, long lastTime){
		
		String ip = address.toString();
		Date date = new Date(lastTime);
		Integer value = 0;
		if(countReqestOfIP.containsKey(ip)){
			value = countReqestOfIP.get(ip).v;
		}
		countReqestOfIP.put(ip, setAttribute(++value, date));
		
	}
	
	/**
	 * Method former table counter request's by IP
	 * @return {@link String}
	 */
	public String getInfoCounterRequestToHTML(){
		
		StringBuilder table = new StringBuilder();
		
		table.append("<p><h3>Counter request's by IP</h3></p>"
					+ "<table><tr>"
					+ "<th>IP</th>"
					+ "<th>Count_request's</th>"
					+ "<th>Last_time_request</th></tr>");
		for(String ip : countReqestOfIP.keySet()){
			table.append("<tr>"
					+ "<td>" + ip + "</td>"
					+ "<td>" + countReqestOfIP.get(ip).v.toString() + "</td>"
					+ "<td>" + countReqestOfIP.get(ip).t.toString() + "</td></tr>");
		}
		table.append("</table>");
		return table.toString();
	}
}
