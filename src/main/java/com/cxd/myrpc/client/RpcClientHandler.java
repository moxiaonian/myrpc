package com.cxd.myrpc.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.util.CharsetUtil;

@Sharable
public class RpcClientHandler extends ChannelInboundHandlerAdapter{
	
	private Method method;
	private Object[] arguments;

	private Object result;
	
	public RpcClientHandler(){}
	
	public RpcClientHandler bindIncocation(Method method, Object[] arguments){
		if(method == null) throw new NullPointerException("method");
		if(arguments == null) throw new NullPointerException("arguments");
		
		this.method = method;
		this.arguments = arguments;
		return this;
	}
	
	public RpcClientHandler(Method method, Object[] arguments){
		if(method == null) throw new NullPointerException("method");
		if(arguments == null) throw new NullPointerException("arguments");
		
		this.method = method;
		this.arguments = arguments;
	}
	
	public Object getResult(){
		return result;
	}
	
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf in = (ByteBuf)msg;
		
		int length = in.readableBytes();
		byte[] bytes = new byte[length];
		ByteArrayInputStream inByteStream = new ByteArrayInputStream(bytes);
		for(int i=0; i<length; i++){
			bytes[i] = in.readByte();
		}
		
		in.release();  //release by hand
		in = null;

		ObjectInputStream inObjStream = new ObjectInputStream(inByteStream);
		
		result = inObjStream.readObject();
	}

//	protected void channelRead(ChannelHandlerContext paramChannelHandlerContext, ByteBuf in) throws Exception {
////		System.out.println(
////				"Client received: " + in.toString(CharsetUtil.UTF_8));
////		if(method == null && arguments == null){  //the handler start
////			System.out.println("connnet success!");
////			return;
////		}
//		
//		int length = in.readableBytes();
//		byte[] bytes = new byte[length];
//		ByteArrayInputStream inByteStream = new ByteArrayInputStream(bytes);
//		for(int i=0; i<length; i++){
//			bytes[i] = in.readByte();
//		}
//		
//		in.release();  //release by hand
//		in = null;
//
//		ObjectInputStream inObjStream = new ObjectInputStream(inByteStream);
//		
//		result = inObjStream.readObject();
////		System.out.println("Client received result: " + result);
//	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		if(method == null && arguments == null){  //the handler start
//			System.out.println("Client start, try connnet...");
//			return;
//		}
		
		//序列化,采用JDK原生的序列化
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream  objectStream = new ObjectOutputStream(byteStream);
		objectStream.writeUTF(method.getName());
		objectStream.writeObject(method.getParameterTypes());
		objectStream.writeObject(arguments);
		
		ByteBuf out = Unpooled.copiedBuffer(byteStream.toByteArray());
		ctx.writeAndFlush(out);		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	

}
