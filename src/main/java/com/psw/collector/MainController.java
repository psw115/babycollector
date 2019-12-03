package com.psw.collector;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.psw.collector.firestore.FStoreManager;

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
	
	@RequestMapping("/testWrite")
	public String testWrite(HttpServletRequest request, HttpServletResponse response) {
		
		Enumeration<String> e = request.getParameterNames();
		
		String id = request.getParameter("id");
		if(id == null)
			return "id can not be null.";
		
		try {
			String key;
			Firestore fs = new FStoreManager().getFStore();
			Map<String, String> map = new HashMap<>();
			while(e.hasMoreElements()) {
				key = e.nextElement();
				map.put(key, request.getParameter(key));
			}
			
			DocumentReference docRef = fs.collection("test").document(id);
			ApiFuture<WriteResult> result = docRef.set(map);
			
			return "Write : " + map.size() +", Update time : " + result.get().getUpdateTime();
		}catch(Exception ex) {
			ex.printStackTrace();
			return "error : " + ex.getMessage();
		}
	}

}
