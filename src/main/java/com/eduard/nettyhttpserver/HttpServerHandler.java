package com.eduard.nettyhttpserver;



import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.eduard.nettyhttpserver.session.SessionHandler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Values;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * This class {@link HttpServerHandler} defines the business logic of the server
 * @author Eduard Voronkov
 * @see HttpServer
 */


public class HttpServerHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * Repository for session information
	 */
	private SessionHandler session;
	
	public HttpServerHandler(SessionHandler session) {
		this.session = session;
	}
	

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx){
		ctx.flush();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
		if(msg instanceof HttpRequest){
			//timestamp
			long lastTime = System.currentTimeMillis();
			InetAddress ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress();
			session.setCounterRequestOfIP(ip, lastTime);
			
			HttpRequest request = (HttpRequest) msg;
			
			long receivedBytes = request.toString().getBytes().length;
			session.setCountUniqueRequest(ip, request.getUri().toLowerCase());
			
			if (HttpHeaders.is100ContinueExpected(request)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            
            boolean keepAlive = HttpHeaders.isKeepAlive(request);
            FullHttpResponse response = RequestHandler.getResponce(ctx, request, session);
            
            long deltaTime  = System.currentTimeMillis() - lastTime;
            long sentBytes = response.content().readableBytes();
            //multiple 1000, because the purpose of the dimension bytes/sec
            
            
            response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
            response.headers().set(CONTENT_LENGTH, sentBytes);

            if (!keepAlive) {
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }
            
            long speed = (deltaTime != 0) ? receivedBytes/deltaTime : 0;
            session.setNoteAboutConnection(ip, request.getUri(), lastTime,
            		sentBytes, receivedBytes, speed);
		} 
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        session.setActiveConnectionCount('+');
        ctx.fireChannelRegistered();
    }

	@Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		session.setActiveConnectionCount('-');
		ctx.fireChannelUnregistered();
		
    }

}
