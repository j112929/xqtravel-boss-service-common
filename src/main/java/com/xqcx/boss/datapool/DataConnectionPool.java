package com.xqcx.boss.datapool;

import com.alibaba.druid.pool.DruidDataSource;

public class DataConnectionPool extends DruidDataSource{
	
	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = -180618972386293512L;

	public String getPassword() {
		try {
			return DesUtil.decrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
