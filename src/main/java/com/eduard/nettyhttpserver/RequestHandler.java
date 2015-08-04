package com.eduard.nettyhttpserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;


/**
 * This class is based on the class {@link HttpRequest} creates
 * an instance of the class {@link FullHttpResponse}
 * @author Eduard Voronkov
 * @see HttpServerHandler
 */
public class RequestHandler {

	public static FullHttpResponse getResponce(ChannelHandlerContext ctx, HttpRequest request) throws InterruptedException {
		
		QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
		String uri = decoder.path();
		
		if(uri.equalsIgnoreCase("/hello")){
			Thread.sleep(10000L);
			byte[] content =  "<center><h1>Hello World</h1></center>".getBytes();
			return new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));
		} 
		
		if(uri.equalsIgnoreCase("/status")){
			return null;
		}
		
		if(uri.equalsIgnoreCase("/redirect")){
			FullHttpResponse response = null;
			
			if(decoder.parameters().get("url") != null){
				String uriRedirect = decoder.parameters().get("url").get(0);
				int shemaPart = uriRedirect.indexOf("://");
				if(shemaPart == -1){
					uriRedirect = String.valueOf("http://" + uriRedirect);
				}
				response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
		        response.headers().set(LOCATION, uriRedirect);
		        return response;
			} 
			byte[] content = String.valueOf("<body text='#ff0000'><h2>"
					+ "Not found parameter url "
					+ "</h2></body> "
					).getBytes();
			return new DefaultFullHttpResponse(HTTP_1_1, NOT_IMPLEMENTED, Unpooled.wrappedBuffer(content));
		} else {
			String answer = "<html><body>"
					+ "<center><h3><p>Hi, People!</p></h3></center>"
					+ "<center><h3><p>This is my testing task</p></h3></center>"
					+ "<center><h3><p>Please, go to the link's</p></h3></center>"
					+ "<center><h3><p><a href='/hello'>hello page</a></p></h3></center>"
					+ "<center><h3><p><a href='/status'>status server</a></p></h3></center>"
					+ "</body></html>";
			byte[] content = answer.getBytes();
					
			return new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));		
		}
	} 

}
