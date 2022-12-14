package com.hero.herolanding.service;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
@RequiredArgsConstructor
public class SendMessageService {

	public void certifiedPhoneNumber(String userPhoneNumber, int randomNumber) {
		String api_key = "NCSEEQZNKCK9AI0P";							// coolsms에서 받은 api_key, api_secret
	    String api_secret = "LTY4P6GCF2CVPMQXXFLYQLQNEAEYD2KB";		
	    Message coolsms = new Message(api_key, api_secret);

	    // 4 params(to, from, type, text) are mandatory. must be filled
	    HashMap<String, String> params = new HashMap<String, String>();
	    params.put("to", userPhoneNumber);    // 수신전화번호
	    params.put("from", "01051176974");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
	    params.put("type", "SMS");
	    params.put("text", "[TEST] 인증번호는" + "["+randomNumber+"]" + "입니다."); // 문자 내용 입력
	    params.put("app_version", "test app 1.2"); // application name and version

	    System.out.println("샌드서비스 들어옴");
	    System.out.println("문자메세지 : " + params.get("text"));
	    System.out.println("userPhoneNumber : "+userPhoneNumber);
	    
	    
	    try {
	        JSONObject obj = (JSONObject) coolsms.send(params);
	        System.out.println(obj.toString());
	      } catch (CoolsmsException e) {
	        System.out.println(e.getMessage());
	        System.out.println(e.getCode());
	      }   
	}
}
