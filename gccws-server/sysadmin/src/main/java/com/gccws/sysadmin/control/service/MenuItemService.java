package com.gccws.sysadmin.control.service;
import java.util.List;


import com.gccws.base.service.BaseService;
import com.gccws.common.entity.MenuItem;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.control.dto.MenuItemDto;

/**
 * @Author    Rima
 * @Since     July 10, 2023
 * @version   1.0.0
 */

public interface MenuItemService extends BaseService<MenuItemDto, CommonDropdownModel, CommonPageableData> {

    MenuItemDto getById(int id);

    MenuItemDto getByDevCode(int devCode);

    List<CommonDropdownModel> getDropdownListByMenuType(String menuType, int userId);

    List<MenuItemDto> getAuthorizedReportList(Integer userId, Integer moduleId);

    List<MenuItem> getPageByAppUserId(Integer appUserId, int userId);

    List<CommonDropdownModel> getModuleList(Integer userId);

    List<MenuItemDto> getReportModuleByAppUser(Integer appUserId, int userId);
    List<MenuItemDto> getReportByModuleIdAndAppUser(Integer moduleId, Integer appUserId, int userId);

}
