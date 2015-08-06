package com.eduard.nettyhttpserver;



import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.eduard.nettyhttpserver.session.SessionHandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
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
			
			long lastTime = System.currentTimeMillis(); //timestamp
			InetAddress ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress();
			session.setCounterRequestOfIP(ip, lastTime);
			int receivedBytes = 0;
//					((ByteBufHolder) msg ).content().readableBytes();
			
			HttpRequest request = (HttpRequest) msg;
			System.out.println(request.getDecoderResult());
			System.out.println(request);
			if (HttpHeaders.is100ContinueExpected(request)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            
            boolean keepAlive = HttpHeaders.isKeepAlive(request);
            FullHttpResponse response = RequestHandler.getResponce(ctx, request, session);
            
            long deltaTime = lastTime - System.currentTimeMillis();
            int sentBytes = response.content().readableBytes();
            //multiple 1000, because the purpose of the dimension bytes/sec
            double speed = (deltaTime != 0) ? ((double)receivedBytes * 1000)/deltaTime : 0;
            session.setNoteAboutConnection(ip, request.getUri(), lastTime,
            		sentBytes, receivedBytes, speed);
            
            response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
            response.headers().set(CONTENT_LENGTH, sentBytes);

            if (!keepAlive) {
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }
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
