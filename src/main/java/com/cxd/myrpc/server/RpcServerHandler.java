package com.cxd.myrpc.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class RpcServerHandler extends ChannelInboundHandlerAdapter{

	private Object service;
	
	public RpcServerHandler(){
	}
	
	public RpcServerHandler(Object service){
		this.service = service;
	}
	
	public RpcServerHandler bindService(Object service){
		this.service = service;
		return this;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		if(service == null){
//			System.out.println("server start!");
//			return;
//		}

		ByteBuf in = (ByteBuf)msg;
		
		//handle the ByteBuf
		int length = in.readableBytes();
		byte[] bytes = new byte[length];
		ByteArrayInputStream inByteStream = new ByteArrayInputStream(bytes);
		for(int i=0; i<length; i++){
			bytes[i] = in.readByte();
		}
		
		in.release();  //release by hand
		in = null;
		
		ObjectInputStream inObjStream = new ObjectInputStream(inByteStream);
		
		String methodName = inObjStream.readUTF();
		Class<?>[] parameterTypes = (Class<?>[])inObjStream.readObject();
		Object[] arguments = (Object[])inObjStream.readObject();
		
		//serialize the result, use JDK
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		ObjectOutputStream outObjStream = new ObjectOutputStream(outByteStream);
		//invoke the method
		try{
			Method method = service.getClass().getMethod(methodName, parameterTypes);
			Object result = method.invoke(service, arguments);
			outObjStream.writeObject(result);
		}catch(Throwable t){
			outObjStream.writeObject(t);
		}
		
		ByteBuf out = Unpooled.copiedBuffer(outByteStream.toByteArray());
		ctx.write(out);

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
				.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	
}
