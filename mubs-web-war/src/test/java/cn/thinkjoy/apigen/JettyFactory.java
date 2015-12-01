package cn.thinkjoy.apigen;


/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 创建Jetty Server的工厂类.
 * 
 * @author calvin
 */
public class JettyFactory {

	private JettyFactory() {
	}

	/**
	 * 创建用于开发运行调试的Jetty Server, 以src/main/webapp为Web应用目录.
	 */
	public static Server buildNormalServer(int port, String contextPath) {
		String DEFAULT_WEBAPP_PATH = "src/main/webapp";
//		Server server = new Server(port);
//		ServletContextHandler root = new ServletContextHandler(
//				ServletContextHandler.SESSIONS);
////		root.setSessionHandler(new SessionHandler());
//		root.setContextPath(contextPath);
//		root.setResourceBase("src/main/webapp");
//		root.setDecorators(List<Decorator>);
//		root.setDescriptor("./web/WEB-INF/web.xml");
//		WebAppContext web = null
////		ServletContextHandler webContext = new WebAppContext("src/main/webapp", contextPath);
//		root.setClassLoader(Thread.currentThread().getContextClassLoader());
//		server.setHandler(root);
//		server.setStopAtShutdown(true);
//		return server;

		Server server = new Server(port);
//		SelectChannelConnector connector = new SelectChannelConnector();
//		connector.setPort(port);
//		// 解决Windows下重复启动Jetty居然不报告端口冲突的问题.
//		connector.setReuseAddress(false);
//		server.setConnectors(new Connector[] { connector });

		WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH, contextPath);
		// 修改webdefault.xml，解决Windows下Jetty Lock住静态文件的问题.
//		webContext.setDefaultsDescriptor(WINDOWS_WEBDEFAULT_PATH);
		server.setHandler(webContext);
		server.setStopAtShutdown(true);
		return server;

	}

	/**
	 * 创建用于Functional Test的Jetty Server:
	 * 1.以src/main/webapp为Web应用目录.
	 * 2.以test/resources/web.xml指向applicationContext-test.xml创建测试环境.
	 */
    @SuppressWarnings("unused")
	public static Server buildTestServer(int port, String contextPath) {
		Server server = buildNormalServer(port, contextPath);
//		((ServletContextHandler) server.getHandler()).addDecorator("src/test/resources/web.xml");
		return server;
	}
}
