package com.cxd.myrpc.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RpcClient {
	private String host;
	private int port;
		
	public RpcClient(){
	}
	
	public RpcClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public RpcClient bindHostAndPort(String host, int port){
		if(host == null || host.length() == 0) throw new IllegalArgumentException("host == null!");
		this.host = host;
		this.port = port;
		return this;
	}
	
	
	public void start() throws Exception{
		Bootstrap b = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel sc) throws Exception {
							sc.pipeline().addLast(getRpcClientHandler());
							
						}
					});
			
			ChannelFuture f = b.connect().sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
		
	}
	
	public RpcClientHandler getRpcClientHandler(){
		return new RpcClientHandler();
	}
	
	
}
