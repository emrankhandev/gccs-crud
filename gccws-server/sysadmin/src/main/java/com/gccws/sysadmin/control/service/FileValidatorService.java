package com.gccws.sysadmin.control.service;


import com.gccws.base.service.BaseService;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.control.dto.FileValidatorDto;

import java.util.List;

/**
 * @Author    Md. Chabed Alam
 * @Since     September 26, 2022
 * @version   1.0.0
 */

public interface FileValidatorService extends BaseService<FileValidatorDto , CommonDropdownModel, CommonPageableData> {

    FileValidatorDto save(FileValidatorDto obj);
    FileValidatorDto update(FileValidatorDto obj);
    Boolean delete(FileValidatorDto obj);
    FileValidatorDto getById(int id);
    List<FileValidatorDto> getAll();
    List<FileValidatorDto> getAllActive();

    FileValidatorDto getByDevCode(Integer devCode, int userId);

    /*for public api*/
    FileValidatorDto getByDevCode(Integer devCode);

}
