package com.gccws.common.utils;

import java.util.List;

import com.gccws.common.controller.WelcomeController;
import com.gccws.common.entity.SmsFactory;
import com.gccws.common.repository.SMSFactoryRepository;
import com.gccws.common.model.SmsResponse;
import com.gccws.common.model.SmsResponseCollection;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;


/**
 * @Author    Md. Chabed Alam
 * @Since     September 28, 2022
 * @version   1.0.0
 */

@Component
@AllArgsConstructor
public class SmsSender {
	
	private static final Logger LOG = LoggerFactory.getLogger(WelcomeController.class);
	
	private final String SMS_URL = "https://api.mobireach.com.bd/SendTextMessage?";
	private final String USER_NAME = "addiemora";
	private final String PASSWORD = "@dd!eMORA123";
	private final String FROM = "MoRAProject";
	
	private final SMSFactoryRepository smsFactoryRepo;
	
	
	public void sendSms(SmsFactory smsFactory) {
		try {
			CloseableHttpClient httpClient = HttpClients.custom()
	                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
	                .build();
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
			
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			SmsResponseCollection result = restTemplate.getForObject(
					SMS_URL + 
					"Username=" + USER_NAME + 
					"&Password=" + PASSWORD + 
					"&From=" + FROM +
					"&To=" + smsFactory.getMobileNo() +
					"&Message=" + smsFactory.getSmsText(), 
					SmsResponseCollection.class);
	    	
	    	List<SmsResponse> smsResponses =  result.getReserveInfoList();
	    	if (smsResponses.size() > 0) {
	    		SmsResponse smsResponse = smsResponses.get(0);
		    	BeanUtils.copyProperties(smsResponse, smsFactory);
		    	System.out.println(smsFactory);
		    	smsFactoryRepo.save(smsFactory);
	    	}
			
		}catch (Exception e) {
			e.printStackTrace();
    		LOG.error(e.getMessage());
    	} 
	}
	

}
