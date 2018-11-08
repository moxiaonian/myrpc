package com.cxd.myrpc;

import com.cxd.myrpc.server.RpcServerRefer;

public class RpcTest {

	public static void main(String[] args) throws Exception {
		RpcServerRefer server = new RpcServerRefer();
		
		server.port(1234).service(new Object()).start();
	}
}
