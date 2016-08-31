package com.yunbei.shorturl.core.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunbei.shorturl.core.base.constant.SessionConfigs;
import com.yunbei.shorturl.core.base.dto.BaseResult;
import com.yunbei.shorturl.core.base.utils.NetworkUtil;

/**
 * @author: mlc
 * @dat: 2016年6月25日
 * @Description: TODO
 */

public class BaseController {

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
			ip = NetworkUtil.getRemoteIPForNginx(request);
			session.setAttribute(SessionConfigs.SESSION.LOGIN_IP, ip);
		}
		return ip;
	}

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
