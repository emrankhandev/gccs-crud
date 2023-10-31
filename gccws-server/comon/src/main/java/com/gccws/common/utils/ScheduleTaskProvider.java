package com.gccws.common.utils;

import com.gccws.common.repository.SMSFactoryRepository;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor
public class ScheduleTaskProvider{
	
	/* schedule property */
	private static final int DELAY_SECOND = 30;
	
	/* sms property */
	private final SMSFactoryRepository smsFactoryRepo;
	private final SmsSender smsSender;
	
	
	/*@Scheduled(fixedDelay = DELAY_SECOND * 1000)
	public void scheduleTaskWithFixedDelay() {
		List<SmsFactory> smsList =  smsFactoryRepo.findUnSendMessage();
		for (SmsFactory sms : smsList) {
			smsSender.sendSms(sms);
		}
	}*/
	
	
	
	
	
	
}
