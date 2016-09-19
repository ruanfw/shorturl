package com.yunbei.shorturl.core.base.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunbei.shorturl.core.base.constant.SessionConfigs;
import com.yunbei.shorturl.core.base.dto.BaseResult;

public class BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	protected HttpSession session;

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	/**
	 * 获取ip地址
	 * 
	 * @return
	 */
	protected String getRequestIP() {
		String ip = (String) session.getAttribute(SessionConfigs.SESSION.LOGIN_IP);

		if (StringUtils.isBlank(ip)) {
			ip = getIpAddr();
			session.setAttribute(SessionConfigs.SESSION.LOGIN_IP, ip);
		}

		return ip;
	}

	// private String getIpAddr() {
	// String ip = request.getHeader("X-Real-IP");
	// if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
	// return ip;
	// }
	// ip = request.getHeader("X-Forwarded-For");
	// if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
	// // 多次反向代理后会有多个IP值，第一个为真实IP。
	// int index = ip.indexOf(',');
	// if (index != -1) {
	// return ip.substring(0, index);
	// } else {
	// return ip;
	// }
	// } else {
	// return request.getRemoteAddr();
	// }
	// }

	public String getIpAddr() {
		String ip = request.getHeader("x-forwarded-for");

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					LOG.error(e.getMessage(), e);
				}
				ip = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}

		return ip;
	}

	// @ExceptionHandler
	// public String exp(Exception ex) {
	// request.setAttribute("ex", ex);
	// return "短网址不合要求！或 数据调用失败返回为空！";
	// }

	protected BaseResult failedResult() {
		BaseResult baseResult = new BaseResult(false);
		return baseResult;
	}

	protected BaseResult failedResult(int code, String msg) {
		BaseResult baseResult = new BaseResult(false, code, msg, null);
		return baseResult;
	}

	protected BaseResult successResult() {
		BaseResult baseResult = new BaseResult(true);
		return baseResult;
	}

	protected BaseResult successResult(Object object) {
		BaseResult baseResult = new BaseResult(true, object);
		return baseResult;
	}

}
