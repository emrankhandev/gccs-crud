package com.gccws.sysadmin.control.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.gccws.common.entity.MenuItem;
import com.gccws.common.entity.ReportUpload;
import com.gccws.common.entity.UserRoleAssignDetails;
import com.gccws.common.entity.UserRoleDetails;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.repository.UserRoleAssignDetailsRepository;
import com.gccws.common.repository.UserRoleDetailsRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.repository.MenuItemRepository;
import com.gccws.sysadmin.control.dto.MenuItemDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;

/**
 * @Author    Rima
 * @Since     July 10, 2023
 * @version   1.0.0
 */

@Service
@AllArgsConstructor
public class MenuItemServiceImpl implements MenuItemService{

	private ModelMapper modelMapper;
	private final MenuItemRepository repo;
	private final UserRoleAssignDetailsRepository roleAssignDetailsRepository;
	private final UserRoleDetailsRepository roleDetailsRepository;
	private final CommonUtils commonUtils;

	/*exta code*/
	private final String ENTITY_NAME = MenuItem.class.getSimpleName();

	@Transactional
	@Override
	public MenuItemDto save(MenuItemDto obj, int userId) {
		MenuItem savedEntity = repo.save(generateEntity(obj, userId, true));
		commonUtils.auditLoggingForSave(userId, ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public MenuItemDto update(MenuItemDto obj, int userId) {
		MenuItemDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
		MenuItem savedEntity = repo.save(generateEntity(obj,userId, false));
		commonUtils.auditLoggingForUpdate(userId, ENTITY_NAME, generateDto(savedEntity), oldAuditDto);
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public Boolean delete(MenuItemDto obj, int userId) {
		MenuItemDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
		if (!ObjectUtils.isEmpty(obj.getId())){
			MenuItem entity = new MenuItem();
			entity.setId(obj.getId());
			repo.delete(entity);
			commonUtils.auditLoggingForDelete(userId, ENTITY_NAME, deleteAuditDto);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public MenuItemDto getById(int id, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<MenuItem> dataList = repo.findById(id);
		if(dataList.isEmpty()) {
			return null;
		}else {
			return generateDto(dataList.get());
		}
	}

	@Override
	public MenuItemDto getByDevCode(int devCode) {
		return generateDto(repo.findByDevCode(devCode));
	}

	@Override
	public List<CommonDropdownModel> getDropdownList(int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findDropdownModel();
	}


	@Override
	public List<CommonDropdownModel> getDropdownListByMenuType(String menuTypeString, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		String [] menuTypes = menuTypeString.split(",");
		List<Integer> menuTypeList = new ArrayList<>();
		for(String menuType: menuTypes){
			menuTypeList.add(Integer.parseInt(menuType));
		}
		return repo.findDropdownModelByMenuType(menuTypeList);
	}

	@Override
	public List<MenuItemDto> getAuthorizedReportList(Integer userId, Integer moduleId) {
		List<MenuItem> temp = new ArrayList<>();

		List<UserRoleAssignDetails> roleAssignDetailList = roleAssignDetailsRepository.findByMasterAppUserId(userId);
		for(UserRoleAssignDetails rad : roleAssignDetailList){

			List<UserRoleDetails> roleDetailList = roleDetailsRepository.findByMasterIdAndMenuItemMenuTypeAndMenuItemParentIdOrderByMenuItemSerialNoAsc(rad.getUserRole().getId(), 4, moduleId);
			for(UserRoleDetails rd : roleDetailList){

				temp.add(rd.getMenuItem());
			}
		}
		return convertEntityListToDtoList(temp.stream());
	}

	@Override
	public Page<MenuItemDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<MenuItem> pageresult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<MenuItemDto> objlist = convertEntityListToDtoList(pageresult.stream());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}


	//public api
	@Override
	public MenuItemDto getById(int id) {
		Optional<MenuItem> dataList = repo.findById(id);
		if(dataList.isEmpty()) {
			return null;
		}else {
			return generateDto(dataList.get());
		}
	}

	@Override
	public List<MenuItem> getPageByAppUserId(Integer appUserId, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findPageByAppUserId(appUserId);
	}

	@Override
	public List<CommonDropdownModel> getModuleList(Integer userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findModuleDropdownModel();
	}

	@Override
	public List<MenuItemDto> getReportModuleByAppUser(Integer appUserId, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		return convertEntityListToDtoList(repo.findReportModuleByAppUser(appUserId).stream());
	}

	@Override
	public List<MenuItemDto> getReportByModuleIdAndAppUser(Integer moduleId, Integer appUserId, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		return convertEntityListToDtoList(repo.findReportByModuleIdAndAppUser(moduleId, appUserId).stream());
	}

	//..................... Generate Model....................//

	private MenuItem generateEntity(MenuItemDto dto, int userId, Boolean isSaved) {
		MenuItem entity = new MenuItem();
		BeanUtils.copyProperties(dto, entity);
		if(isSaved) {
			entity.setEntryUser(userId);
			commonUtils.setEntryUserInfo(entity);
		}else {
			MenuItem dbEntity = repo.findById(dto.getId()).get();
			entity.setUpdateUser(userId);
			commonUtils.setUpdateUserInfo(entity, dbEntity);
		}
		if(!ObjectUtils.isEmpty(dto.getParentId())) {
			MenuItem parent = new MenuItem();
			parent.setId(dto.getParentId());
			entity.setParent(parent);
		}
		//        for report dropdown
		if(!ObjectUtils.isEmpty(dto.getReportUploadId())) {
			ReportUpload reportUpload = new ReportUpload();
			reportUpload.setId(dto.getReportUploadId());
			entity.setReportUpload(reportUpload);
		}
		return entity;
	}

	private List<MenuItemDto> convertEntityListToDtoList(Stream<MenuItem> entityList) {
		return entityList.map(entity -> {
			return generateDto(entity);
		}).collect(Collectors.toList());
	}

	private MenuItemDto generateDto(MenuItem entity) {
		MenuItemDto dto = modelMapper.map(entity, MenuItemDto.class);
		if(!ObjectUtils.isEmpty(entity.getParent())) {
			dto.setParentId(entity.getParent().getId());
			dto.setParentName(entity.getParent().getName());

		}
		if(!ObjectUtils.isEmpty(entity.getReportUpload())) {
			dto.setReportUploadId(entity.getReportUpload().getId());
			dto.setReportUploadCode(entity.getReportUpload().getCode());
			dto.setReportUploadName(entity.getReportUpload().getFileName());
			dto.setReportUploadNameJasper(entity.getReportUpload().getFileNameJasper());
			dto.setReportUploadNameParams(entity.getReportUpload().getFileNameParams());
			dto.setReportUploadIsSubreport(entity.getReportUpload().getIsSubreport());
		}
		return dto;
	}



}
