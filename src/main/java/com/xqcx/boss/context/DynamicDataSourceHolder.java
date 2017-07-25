/**
 * DynamicDataSourceHolder.java
 * com.xq.common.data
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2017年1月17日 		Administrator
 *
 * Copyright (c) 2017, TNT All Rights Reserved.
 */

package com.xqcx.boss.context;

/**
 * @ClassName: DynamicDataSourceHolder
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author <a href="mailto:fsz19900908@163.com">冯升志</a> 于 与 2017年1月17日 创建
 */
public class DynamicDataSourceHolder {
	/**
	 * 线程变量副本
	 */
	public static final ThreadLocal<String> holder = new ThreadLocal<String>();
	
	/**
	 * @Title: putDataSource
	 * @Description: set数据源
	 * @param name  void
	 * @throws
	 */
	public static void putDataSource(String name) {
		clearType();
		holder.set(name);
	}
	
	/**
	 * @Title: getDataSouce
	 * @Description: get数据源
	 * @return  String
	 * @throws
	 */
	public static String getDataSouce() {
		return holder.get();
	}
	
	/**
	 * @Title: clearType
	 * @Description: 清除数据源类型
	 * @throws
	 */
	public static void clearType() {
		System.out.println("clear dataSource~~~~~~~~~~~~");
		holder.remove();
    }
}
