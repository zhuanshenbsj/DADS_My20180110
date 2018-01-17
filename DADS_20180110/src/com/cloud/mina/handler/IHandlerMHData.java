package com.cloud.mina.handler;

import org.apache.mina.core.session.IoSession;

public interface IHandlerMHData {
	public void handle(IoSession session, Object message);
}
