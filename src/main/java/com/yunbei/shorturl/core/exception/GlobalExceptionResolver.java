package com.yunbei.shorturl.core.exception;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Override
	@ResponseBody
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		LOG.error("访问" + request.getRequestURI() + " 发生错误, 错误信息:" + ex.getMessage());
		// 这里有2种选择
		// 跳转到定制化的错误页面
		/*
		 * ModelAndView error = new ModelAndView("error");
		 * error.addObject("exMsg", ex.getMessage()); error.addObject("exType",
		 * ex.getClass().getSimpleName().replace("\"", "'"));
		 */
		// 返回json格式的错误信息
		try {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			// BaseResult result = new BaseResult(false, "短网址不合要求！或
			// 数据调用失败返回为空！");
			writer.write("短网址不合要求！或数据调用失败返回为空！");
			writer.flush();
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
		}
		return new ModelAndView();
	}

}
