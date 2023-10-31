package com.gccws.common.controller;
import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.common.model.CommonPageableData;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.dto.MessageHistoryDto;
import com.gccws.common.service.MessageHistoryService;
import com.gccws.common.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import static com.gccws.common.utils.CommonConstants.*;

/**
 * @author  Md. Mizanur Rahman
 * @Email   kmmizanurrahmanjp@gmail.com
 * @since   March 14,2023
 * @version 1.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(COMMON_END_POINT + "message-history")
public class MessageHistoryController implements BaseController<BaseResponse, MessageHistoryDto, CommonPageableData, HttpServletRequest> {
	

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(MessageHistoryController.class);

	private final MessageHistoryService service;
    private final CommonUtils commonUtils;
    private final UserTokenRequestUtils authTokenUtils;



	@Override
	@PostMapping
	public BaseResponse save(@Valid @RequestBody MessageHistoryDto body, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.save(body, authTokenUtils.getUserIdFromRequest(request)), SMS_MESSAGE, SMS_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping
	public BaseResponse update(@Valid @RequestBody  MessageHistoryDto body, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.update(body, authTokenUtils.getUserIdFromRequest(request)), UPDATE_MESSAGE, UPDATE_MESSAGE_BN);
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@DeleteMapping
	public BaseResponse delete(@Valid @RequestBody MessageHistoryDto body, HttpServletRequest request) {
    	try {
    		if(service.delete(body, authTokenUtils.getUserIdFromRequest(request))) {
    			return commonUtils.generateSuccessResponse(null, DELETE_MESSAGE, DELETE_MESSAGE_BN);
    		}else {
    			return commonUtils.generateSuccessResponse(null, DELETE_MESSAGE_FAILED, DELETE_MESSAGE_FAILED_BN);
    		}
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping( value = GET_OBJECT_BY_ID, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getById(@PathVariable(OBJECT_ID) int id, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.getById(id, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override@GetMapping(value = DROPDOWN_LIST_PATH, produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getDropdownList(HttpServletRequest request){
    	try {
    		return commonUtils.generateSuccessResponse(service.getDropdownList(authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
    }

	@Override
	@PutMapping( value = PAGEABLE_DATA_PATH, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getPageableListData(@Valid @RequestBody CommonPageableData pageableData, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getPageableListData(pageableData, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping( value = "/get-message-list-by-user-id", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getById(HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getByUserId(authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping( value = "/transaction/{transactionId}/{transactionTable}", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getByTransactionIdAndTransactionTable(@PathVariable("transactionId") int transactionId, @PathVariable("transactionTable") String transactionTable) {
		try {
			return commonUtils.generateSuccessResponse(service.getByTransactionIdAndTransactionTable(transactionId, transactionTable));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}
    
}
