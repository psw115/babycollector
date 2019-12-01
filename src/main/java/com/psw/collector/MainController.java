package com.psw.collector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	private String localIp = "";
	
	@RequestMapping("/")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("ID");
		if("local".equals(id)) {
			localIp = request.getRemoteAddr();
		}
		
		return "index pages...your ip = " + request.getRemoteAddr() +", misc = " + ("local".equals(id) ? localIp : "");
	}

}
