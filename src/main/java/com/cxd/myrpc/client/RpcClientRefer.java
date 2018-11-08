package com.cxd.myrpc.client;

import java.lang.reflect.Method;

public class RpcClientRefer extends RpcClient{

	private Method method;
	private Object[] arguments;
	
	private static RpcClientHandler rpcClientHandler;
	
	public RpcClientRefer(){
	}
	
	public RpcClientRefer bindHostAndPort(String host, int port){
		if(host == null || host.length() == 0) throw new IllegalArgumentException("host == null!");
		super.bindHostAndPort(host, port);
		return this;
	}
	
	public RpcClientRefer(String host, int port, Method method, Object[] arguments){
		super(host, port);
		this.method = method;
		this.arguments = arguments;
	}

	public RpcClientRefer bindInvocation(Method method, Object[] arguments){
		this.method = method;
		this.arguments = arguments;
		return this;
	}
	
	
	@Override
	public RpcClientHandler getRpcClientHandler() {
		synchronized (this) {
			if(rpcClientHandler == null){
				rpcClientHandler = new RpcClientHandler(method, arguments);
			}
		}
		return rpcClientHandler;
	}
		
	public Object get(){
		if(rpcClientHandler == null){
			throw new IllegalStateException();
		}
		return rpcClientHandler.getResult();
	}
	
}
