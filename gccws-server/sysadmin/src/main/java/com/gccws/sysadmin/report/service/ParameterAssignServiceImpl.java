package com.gccws.sysadmin.report.service;

import com.gccws.common.entity.MenuItem;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.report.repository.ParameterAssignDetailRepository;
import com.gccws.sysadmin.report.repository.ParameterAssignMasterRepository;
import com.gccws.sysadmin.report.dto.ParameterAssignDetailDto;
import com.gccws.sysadmin.report.dto.ParameterAssignMasterDto;
import com.gccws.sysadmin.report.entity.ParameterAssignDetail;
import com.gccws.sysadmin.report.entity.ParameterAssignMaster;
import com.gccws.sysadmin.report.entity.ParameterMaster;
import com.gccws.sysadmin.report.model.ParameterAssignModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */

@Service
@AllArgsConstructor
public class ParameterAssignServiceImpl implements ParameterAssignService{

    private ModelMapper modelMapper;
    private final ParameterAssignMasterRepository masterRepo;
    private final ParameterAssignDetailRepository detailsRepo;
    private final CommonUtils commonUtils;

    /*extra code*/
    private final String ENTITY_NAME = ParameterAssignModel.class.getSimpleName();

    @Transactional
    @Override
    public ParameterAssignModel save(ParameterAssignModel obj, int userId) {
        ParameterAssignMasterDto master = obj.getMaster();
        List<ParameterAssignDetailDto> detailsList = obj.getDetailsList();

        /*save master*/
        ParameterAssignMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, true));

        /*save details*/
        if(!ObjectUtils.isEmpty(masterEntity)){
            List<ParameterAssignDetail> listForSave = new ArrayList<>();
            for(ParameterAssignDetailDto detail: detailsList){
                listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
            detailsRepo.saveAll(listForSave);
        }

        // for audit log start
        List<ParameterAssignMaster> masterList = new ArrayList<>();
        masterList.add(masterEntity);
        ParameterAssignModel saveAuditModel = convertMasterToDetails(masterList).get(0);
        commonUtils.auditLoggingForSave(userId, ENTITY_NAME, saveAuditModel);
        // for audit log end

        /* create model */
        obj.setMaster(generateMasterDto(masterEntity));
        return obj;
    }

    @Transactional
    @Override
    public ParameterAssignModel update(ParameterAssignModel obj, int userId) {
        // for audit log start
        ParameterAssignMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
        List<ParameterAssignMaster> oldMasterList = new ArrayList<>();
        oldMasterList.add(oldEntity);
        ParameterAssignModel oldAuditModel = convertMasterToDetails(oldMasterList).get(0);
        // for audit log end

        ParameterAssignMasterDto master = obj.getMaster();
        List<ParameterAssignDetailDto> detailsList = obj.getDetailsList();

        /*save master*/
        ParameterAssignMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, false));

        /*save details*/
        if(!ObjectUtils.isEmpty(master)){

            /* delete previous data if needed */
            deleteDetailsData(masterEntity, detailsList);

            /* now save data*/
            List<ParameterAssignDetail> listForSave = new ArrayList<>();
            for(ParameterAssignDetailDto detail: detailsList){
                listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
            detailsRepo.saveAll(listForSave);
        }

        // for audit log start
        List<ParameterAssignMaster> newMasterList = new ArrayList<>();
        newMasterList.add(masterEntity);
        ParameterAssignModel newAuditModel = convertMasterToDetails(newMasterList).get(0);
        commonUtils.auditLoggingForUpdate(userId, ENTITY_NAME, newAuditModel, oldAuditModel);
        // for audit log end

        /* create model */
        obj.setMaster(generateMasterDto(masterEntity));
        return obj;
    }

    @Transactional
    @Override
    public Boolean delete(ParameterAssignModel obj, int userId) {
        // for audit log start
        ParameterAssignMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
        List<ParameterAssignMaster> oldMasterList = new ArrayList<>();
        oldMasterList.add(oldEntity);
        ParameterAssignModel deleteAuditModel = convertMasterToDetails(oldMasterList).get(0);
        // for audit log end
        if(!ObjectUtils.isEmpty(obj.getMaster().getId())) {
            ParameterAssignMaster entity = new ParameterAssignMaster();
            entity.setId(obj.getMaster().getId());
            masterRepo.delete(entity);
            commonUtils.auditLoggingForDelete(userId,  ENTITY_NAME, deleteAuditModel);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public ParameterAssignModel getById(int id, int userId) {
        commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
        Optional<ParameterAssignMaster> dataList = masterRepo.findById(id);
        if(dataList.isEmpty()) {
            return null;
        }else {
            List<ParameterAssignMaster> masterList = new ArrayList<>();
            masterList.add(dataList.get());
            return convertMasterToDetails(masterRepo.findAll()).get(0);
        }
    }

    @Override
    public List<ParameterAssignModel> getByMenuItemId(int id) {
        return convertMasterToDetails(masterRepo.findByMenuItemId(id));
    }

    @Override
    public List<ParameterAssignModel> getByMenuItemDevCode(int devCode) {
        return convertMasterToDetails(masterRepo.findByMenuItemDevCode(devCode));
    }

    @Override
    public List<CommonDropdownModel> getDropdownList(int userId) {
        commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
        return masterRepo.findDropdownModel();
    }

    @Override
    public Page<ParameterAssignModel> getPageableListData(CommonPageableData pageableData, int userId) {
        commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
        PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
        Page<ParameterAssignMaster> pageresult = masterRepo.searchPageableList(pageableData.getSearchValue(), pageRequest);
        List<ParameterAssignModel> objlist = convertMasterToDetails(pageresult.getContent());
        return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
    }

    //..................... Generate Model....................//

    /* master part */
    private ParameterAssignMaster generateMasterEntity(ParameterAssignMasterDto dto, int userId, Boolean isSaved) {
        ParameterAssignMaster entity = new ParameterAssignMaster();
        BeanUtils.copyProperties(dto, entity);
        if(isSaved) {
            entity.setEntryUser(userId);
            commonUtils.setEntryUserInfo(entity);
        }else {
            ParameterAssignMaster dbEntity = masterRepo.findById(dto.getId()).get();
            entity.setUpdateUser(userId);
            commonUtils.setUpdateUserInfo(entity, dbEntity);
        }
        if(!ObjectUtils.isEmpty(dto.getMenuItemId())) {
            MenuItem menuItem = new MenuItem();
            menuItem.setId(dto.getMenuItemId());
            entity.setMenuItem(menuItem);
        }
        return entity;
    }

    private ParameterAssignMasterDto generateMasterDto(ParameterAssignMaster entity) {
        ParameterAssignMasterDto dto = modelMapper.map(entity, ParameterAssignMasterDto.class);
        if(!ObjectUtils.isEmpty(entity.getMenuItem())) {
            dto.setMenuItemId(entity.getMenuItem().getId());
            dto.setMenuItemName(entity.getMenuItem().getName());
        }
        return dto;
    }

    /* details part */
    private ParameterAssignDetail generateDetailsEntity(ParameterAssignDetailDto dto, ParameterAssignMaster masterEntity, int userId) {
        ParameterAssignDetail entity = new ParameterAssignDetail();
        BeanUtils.copyProperties(dto, entity);
        entity.setParameterAssignMaster(masterEntity);
        entity.setEntryUser(userId);
        commonUtils.setEntryUserInfo(entity);
        if(!ObjectUtils.isEmpty(dto.getParameterMasterId())) {
            ParameterMaster parameterMaster = new ParameterMaster();
            parameterMaster.setId(dto.getParameterMasterId());
            entity.setParameterMaster(parameterMaster);
        }
        return entity;
    }

    private List<ParameterAssignDetailDto> convertDetailsEntityListToDtoList(Stream<ParameterAssignDetail> entityList) {
        return entityList.map(entity -> {
            return generateDetailsDto(entity);
        }).collect(Collectors.toList());
    }

    private ParameterAssignDetailDto generateDetailsDto(ParameterAssignDetail entity) {
        ParameterAssignDetailDto dto = modelMapper.map(entity, ParameterAssignDetailDto.class);
        if(!ObjectUtils.isEmpty(entity.getParameterMaster())) {
            dto.setParameterMasterId(entity.getParameterMaster().getId());
            dto.setParameterMasterTitle(entity.getParameterMaster().getTitle());
            dto.setParameterMasterName(entity.getParameterMaster().getName());
            dto.setParameterMasterDataType(entity.getParameterMaster().getDataType());
            dto.setParameterMasterSql(entity.getParameterMaster().getSql());

            if(!ObjectUtils.isEmpty(entity.getParameterMaster().getChild())){
                dto.setParameterMasterChildId(entity.getParameterMaster().getChild().getId());
                dto.setParameterMasterChildName(entity.getParameterMaster().getChild().getName());
            }
        }
        return dto;
    }

    //..................... Helper ....................//

    private void deleteDetailsData(ParameterAssignMaster savedEntity, List<ParameterAssignDetailDto> details) {
        List<ParameterAssignDetail> listForCheckDelete = detailsRepo.findByParameterAssignMasterIdOrderBySerialNoAsc(savedEntity.getId()); // 1,2,3
        List<ParameterAssignDetail> listForDelete = new ArrayList<>();
        for(ParameterAssignDetail obj: listForCheckDelete) {
            boolean needToDelete = true;
            for(ParameterAssignDetailDto detailsEntity: details) { // 1,3
                if(obj.getId().equals(detailsEntity.getId())) {
                    needToDelete = false;
                    break;
                }
            }
            if (needToDelete) {
                listForDelete.add(obj);
            }
        }
        detailsRepo.deleteAll(listForDelete);
    }

    private List<ParameterAssignModel> convertMasterToDetails(List<ParameterAssignMaster> list) {
        List<ParameterAssignModel> returnList = new ArrayList<>();
        for (ParameterAssignMaster master: list) {
            ParameterAssignModel tmp = new ParameterAssignModel();
            /*set master*/
            tmp.setMaster(generateMasterDto(master));
            /*set details*/
            tmp.setDetailsList(convertDetailsEntityListToDtoList(detailsRepo.findByParameterAssignMasterIdOrderBySerialNoAsc(master.getId()).stream()));
            returnList.add(tmp);
        }
        return  returnList;
    }
}
