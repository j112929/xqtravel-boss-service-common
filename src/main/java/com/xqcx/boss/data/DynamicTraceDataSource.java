/**
 * DynamicDataSource.java
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

package com.xqcx.boss.data;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.xqcx.boss.context.DynamicDataSourceHolder;

public class DynamicTraceDataSource extends AbstractRoutingDataSource {
	
	/**
	 * <p>Title: determineCurrentLookupKey</p>
	 * <p>Description:动态切换数据源实现 </p>
	 * @return
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
	 */
	@Override
	protected Object determineCurrentLookupKey() 
	{	System.out.println("行程轨迹动态数据源~~~");
		return DynamicDataSourceHolder.getDataSouce();
	}

}

