package com.cxd.myrpc.server;

public class RpcServerCall {

	/**
	 * export service
	 * @param service
	 * @param port
	 * @throws Exception
	 */
	public static void export(final Object service, int port) throws Exception {
		if(service == null)
			throw new IllegalArgumentException("service instance == null");
		if(port <= 0 || port > 65535)
			throw new IllegalArgumentException("Invalid port" + port);
		
		System.out.println("Export service " + service.getClass().getName() + " on port " + port);
		
		RpcServerRefer serverRefer = new RpcServerRefer();
		serverRefer.port(port).service(service).start();
	}
	
}
