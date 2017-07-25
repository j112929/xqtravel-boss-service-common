package com.xqcx.boss.data;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.xqcx.boss.context.DynamicDataSourceHolder;
import com.xqcx.common.annotation.DataSource;

/**
 * 
 * @ClassName: DataSourceAspect
 * @Description: DataSourceAspect use read writer 
 * @author: sz.feng
 * @date: 2017年3月13日
 */
@Component
@Aspect
@Order(-9999) 
public class DataSourceAspect {
	
	/**
	 * @Title: changeDataSource
	 * @Description: before切入点
	 * @throws
	 */
	@Pointcut("execution(* com.xqcx.boss.dao..*.*(..)) || execution(* com.xqcx.dao..*.*(..))")
	public void changeDataSource() {}
	
	/**
	 * @Title: changeDataSource
	 * @Description: after切入点
	 * @throws
	 */
	@Pointcut("execution(* com.xqcx.boss.dao..*.*(..)) || execution(* com.xqcx.dao..*.*(..))")
	public void changeDataSourceAfter() {}
	
	/**
	 * @Title: before
	 * @Description: before
	 * @param point
	 * @throws NoSuchMethodException
	 * @throws SecurityException  void
	 * @throws
	 */
	@Before("changeDataSource()")
	public void before(JoinPoint point) throws NoSuchMethodException, SecurityException {
		
		Object target = point.getTarget();
		String method = point.getSignature().getName();
		Class<?>[] classz = target.getClass().getInterfaces();
		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
		
		Method m = classz[0].getMethod(method, parameterTypes);
		DataSource annotation = getAnnotation(m);
		if (annotation != null) {
			DynamicDataSourceHolder.putDataSource(annotation.value());
		}
	}
	
	/**
	 * @Title: after
	 * @Description: after
	 * @throws
	 */
	@After("changeDataSourceAfter()")
	public void after() {
		DynamicDataSourceHolder.clearType();
	}
	
	/**
	 * @Title: getAnnotation
	 * @Description: 利用反射根据method获取方法上面的注解
	 * @param method
	 * @return  DataSource
	 * @throws
	 */
	private DataSource getAnnotation(Method method) {
		DataSource dataSource = method.getAnnotation(DataSource.class);
		if (dataSource != null) {
			return dataSource;
		}
		return method.getDeclaringClass().getAnnotation(DataSource.class);
	}
		
	
	
	
	
	
	
	
	/*@Pointcut("execution (* com.xqcx.boss.dao.*.select*(..)) || execution (* com.xqcx.boss.dao..*.find*(..)) || execution (* com.xqcx.boss.dao.*.get*(..)) || execution (* com.xqcx.boss.dao.*.query*(..))")
	public void readMethodPointcut() {	
	}
	
	@Pointcut("execution (* com.xqcx.boss.dao.*.insert*(..)) || execution (* com.xqcx.boss.dao.*.update*(..)) || execution (* com.xqcx.boss.dao.*.delete*(..))")
	public void writeMethodPointcut() {
	}
	
	@Before("readMethodPointcut()")
	public void switchReadDataSource(JoinPoint point) {
		DynamicDataSourceHolder.clearType();
		log.info("~~~~~~~~~~switch dataSourceR~~~~~~~~~~~~~~~~~~");
		DynamicDataSourceHolder.putDataSource("dataSourceR");

	}

	@Before("writeMethodPointcut()")
	public void switchWriteDataSource() {
		DynamicDataSourceHolder.clearType();
		log.info("~~~~~~~~~~switch dataSourceRW~~~~~~~~~~~~~~~~~~");
		DynamicDataSourceHolder.putDataSource("dataSourceRW");
	}*/
	
}

