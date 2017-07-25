package com.xqcx.boss.context;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xqcx.basic.api.SequenceService;
import com.xqcx.common.annotation.Role;
import com.xqcx.common.dto.GeneralEnter;
import com.xqcx.common.exception.BusinessException;
import com.xqcx.common.model.BossUserSession;
import com.xqcx.constants.BusinessCodes;
import com.xqcx.constants.RedisCodes;
import com.xqcx.session.SessionClientFactory;

/**
 * 
 * Created by zhanggt on 2016-4-20.
 * 
 */
@Component
@Aspect
@Slf4j
public class UserRoleCheckInterceptor implements Ordered {

	@Autowired
	private JedisCluster jedisCluster;

	// @Pointcut("execution(* com.xqcx.boss..*ServiceImpl.*(..)))")
	@Pointcut("execution(* com.xqcx.boss.service..*.*(..))")
	public void sercicesPointcut() {
	}

	/**
	 * 检测是否符合登录权限（基于AspectJ框架）
	 * 
	 * @param pjp
	 *            ProceedingJoinPoint
	 * @return 方法调用的结果
	 * @throws Throwable
	 *             反射调用发生异常
	 */
	@Around("sercicesPointcut()")
	public Object checkLoginRole(ProceedingJoinPoint pjp) throws Throwable {
		if (pjp.getTarget() instanceof SequenceService) {
			return pjp.proceed();
		}
		Signature signature = pjp.getSignature();
		if (!(signature instanceof MethodSignature)) {
			return pjp.proceed();
		}
		/**
		 * 
		 * MethodSignature methodSignature = (MethodSignature)signature; Method
		 * method = methodSignature.getMethod();
		 * 
		 */
		Class<?>[] parameterTypes = ((MethodSignature) pjp.getSignature()).getMethod().getParameterTypes();
		Method realMethod = pjp.getTarget().getClass().getMethod(signature.getName(), parameterTypes);

		Role role = getAnnotation(realMethod);
		//log.info("pjp.getArgs: {}" + pjp.getArgs());
		// 如果无相应配置则不进行角色识别
		if (role == null) {
			return pjp.proceed();
		}
		if (pjp.getArgs() == null || pjp.getArgs().length == 0) {
			throw new BusinessException(BusinessCodes.User.USER_NOT_LOGIN, "Please enter sessionkey");
		}
		Object object = pjp.getArgs()[0];
		//log.info("UserRoleCheckInterceptor.checkLoginRole arg:" + JSON.toJSONString(object));
		if (!(object instanceof GeneralEnter)) {
			return pjp.proceed();
		}
		GeneralEnter generalPara = ((GeneralEnter) pjp.getArgs()[0]);
		//log.info("generalPara: {}" + generalPara);
		checkLogin(role, generalPara);
		return pjp.proceed();

	}

	/**
	 * 获取角色判断配置，优先取method上的配置，如果没有，则取Controller上的配置，如果都没则认为没有。 基于业务考虑，对@RequestMapping的方法才进行权限校验
	 * 
	 * @param method
	 *            执行的切向方法
	 * @return 此方法实际需要被使用的role配置。null-如果未配置
	 */
	private Role getAnnotation(Method method) {
		/*
		 * // 不存在RequestMapping的请求不认为是web的直接请求，不做处理
		 * if(!method.isAnnotationPresent(RequestMapping.class)) { return null;
		 * }
		 */
		Role role = method.getAnnotation(Role.class);
		if (role != null) {
			return role;
		}

		return method.getDeclaringClass().getAnnotation(Role.class);
	}

	/**
	 * 判断是否登录超时
	 * 
	 * @param role
	 *            角色配置
	 */
	private void checkLogin(Role role, GeneralEnter generalPara) {
		String sessionKey = generalPara.getSessionKey();
		log.info("sessionKey: {}" + sessionKey);
		if (StringUtils.isBlank(sessionKey)) {
			throw new BusinessException(BusinessCodes.User.USER_NOT_LOGIN, "Please enter sessionkey");
		}
		BossUserSession session = SessionClientFactory.getSessionClient(generalPara.getClientType()).getSession(sessionKey);
		//checkRole(role, session.getRole());
		ServiceContext.initServiceContext(session);
	}

	private void checkRole(Role role, int roleId) {
		String[] roleCodes = role.code();
		String permissionsJson = jedisCluster.hget(RedisCodes.User.ROLE_HASH_KEY, "" + roleId);
		JSONArray permissions = JSON.parseArray(permissionsJson);
		for (String roleCode : roleCodes) {
			boolean hasPermissions = permissions.contains(roleCode);
			if (hasPermissions) {
				return;
			}
		}
		throw new BusinessException(BusinessCodes.User.NO_PERMISSION);
	}

	@Override
	public int getOrder() {
		return 20;
	}
}
