package com.cxd.myrpc;

import com.cxd.myrpc.server.RpcServerCall;
//import rpc.RpcFramework;

public class RpcProvider {

	public static void main(String[] args) throws Exception {
//		HelloService service = new HelloServiceImpl();
//		RpcFramework.export(service,  1234);
		
		HelloService service = new HelloServiceImpl();		
		
		RpcServerCall.export(service, 1234);
		
	}
}
