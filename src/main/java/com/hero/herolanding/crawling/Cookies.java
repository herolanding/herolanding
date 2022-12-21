package com.hero.herolanding.crawling;

import java.time.LocalTime;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class Cookies {
	public void setCookie(HttpServletResponse response ,HttpServletRequest request) {
		Cookie cookie = new Cookie("crawring" , "있따");
		LocalTime now = LocalTime.now();
		int hour = 23 - now.getHour();
        int minute = 59 - now.getMinute();
        int second = 59 - now.getSecond();
        int total = hour*minute*60*second;
		cookie.setMaxAge(total);
		response.addCookie(cookie); // response에 Cookie 추가
			
	}

}