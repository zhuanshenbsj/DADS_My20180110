package com.cloud.mina.entity.packageState;

import org.apache.mina.core.session.IoSession;

public interface IState {
	public boolean handlePackageObject(IoSession session, Object message);

}
