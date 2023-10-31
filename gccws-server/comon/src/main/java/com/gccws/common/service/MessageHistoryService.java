package com.gccws.common.service;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.base.service.BaseService;
import com.gccws.common.dto.MessageHistoryDto;

import java.util.List;

/**
 * @author  Md. Mizanur Rahman
 * @Email   kmmizanurrahmanjp@gmail.com
 * @since   March 14,2023
 * @version 1.0
 */

public interface MessageHistoryService extends BaseService<MessageHistoryDto, CommonDropdownModel, CommonPageableData> {

    List<MessageHistoryDto> getByTransactionIdAndTransactionTable(Integer transactionId, String transactionTable);
    List<MessageHistoryDto> getByUserId(Integer userId);

}

