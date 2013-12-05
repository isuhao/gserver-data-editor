package com.gserver.data.editor.interceptor;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class LockTableInterceptor implements HandlerInterceptor {
	public static Cache<String, String> cache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(10).removalListener(new RemovalListener<String, String>() {
		public void onRemoval(RemovalNotification<String, String> arg) {
			System.out.println(arg.getKey() + "被移除");
		}
	}).build();

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String requestURI = request.getRequestURI();
		if (requestURI.contains("save") || requestURI.contains("update") || requestURI.contains("delete")) {
			String table = requestURI.split("\\/")[1];
			String addr = request.getRemoteAddr();
			String lockTableAddr = cache.getIfPresent(table);
			if (lockTableAddr != null && !addr.equals(lockTableAddr)) {
				String message = lockTableAddr + "将" + table + "锁定您无法进行修改数据的操作。";
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print("{\"success\":false,\"msg\":\"" + message + "\"}");
				System.out.println("requestURI:" + requestURI + " table:" + table + " remoteAddr:" + addr + "  lockTableAddr:" + lockTableAddr);
				return false;
			}
		}
		return true;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3) throws Exception {
	}

	public static boolean isLockTable(String table) {
		String ifPresent = cache.getIfPresent(table);
		if (StringUtils.isNotEmpty(ifPresent)) {
			return true;
		}
		return false;
	}

	public static String lockTableIP(String table) {
		return cache.getIfPresent(table);
	}

	public static void lockTable(String table, String remoteAddr) {
		cache.put(table, remoteAddr);
	}

	public static void unLockTable(String table) {
		cache.invalidate(table);
	}
}
