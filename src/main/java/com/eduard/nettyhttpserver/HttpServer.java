package com.eduard.nettyhttpserver;

import com.eduard.nettyhttpserver.session.SessionHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * The HttpServer is simple relies server 
 * @author Eduard Voronkov 
 *
 */

public class HttpServer {
	/**
	 * Default server port
	 */
	private static final int SERVER_PORT = 8080;
	/**
	 * Instance {@link SessionHandler} for server
	 */
	private static SessionHandler session = new SessionHandler(); 
		
	
	public static void main(String[] args) throws Exception {
		
		/**
		 * Configure the server
		 */
		EventLoopGroup bossGroup 	= new NioEventLoopGroup(1);
		EventLoopGroup workerGroup	= new NioEventLoopGroup();
		
		try{
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					 .channel(NioServerSocketChannel.class)
					 .handler(new LoggingHandler(LogLevel.INFO))
					 .childHandler(new ChannelInitializer<SocketChannel>() {
							@Override
							protected void initChannel(SocketChannel ch) throws Exception {
								ChannelPipeline pipeline = ch.pipeline();
								pipeline.addLast(new HttpRequestDecoder());
								pipeline.addLast(new HttpResponseEncoder());
								pipeline.addLast(new HttpServerHandler(session));
							}
						})
					 .bind(SERVER_PORT).sync().channel()
					 .closeFuture().sync();
			
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
