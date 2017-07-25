package com.xqcx.boss.context;

import com.xqcx.common.model.BossUserSession;

/**
 * 
 * Created by zhanggt on 2016-4-20.
 * 
 */
public class ServiceContext {

	private static ThreadLocal<ServiceContext> localContext = new ThreadLocal<ServiceContext>();

	private BossUserSession session;

	public BossUserSession getSession() {
		return session;
	}

	public void setSession(BossUserSession session) {
		this.session = session;
	}

	public ServiceContext(BossUserSession session) {
		this.session = session;
	}

	/**
	 * 初始化WebContext
	 */
	public static void initServiceContext(BossUserSession session) {
		ServiceContext context = new ServiceContext(session);
		initServiceContext(context);
	}

	/**
	 * 设置WebContext到ThreadLocal中
	 * 
	 * @param webContext
	 *            需要设置到WebContext
	 */
	public static void initServiceContext(ServiceContext webContext) {
		localContext.set(webContext);
	}

	public static ServiceContext getContext() {
		return localContext.get();
	}
}
