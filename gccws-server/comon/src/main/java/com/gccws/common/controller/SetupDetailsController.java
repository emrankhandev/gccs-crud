package com.gccws.common.controller;


import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.BaseConstants;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.dto.SetupDetailsDto;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.service.SetupDetailsService;
import com.gccws.common.utils.CommonUtils;
import com.gccws.common.utils.CommonConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * @Author    Md. Chabed Alam
 * @Since     September 26, 2022
 * @version   1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(CommonConstants.COMMON_END_POINT + "setup-details")
public class SetupDetailsController implements BaseController<BaseResponse, SetupDetailsDto, CommonPageableData, HttpServletRequest> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SetupDetailsController.class);

	private final SetupDetailsService service;

	/* utils */
	private final CommonUtils commonUtils;
	private final UserTokenRequestUtils tokenUtils;

	@Override
	@PostMapping
	public BaseResponse save(@Valid @RequestBody SetupDetailsDto body, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.save(body, tokenUtils.getUserIdFromRequest(request)), BaseConstants.SAVE_MESSAGE, BaseConstants.SAVE_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping
	public BaseResponse update(@Valid @RequestBody SetupDetailsDto body, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.update(body, tokenUtils.getUserIdFromRequest(request)), BaseConstants.UPDATE_MESSAGE, BaseConstants.UPDATE_MESSAGE_BN);
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@DeleteMapping
	public BaseResponse delete(@Valid @RequestBody SetupDetailsDto body,HttpServletRequest request){
		try {
			if(service.delete(body, tokenUtils.getUserIdFromRequest(request))) {
				return commonUtils.generateSuccessResponse(null, BaseConstants.DELETE_MESSAGE, BaseConstants.DELETE_MESSAGE_BN);
			}else {
				return commonUtils.generateSuccessResponse(null, BaseConstants.DELETE_MESSAGE_FAILED, BaseConstants.DELETE_MESSAGE_FAILED_BN);
			}
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping( value = BaseConstants.GET_OBJECT_BY_ID, produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
	public BaseResponse getById(@PathVariable(BaseConstants.OBJECT_ID) int id, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getById(id, tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping(value = BaseConstants.DROPDOWN_LIST_PATH, produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
	public BaseResponse getDropdownList(HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getDropdownList(tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping( value = BaseConstants.PAGEABLE_DATA_PATH, produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
	public BaseResponse getPageableListData(@Valid @RequestBody CommonPageableData pageableData, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getPageableListData(pageableData, tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping( value = "get-by-dev-code/{devCode}", produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
	public BaseResponse getByMasterId(@PathVariable("devCode") Integer devCode, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getByDevCodeAndActive(devCode, tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping( value = "get-by-dev-code-and-parent-id/{devCode}/{parentId}", produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
	public BaseResponse getByMasterId(@PathVariable("devCode") Integer devCode, @PathVariable("parentId") Integer parentId, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getByDevCodeAndParentId(devCode, parentId, tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "get-dropdown-list-by-moduleId/{moduleId}", produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
	public BaseResponse getDropdownListByModuleId(@PathVariable("moduleId") int moduleId, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getDropdownListByModuleId(moduleId, tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@PutMapping (value = "/pageable-by-moduleId", produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
	public BaseResponse getPageableListDataByModuleId(@Valid @RequestBody CommonPageableData pageableData, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getPageableListByModuleId(pageableData, tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

}
