package com.cxd.myrpc;

import com.cxd.myrpc.client.RpcClientCall;

public class RpcConsumer {

	public static void main(String[] args) throws Exception {
//		HelloService service = RpcClientCall.refer(HelloService.class, "127.0.0.1", 1234);
//		
//		String hello = service.hello("World");
//		System.out.println(hello);
		
		final HelloService service = RpcClientCall.refer(HelloService.class, "127.0.0.1", 1234);
		for (int i=0; i<Integer.MAX_VALUE; i++){
			new Thread(new Runnable(){

				@Override
				public void run() {
					String hello = service.hello("World");
					System.out.println(hello);
				}
				
			}).start();

			Thread.sleep(2);
		}
		
	}
}
