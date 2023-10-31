package com.gccws.common.service;
import com.gccws.base.service.BaseService;
import com.gccws.common.dto.SetupMasterDto;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import org.springframework.data.domain.Page;


import java.util.List;

/**
 * @Author    Rima
 * @Since     January 09, 2023
 * @version   1.0.0
 */

public interface SetupMasterService extends BaseService<SetupMasterDto, CommonDropdownModel, CommonPageableData> {

    List<CommonDropdownModel> getDropdownListByModuleId(int moduleId, int userId);
    Page<SetupMasterDto> getPageableListByModuleId(CommonPageableData commonPageableData, int userId);


}
