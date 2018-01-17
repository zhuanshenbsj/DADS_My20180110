package com.cloud.mina.acceptor;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * 单例模式创建连接
 * @author user
 *
 */
public class InitIoAcceptor {

	private InitIoAcceptor() { //私有构造方法，防止被实例化  
	}

	/*使用一个内部类来维护单例 */
	private static class SingletonFactory {
		private static IoAcceptor instance = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
	}

	public static IoAcceptor getInstance() { //获取实例  
		return SingletonFactory.instance;
	}

	/* 如果该对象被用于序列化，可以保证对象在序列化前后保持一致 */
	public Object readResolve() {
		return getInstance();
	}
}
