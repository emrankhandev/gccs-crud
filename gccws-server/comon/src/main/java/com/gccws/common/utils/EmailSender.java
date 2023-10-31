package com.gccws.common.utils;
import com.gccws.common.entity.EmailFactory;
import com.gccws.common.repository.EmailFactoryRepository;
import com.gccws.common.dto.EmailFactoryDto;

import lombok.AllArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @Author    Emran Khan
 * @Since     November 11, 2022
 * @version   1.0.0
 */
@Component
@AllArgsConstructor
public class EmailSender {
	
	private Environment env;
	private JavaMailSender javaMailSender;
	private final String ENV_FROM_MAIL = "send.from.email";
	private EmailFactoryRepository emailFactoryRepo;
	
	public void sendSalarySheetEmail(EmailFactoryDto dto, String fileDir) throws MessagingException {
		String mailFrom = env.getProperty(ENV_FROM_MAIL);
		System.err.println(mailFrom);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setFrom(mailFrom);
		mimeMessageHelper.setTo(dto.getRecipient());
		mimeMessageHelper.setSubject(dto.getSubject());
		mimeMessageHelper.setText(dto.getMsgBody());
		FileSystemResource fileSystemResource = new FileSystemResource(new File(fileDir));
		mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
		javaMailSender.send(mimeMessage);
		
		/* now save data in server */
		EmailFactory entity = new EmailFactory();
    	BeanUtils.copyProperties(dto, entity);
		emailFactoryRepo.save(entity);
	}

}
