package com.yunbei.shorturl.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yunbei.shorturl.core.base.cache.RedisCache;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private RedisCache redisCache;

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String home(String name, Locale locale, Model model) {

        logger.info("Welcome home! The client locale is {}. name {}", locale, name);

        logger.warn(redisCache.ping());

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        return "home";
    }

}
