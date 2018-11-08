package com.cxd.myrpc.server;

public class RpcServerRefer extends RpcServer{

	public Object service;
	
	public RpcServerRefer(){
	}
	
	public RpcServerRefer(int port, Object service) {
		super(port);
		this.service = service;
	}

	public RpcServerRefer port(int port){
		super.port(port);
		return this;
	}
	
	public RpcServerRefer service(Object service){
		this.service = service;
		return this;
	}
	
	@Override
	public RpcServerHandler getRpcServerHandler() {
		if(service == null) throw new IllegalArgumentException("service instance == null");
		
		return new RpcServerHandler(service);
	}
	
	

}
