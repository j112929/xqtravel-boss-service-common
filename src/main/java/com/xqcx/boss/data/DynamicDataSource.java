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



public class DynamicDataSource extends AbstractRoutingDataSource {
	
	/**
	 * <p>Title: determineCurrentLookupKey</p>
	 * <p>Description:动态切换数据源实现 </p>
	 * @return
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
	 */
	@Override
	protected Object determineCurrentLookupKey() 
	{
		return DynamicDataSourceHolder.getDataSouce();
	}

}

