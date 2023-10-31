package com.gccws.sysadmin.approve.controller;

import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.approve.service.ApprovalHistoryService;
import com.gccws.sysadmin.approve.dto.ApprovalHistoryDto;
import com.gccws.sysadmin.approve.utils.ApprovalConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

import static com.gccws.base.utils.BaseConstants.*;
import static com.gccws.common.utils.CommonConstants.SYSTEM_ADMIN_END_POINT;

/**
 * @Author    Rima
 * @Since     February 23, 20223
 * @version   1.0.0
 */
@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_ADMIN_END_POINT + "approval-history")
public class ApprovalHistoryController implements BaseController<BaseResponse, ApprovalHistoryDto, CommonPageableData, HttpServletRequest> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ApprovalHistoryController.class);
	private final ApprovalHistoryService service;
	/* utils */
	private final CommonUtils commonUtils;
	private final UserTokenRequestUtils authTokenUtils;

	@Override
	@PostMapping
	public BaseResponse save(@Valid @RequestBody ApprovalHistoryDto body, HttpServletRequest request) {
		try {
			String messageEn = "";
			String messageBn = "";
			if(body.getApprovalStatusId().equals(ApprovalConstants.SUBMIT)) {
				messageEn = ApprovalConstants.SUBMIT_MESSAGE_EN;
				messageBn = ApprovalConstants.SUBMIT_MESSAGE_BN;
			}else if(body.getApprovalStatusId().equals(ApprovalConstants.FORWARD)) {
				messageEn = ApprovalConstants.FORWARD_MESSAGE_EN;
				messageBn = ApprovalConstants.FORWARD_MESSAGE_BN;
			}else if(body.getApprovalStatusId().equals(ApprovalConstants.APPROVED)) {
				messageEn = ApprovalConstants.APPROVED_MESSAGE_EN;
				messageBn = ApprovalConstants.APPROVED_MESSAGE_BN;
			}else if(body.getApprovalStatusId().equals(ApprovalConstants.BACK)) {
				messageEn = ApprovalConstants.BACK_MESSAGE_EN;
				messageBn = ApprovalConstants.BACK_MESSAGE_BN;
			}else if(body.getApprovalStatusId().equals(ApprovalConstants.REJECT)) {
				messageEn = ApprovalConstants.REJECT_MESSAGE_EN;
				messageBn = ApprovalConstants.REJECT_MESSAGE_BN;
			}
			else {
				messageEn = SAVE_MESSAGE;
				messageBn = SAVE_MESSAGE_BN;
			}
			return commonUtils.generateSuccessResponse(service.save(body, authTokenUtils.getUserIdFromRequest(request)), messageEn, messageBn);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping
	public BaseResponse update(@Valid @RequestBody ApprovalHistoryDto body, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.update(body, authTokenUtils.getUserIdFromRequest(request)), UPDATE_MESSAGE, UPDATE_MESSAGE_BN);
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@DeleteMapping
	public BaseResponse delete(@Valid @RequestBody ApprovalHistoryDto body, HttpServletRequest request) {
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
	@GetMapping(value = GET_OBJECT_BY_ID, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getById(@PathVariable(OBJECT_ID) int id,HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getById(id, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping(value = DROPDOWN_LIST_PATH, produces = EXTERNAL_MEDIA_TYPE)
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

	@GetMapping(value = "get-notification-list-by-user-id")
	public BaseResponse getNotificationListByNotifyUserId(HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getNotificationListByNotifyUserId(authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "get-pending-list-by-user-id/{userId}/{fromDate}/{toDate}/{transactionTypeId}")
	public BaseResponse getApprovalPendingList(@PathVariable("userId") int userId,
											   @PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
											   @PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
											   @PathVariable("transactionTypeId") int transactionTypeId){
		try {
			return commonUtils.generateSuccessResponse(service.getApprovalPendingList(userId, fromDate, toDate, transactionTypeId));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "get-by-approval-history-id/{approvalHistoryId}")
	public BaseResponse getByApprovalHistoryId(@PathVariable("approvalHistoryId") int approvalHistoryId, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getByApprovalHistoryId(approvalHistoryId, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}


	@GetMapping( value = "/{userId}/{formDate}/{toDate}", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getSearchByMerchantId(@PathVariable("userId")int userId,
											  @PathVariable("formDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date formDate,
											  @PathVariable("toDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
											  HttpServletRequest request){
		try {
			formDate = formDate.equals(commonUtils.getCompareDate()) ? null : formDate;
			toDate   = toDate.equals(commonUtils.getCompareDate()) ? null : toDate;
			return commonUtils.generateSuccessResponse(service.getApprovalPendingListByDate(userId, formDate, toDate));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}


}
