package com.cxd.myrpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientCall {
	
	/**
	 * call service
	 * @param interfaceClass
	 * @param host
	 * @param port
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T refer(Class<T> interfaceClass, final String host, final int port) throws Exception{
		if (interfaceClass == null)
			throw new IllegalArgumentException("Interface class == null");
		
		if(! interfaceClass.isInterface())
			throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
		
		if(host == null || host.length() == 0)
			throw new IllegalArgumentException("host == null!");
		
		
		System.out.println("Get remote service " + interfaceClass.getName() + " from server " + host + ":" + port);
		
		//JDK动态代理
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
				
				RpcClientRefer clientRefer = new RpcClientRefer();
				clientRefer.bindHostAndPort(host, port).bindInvocation(method, arguments).start();

				Object result = clientRefer.get();
				if (result instanceof Throwable) {
					throw (Throwable) result;
				}
				return result;	
			}});
	}
}
