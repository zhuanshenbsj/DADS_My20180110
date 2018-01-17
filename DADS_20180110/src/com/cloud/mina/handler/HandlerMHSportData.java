package com.cloud.mina.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.cloud.mina.entity.packageData.PackageData;
import com.cloud.mina.entity.packageState.IState;
import com.cloud.mina.entity.packageState.sports.StateMHSport;
import com.cloud.mina.entity.packageState.sports.StateMHSportLogin;
import com.cloud.mina.entity.packageState.sports.StateMHSportLogout;
import com.cloud.mina.entity.packageState.sports.StateMHSportNo8One;
import com.cloud.mina.entity.packageState.sports.StateMHSportNo8Three;
import com.cloud.mina.entity.packageState.sports.StateMHSportNo8Two;

public class HandlerMHSportData implements IHandlerMHData {

	//定义变量区
	private static IState state;

	private static Map<String, Class> stateMap = new HashMap<String, Class>();
	static {
		stateMap.put("login", StateMHSportLogin.class);
		stateMap.put("No8-1", StateMHSportNo8One.class);
		stateMap.put("No8-2", StateMHSportNo8Two.class);
		stateMap.put("No8-3", StateMHSportNo8Three.class);
		stateMap.put("logout", StateMHSportLogout.class);
	}

	public static void setState(StateMHSport state) {
		HandlerMHSportData.state = state;
	}

	@Override
	public void handle(IoSession session, Object message) {
		//1.根据数据包的头，来调用具体的状态类(状态模式)
		try {
			if (message != null && message instanceof PackageData) {
				PackageData pkgData = (PackageData) message;
				setState((StateMHSport) stateMap.get(pkgData.getType()).newInstance());
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (state != null) {
			state.handlePackageObject(session, message);
		}

	}

}
