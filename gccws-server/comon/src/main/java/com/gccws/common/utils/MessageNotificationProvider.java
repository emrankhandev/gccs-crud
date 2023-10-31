package com.gccws.common.utils;

import com.gccws.common.dto.MessageHistoryDto;
import com.gccws.common.entity.NotificationDetails;
import com.gccws.common.repository.NotificationDetailsRepository;
import com.gccws.common.service.MessageHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2023
 * @version   1.0.0
 */

@Component
@AllArgsConstructor
public class MessageNotificationProvider {

    private final NotificationDetailsRepository notificationDetailsRepository;
    private final MessageHistoryService messageHistoryService;


    public void sendMessageNotification(
            Integer moduleId,
            Integer authorityId,
            String transactionType,
            String transactionTable,
            Integer transactionId,
            String message,
            String description,
            Date publishDate,
            String attachment,
            String attachmentLocation,
            boolean isAction,
            String link,
            int notificationTypeId,
            int userId){
        List<NotificationDetails> notificationDetailsList =	notificationDetailsRepository.findByMasterNotificationTypeId(notificationTypeId);
        for(NotificationDetails d : notificationDetailsList){
            MessageHistoryDto historyDto = new MessageHistoryDto();
            historyDto.setId(0);

            historyDto.setModuleId(moduleId); //MenuItem id
            historyDto.setAuthorityId(authorityId); //AppUser id

            historyDto.setTransactionId(transactionId);
            historyDto.setTransactionTable(transactionTable);
            historyDto.setTransactionType(transactionType);
            historyDto.setMessage(message);
            historyDto.setDescription(description);
            historyDto.setReceivedUserId(d.getAppUser().getId()); // AppUser id
            historyDto.setPublishDate(publishDate);

            //attachment
            historyDto.setAttachment(attachment);
            historyDto.setAttachmentLocation(attachmentLocation);

            //link
            historyDto.setIsAction(isAction);
            historyDto.setLink(link);

            historyDto.setIsClose(false);
            historyDto.setIsRead(false);

            messageHistoryService.save(historyDto, userId);
        }
    }

    public void sendMessageNotificationForSpecificUser(
            Integer moduleId,
            Integer authorityId,
            String transactionType,
            String transactionTable,
            Integer transactionId,
            String message,
            String description,
            Date publishDate,
            String attachment,
            String attachmentLocation,
            boolean isAction,
            String link,
            Integer receiveUserId,
            int userId){

        MessageHistoryDto historyDto = new MessageHistoryDto();
        historyDto.setId(0);

        historyDto.setModuleId(moduleId); //MenuItem id
        historyDto.setAuthorityId(authorityId); //AppUser id

        historyDto.setTransactionId(transactionId);
        historyDto.setTransactionTable(transactionTable);
        historyDto.setTransactionType(transactionType);
        historyDto.setMessage(message);
        historyDto.setDescription(description);
        historyDto.setReceivedUserId(receiveUserId); // AppUser id
        historyDto.setPublishDate(publishDate);

        //attachment
        historyDto.setAttachment(attachment);
        historyDto.setAttachmentLocation(attachmentLocation);

        //link
        historyDto.setIsAction(isAction);
        historyDto.setLink(link);

        historyDto.setIsClose(false);
        historyDto.setIsRead(false);

        messageHistoryService.save(historyDto, userId);
    }

}
