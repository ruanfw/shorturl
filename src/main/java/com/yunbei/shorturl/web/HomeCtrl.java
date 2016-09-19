package com.yunbei.shorturl.web;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunbei.shorturl.core.base.controller.BaseController;

@Controller
public class HomeCtrl extends BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(HomeCtrl.class);

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String home(String name, Locale locale, Model model) {
		return "短网址不合要求！或数据调用失败返回为空！";
	}

}
