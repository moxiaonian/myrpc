package com.cxd.myrpc.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RpcServer {

	private int port;
	
	public RpcServer(){
	}

	public RpcServer(int port) {
		this.port = port;
	}
	
	public RpcServer port(int port){
		this.port = port;
		return this;
	}
	
	public void start() throws Exception {
		
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel sc) throws Exception {
							sc.pipeline().addLast(getRpcServerHandler());
						}
					});
			
			ChannelFuture f = b.bind().sync();
			System.out.println(RpcServer.class.getName() + " started and listening for connectinos on "
					+ f.channel().localAddress());
			
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
		
	}
	
	public RpcServerHandler getRpcServerHandler(){
		return new RpcServerHandler();
	}

}
