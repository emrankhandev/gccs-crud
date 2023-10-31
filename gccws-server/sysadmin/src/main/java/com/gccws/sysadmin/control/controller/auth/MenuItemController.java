package com.gccws.sysadmin.control.controller.auth;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.MenuItemDto;
import com.gccws.sysadmin.control.service.MenuItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import static com.gccws.base.utils.BaseConstants.*;
import static com.gccws.common.utils.CommonConstants.SYSTEM_ADMIN_END_POINT;

/**
 * @Author    Rima
 * @Since     July 10, 2023
 * @version   1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_ADMIN_END_POINT + "menu-item")
public class MenuItemController implements BaseController<BaseResponse, MenuItemDto, CommonPageableData, HttpServletRequest> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(MenuItemController.class);

	/*utils*/
	private final CommonUtils commonUtils;

	/*service*/
	private final MenuItemService service;
	private final UserTokenRequestUtils tokenUtils;


	@Override
	@PostMapping
	public BaseResponse save(@Valid @RequestBody MenuItemDto body, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.save(body, tokenUtils.getUserIdFromRequest(request)), SAVE_MESSAGE, SAVE_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping
	public BaseResponse update(@Valid @RequestBody MenuItemDto body, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.update(body, tokenUtils.getUserIdFromRequest(request)), UPDATE_MESSAGE, UPDATE_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@DeleteMapping
	public BaseResponse delete(@Valid @RequestBody MenuItemDto body, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.delete(body,tokenUtils.getUserIdFromRequest(request)), DELETE_MESSAGE, DELETE_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping( value = GET_OBJECT_BY_ID, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getById(@PathVariable(OBJECT_ID) int id, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getById(id,tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping(value = DROPDOWN_LIST_PATH, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getDropdownList(HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getDropdownList(tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping( value = PAGEABLE_DATA_PATH, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getPageableListData(@Valid @RequestBody CommonPageableData pageableData, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getPageableListData(pageableData, tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "dropdown-list-by-menu-type/{menuType}", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getDropdownListByMenuType(@PathVariable("menuType") String menuType, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getDropdownListByMenuType(menuType, tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "page-by-app-user-id", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getPageByAppUserId(HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getPageByAppUserId(tokenUtils.getUserIdFromRequest(request), tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "get-module-list")
	public BaseResponse getModuleDropdownList(HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getModuleList(tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "report-module-by-appUser", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getReportModuleByAppUser(HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getReportModuleByAppUser(tokenUtils.getUserIdFromRequest(request), tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "report-by-moduleId-and-appUser/{moduleId}", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getReportByModuleIdAndAppUser(@PathVariable("moduleId") Integer moduleId, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getReportByModuleIdAndAppUser(moduleId, tokenUtils.getUserIdFromRequest(request), tokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}


	@GetMapping( value = "/authorized-report/{moduleId}", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getAuthorizedReportList(@PathVariable("moduleId") int moduleId, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getAuthorizedReportList(tokenUtils.getUserIdFromRequest(request),  moduleId));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}


}
