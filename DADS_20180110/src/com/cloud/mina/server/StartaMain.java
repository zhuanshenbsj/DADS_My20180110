package com.cloud.mina.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;

import com.cloud.mina.acceptor.InitIoAcceptor;
import com.cloud.mina.filter.PkgDecodeIoFilter;
import com.cloud.mina.filter.decode.mh.PkgDecMHBpComponent;
import com.cloud.mina.filter.decode.mh.PkgDecMHComponent;
import com.cloud.mina.filter.decode.mh.PkgDecMHSportsComponent;
import com.cloud.mina.filter.decode.mh.sports.PkgDecMHSport8OneLeaf;
import com.cloud.mina.filter.decode.mh.sports.PkgDecMHSport8ThreeLeaf;
import com.cloud.mina.filter.decode.mh.sports.PkgDecMHSport8TwoLeaf;
import com.cloud.mina.filter.decode.mh.sports.PkgDecMHSportLoginLeaf;
import com.cloud.mina.filter.decode.mh.sports.PkgDecMHSportLogoutLeaf;
import com.cloud.mina.handler.StrategyFactoryHandler;

public class StartaMain {

	/**
	 * IoAcceptor->IoFilter->IoHandler
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		//建立链接IoAcceptor
		IoAcceptor ioAcceptor = InitIoAcceptor.getInstance();

		//添加过滤器链IoFilter
		PkgDecMHComponent MHdecRoot = new PkgDecMHComponent();
		PkgDecMHSportsComponent mhSportsPkgDec = new PkgDecMHSportsComponent();
		PkgDecMHBpComponent mhBpPkgDec = new PkgDecMHBpComponent();

		PkgDecMHSportLoginLeaf mhSportLoginPkgDec = new PkgDecMHSportLoginLeaf();
		PkgDecMHSportLogoutLeaf mhSportLogoutPkgDec = new PkgDecMHSportLogoutLeaf();
		PkgDecMHSport8OneLeaf mhSportLogin8OnePkgDec = new PkgDecMHSport8OneLeaf();
		PkgDecMHSport8TwoLeaf mhSportLogin8TwoPkgDec = new PkgDecMHSport8TwoLeaf();
		PkgDecMHSport8ThreeLeaf mhSportLogin8ThreePkgDec = new PkgDecMHSport8ThreeLeaf();

		mhSportsPkgDec.addDecode(mhSportLoginPkgDec);
		mhSportsPkgDec.addDecode(mhSportLogoutPkgDec);
		mhSportsPkgDec.addDecode(mhSportLogin8OnePkgDec);
		mhSportsPkgDec.addDecode(mhSportLogin8TwoPkgDec);
		mhSportsPkgDec.addDecode(mhSportLogin8ThreePkgDec);

		//MHdecRoot.addDecode(mhSportsPkgDec);
		//MHdecRoot.addDecode(mhBpPkgDec);
		ioAcceptor.getFilterChain().addLast("mhSportsPkgDec", new PkgDecodeIoFilter(mhSportsPkgDec));
		//添加日志过滤器
		ioAcceptor.getFilterChain().addLast("logging", new LoggingFilter());
		ioAcceptor.getFilterChain().addLast("executors", new ExecutorFilter());
		ioAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
		//设置IoHandler处理具体业务逻辑
		ioAcceptor.setHandler(new StrategyFactoryHandler());

		//监听端口
		ioAcceptor.bind(new InetSocketAddress(8891));
		System.out.println("正在监听8891");
	}
}
