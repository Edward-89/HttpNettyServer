package com.eduard.nettyhttpserver;


import com.eduard.nettyhttpserver.session.SessionHandler;

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
	
	/**
	 * <pre> Method generates a response {@link FullHttpResponse} depending on the request {@link HttpRequest}</pre>
	 * @param ctx
	 * @param request
	 * @param session
	 * @return {@link FullHttpResponce}
	 * @throws InterruptedException
	 */
	public static FullHttpResponse getResponce(ChannelHandlerContext ctx, HttpRequest request,
			SessionHandler session) throws InterruptedException {
		
		QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
		String uri = decoder.path();
		
		if(uri.equalsIgnoreCase("/hello")){
			session.addRequestCount();
			Thread.sleep(10000L);
			byte[] content =  String.valueOf("<center><style>h1 { font-size: 150px;}</style>"
					+ "<h1>Hello World</h1></center>").getBytes();
			return new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));
		} 
		
		if(uri.equalsIgnoreCase("/status")){
			session.addRequestCount();
			return getStatus(ctx, request, session);
		}
		
		if(uri.equalsIgnoreCase("/redirect")){
			session.addRequestCount();
			FullHttpResponse response = null;
			
			if(decoder.parameters().get("url") != null){
				String uriRedirect = decoder.parameters().get("url").get(0);
				int shemaPart = uriRedirect.indexOf("://");
				if(shemaPart == -1){
					uriRedirect = String.valueOf("http://" + uriRedirect);
				}
				session.addRedirect(uriRedirect);
				response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
		        response.headers().set(LOCATION, uriRedirect);
		        return response;
			} 
			byte[] content = String.valueOf("<style>h2{ color: #ff0000 }</style><h2>"
					+ "Not found parameter url "
					+ "</h2></body> "
					).getBytes();
			return new DefaultFullHttpResponse(HTTP_1_1, NOT_IMPLEMENTED, Unpooled.wrappedBuffer(content));
		} else {
			session.addRequestCount();
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
	
	
	/**
	 * The method formate Status Page
	 * @param ctx
	 * @param request
	 * @return {@link FullHttpResponse}
	 */
	private static FullHttpResponse getStatus(ChannelHandlerContext ctx,
			HttpRequest request, SessionHandler session) {
		
		byte[] content = String.valueOf("<h2><p>STATUS PAGE</p></h2>"
				+ "<p><h3>Total count request's : " + session.getRequestTotalCount()
				+ "</h3></p><p><h3>Count open channels now : "
				+ session.getActiveClientCount() + "</h3></p>"
				+ session.getInfoAboutCountRedirect()
				+ session.getUniqueRequests()
				+ session.getCounterRequest()
				+ session.getLastNoteFinishedConnections()).getBytes();
		
		return new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));
	} 

}
