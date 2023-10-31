package com.gccws.sysadmin.approve.service;

import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.ApprovalHistory;
import com.gccws.common.entity.ApprovalTeamMaster;
import com.gccws.common.entity.SetupDetails;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.repository.AppUserRepository;
import com.gccws.common.repository.ApprovalHistoryRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.approve.dto.ApprovalHistoryDto;
import com.gccws.sysadmin.approve.repository.ApprovalConfigurationRepository;
import com.gccws.sysadmin.approve.utils.ApprovalConstants;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author    Rima
 * @Since     February 23, 20223
 * @version   1.0.0
 */
@Service
@AllArgsConstructor
public class ApprovalHistoryServiceImpl implements ApprovalHistoryService{

    private ModelMapper modelMapper;

    private final AppUserRepository appUserRepo;

    private final ApprovalHistoryRepository repo;
    private final ApprovalConfigurationRepository approvalConfigRepo;

    private final CommonUtils commonUtils;

    /*extra code*/
    private final String ENTITY_NAME = ApprovalHistory.class.getSimpleName();



    @Transactional
    @Override
    public ApprovalHistoryDto save(ApprovalHistoryDto obj, int userId) {
        /*first close previous entry*/
        if(!ObjectUtils.isEmpty(obj.getId())){
            ApprovalHistory previousHistory = repo.findById(obj.getId()).get();
            previousHistory.setIsClose(true);
            repo.save(previousHistory);
            /*now clear new obj*/
            obj.setId(null);
            obj.setIsSeen(false);
            obj.setIsCross(false);
            obj.setSeenDate(null);
        }
        /*save history data*/
        ApprovalHistory savedEntity = repo.save(generateEntity(obj, userId, true));

        /*modify transaction table*/
        modifyTransactionTable(savedEntity);

        /*on approve notify other user*/
        if(savedEntity.getApprovalStatusId().equals(ApprovalConstants.APPROVED) || savedEntity.getApprovalStatusId().equals(ApprovalConstants.REJECT)){
            onApproveNotifyOtherUser(savedEntity);
        }

        /*audit log*/
        commonUtils.auditLoggingForSave(userId, ENTITY_NAME,generateDto(savedEntity));
        return generateDto(savedEntity);
    }


    @Transactional
    @Override
    public ApprovalHistoryDto update(ApprovalHistoryDto obj, int userId) {
        ApprovalHistoryDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
        obj.setSeenDate(new Date());
        ApprovalHistory savedEntity = repo.save(generateEntity(obj,userId, false));
        commonUtils.auditLoggingForUpdate(userId,  ENTITY_NAME,generateDto(savedEntity), oldAuditDto);
        return generateDto(savedEntity);
    }

    @Transactional
    @Override
    public Boolean delete(ApprovalHistoryDto obj, int userId) {
        ApprovalHistoryDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
        if(!ObjectUtils.isEmpty(obj.getId())) {
            ApprovalHistory entity = new ApprovalHistory();
            entity.setId(obj.getId());
            repo.delete(entity);
            commonUtils.auditLoggingForDelete(userId,  ENTITY_NAME, deleteAuditDto);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public ApprovalHistoryDto getById(int id, int userId) {
        commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
        Optional<ApprovalHistory> dataList = repo.findById(id);
        if(dataList.isEmpty()) {
            return null;
        }else {
            return generateDto(dataList.get());
        }
    }

    @Override
    public List<CommonDropdownModel> getDropdownList(int userId) {
        commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
        return repo.findDropdownModel();
    }

    @Override
    public List<ApprovalHistoryDto> getNotificationListByNotifyUserId(Integer userId) {
        commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
        return convertEntityListToDtoList(repo.findNotificationListByNotifyUserId(userId).stream());
    }

    @Override
    public List<ApprovalHistoryDto> getApprovalPendingList(Integer userId, Date fromDate, Date toDate, Integer transactionTypeId) {
        commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
        return convertEntityListToDtoList(repo.findApprovalPendingList(userId, fromDate, toDate, transactionTypeId).stream());
    }

    @Override
    public List<ApprovalHistoryDto> getByApprovalHistoryId(Integer approvalHistoryId, Integer userId) {
        commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
        return convertEntityListToDtoList(repo.findByApprovalHistoryId(approvalHistoryId).stream());
    }

    @Override
    public List<ApprovalHistoryDto> getApprovalPendingListByDate(int userId, Date formDate, Date toDate) {
        commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
        return convertEntityListToDtoList(repo.findApprovalPendingListByDate(userId,formDate, toDate ).stream());

    }

    @Override
    public Page<ApprovalHistoryDto> getPageableListData(CommonPageableData pageableData, int userId) {
        commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
        PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
        Page<ApprovalHistory> pageresult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
        List<ApprovalHistoryDto> objlist = convertEntityListToDtoList(pageresult.stream());
        return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
    }


    //..................... Generate Model....................//

    private  ApprovalHistory generateEntity(ApprovalHistoryDto dto, int userId, Boolean isSaved){
        ApprovalHistory entity = new ApprovalHistory();
        BeanUtils.copyProperties(dto, entity);
        if(isSaved){
            entity.setEntryUser(userId);
            commonUtils.setEntryUserInfo(entity);
        }else {
            ApprovalHistory dbEntity = repo.findById(dto.getId()).get();
            entity.setUpdateUser(userId);
            commonUtils.setUpdateUserInfo(entity, dbEntity);
        }
        if(!ObjectUtils.isEmpty(dto.getDepartmentId())){
            SetupDetails obj = new SetupDetails();
            obj.setId(dto.getDepartmentId());
            entity.setDepartment(obj);
        }else {
            entity.setDepartment(null);
        }
        if(!ObjectUtils.isEmpty(dto.getFromTeamId())) {
            ApprovalTeamMaster fromTeamObj = new ApprovalTeamMaster();
            fromTeamObj.setId(dto.getFromTeamId());
            entity.setFromTeam(fromTeamObj);
        }else {
            entity.setFromTeam(null);
        }

        if(!ObjectUtils.isEmpty(dto.getToTeamId())) {
            ApprovalTeamMaster toTeamObj = new ApprovalTeamMaster();
            toTeamObj.setId(dto.getToTeamId());
            entity.setToTeam(toTeamObj);
        }else {
            entity.setToTeam(null);
        }

        if(!ObjectUtils.isEmpty(dto.getFromAppUserId())) {
            AppUser fromAppUserObj = new AppUser();
            fromAppUserObj.setId(dto.getFromAppUserId());
            entity.setFromAppUser(fromAppUserObj);
        }else {
            entity.setFromAppUser(null);
        }

        if(!ObjectUtils.isEmpty(dto.getDefaultAppUserId())) {
            AppUser defaultAppUserObj = new AppUser();
            defaultAppUserObj.setId(dto.getDefaultAppUserId());
            entity.setDefaultAppUser(defaultAppUserObj);
        }else {
            entity.setDefaultAppUser(null);
        }
        return entity;
    }

    private List<ApprovalHistoryDto> convertEntityListToDtoList(Stream<ApprovalHistory> entityList) {
        return  entityList.map(entity -> {
            return generateDto(entity);
        }).collect(Collectors.toList());
    }

    private  ApprovalHistoryDto generateDto(ApprovalHistory entity) {
        ApprovalHistoryDto dto = modelMapper.map(entity, ApprovalHistoryDto.class);

        if(!ObjectUtils.isEmpty(entity.getDepartment())){
            dto.setDepartmentId(entity.getDepartment().getId());
            dto.setDepartmentName(entity.getDepartment().getName());
        }
        if(!ObjectUtils.isEmpty(entity.getFromTeam())) {
            dto.setFromTeamId(entity.getFromTeam().getId());
            dto.setFromTeamName(entity.getFromTeam().getName());
        }

        if(!ObjectUtils.isEmpty(entity.getToTeam())) {
            dto.setToTeamId(entity.getToTeam().getId());
            dto.setToTeamName(entity.getToTeam().getName());
        }

        if(!ObjectUtils.isEmpty(entity.getFromAppUser())) {
            dto.setFromAppUserId(entity.getFromAppUser().getId());
            dto.setFromAppUserName(entity.getFromAppUser().getDisplayName());
        }

        if(!ObjectUtils.isEmpty(entity.getDefaultAppUser())) {
            dto.setDefaultAppUserId(entity.getDefaultAppUser().getId());
            dto.setDefaultAppUserName(entity.getDefaultAppUser().getDisplayName());
        }
        return dto;
    }

    private void modifyTransactionTable(ApprovalHistory savedEntity) {
//        if (savedEntity.getTransactionTypeId().equals(ApprovalConstants.JOURNAL_ENTRY)) {
//            AccountTransactionMaster dbEntity = accountTransactionMasterRepository.findById(savedEntity.getTransactionId()).get();
//            dbEntity.setApprovalStatusId(savedEntity.getApprovalStatusId());
//            dbEntity.setApprovalStatusName(savedEntity.getApprovalStatusName());
//            dbEntity.setApprovalHistory(savedEntity);
//            accountTransactionMasterRepository.save(dbEntity);
//        } else if (savedEntity.getTransactionTypeId().equals(ApprovalConstants.SALARY_APPROVAL)) {
//            SalaryApprovalMaster dbEntity = salaryApprovalMasterRepository.findById(savedEntity.getTransactionId()).get();
//            dbEntity.setApprovalStatusId(savedEntity.getApprovalStatusId());
//            dbEntity.setApprovalStatusName(savedEntity.getApprovalStatusName());
//            dbEntity.setApprovalHistory(savedEntity);
//            salaryApprovalMasterRepository.save(dbEntity);
//            /*on approve work with attendance details*/
//            if(savedEntity.getApprovalStatusId().equals(ApprovalConstants.APPROVED)){
//                workWithSalaryProcessMasterStatus(savedEntity);
//            }
//        } else if (savedEntity.getTransactionTypeId().equals(ApprovalConstants.BONUS_APPROVAL)) {
//            BonusApprovalMaster dbEntity = bonusApprovalMasterRepository.findById(savedEntity.getTransactionId()).get();
//            dbEntity.setApprovalStatusId(savedEntity.getApprovalStatusId());
//            dbEntity.setApprovalStatusName(savedEntity.getApprovalStatusName());
//            dbEntity.setApprovalHistory(savedEntity);
//            bonusApprovalMasterRepository.save(dbEntity);
//            /*on approve work with attendance details*/
//            if(savedEntity.getApprovalStatusId().equals(ApprovalConstants.APPROVED)){
//                workWithBonusProcessMasterStatus(savedEntity);
//            }
//        }
    }


    private void onApproveNotifyOtherUser(ApprovalHistory savedEntity){
        List<ApprovalHistory> allTransactionList = repo.findByTransactionIdAndTransactionTypeId(savedEntity.getTransactionId(), savedEntity.getTransactionTypeId());
        List<Integer> userIdList = new ArrayList<>();
        List<ApprovalHistory> listForSave = new ArrayList<>();
        for(ApprovalHistory obj : allTransactionList){
            if(savedEntity.getApprovalStatusId().equals(ApprovalConstants.SUBMIT)){
                userIdList.add(savedEntity.getDefaultAppUser().getId());
            }
            else if( !ObjectUtils.isEmpty(obj.getApprovalStatusId()) &&
                        !obj.getApprovalStatusId().equals(ApprovalConstants.APPROVED) &&
                        !obj.getApprovalStatusId().equals(ApprovalConstants.BACK) &&
                        !obj.getApprovalStatusId().equals(ApprovalConstants.REJECT) &&
                        obj.getFromAppUser() != null &&
                        !userIdList.contains(obj.getFromAppUser().getId())
            ){
                ApprovalHistory approvalHistory = getApprovalHistory(savedEntity);
                approvalHistory.setDefaultAppUser(obj.getFromAppUser());
                listForSave.add(approvalHistory);
                userIdList.add(obj.getFromAppUser().getId());
            }
        }

        /*now notify missed user*/
        /*List<ApprovalConfiguration> approvalConfigurationList = approvalConfigRepo.findByDepartmentIdTypeIdAndNotNotifyUser(savedEntity.getDepartment().getId(), savedEntity.getTransactionTypeId(), userIdList);
        for(ApprovalConfiguration obj : approvalConfigurationList){
            ApprovalHistory approvalHistory = getApprovalHistory(savedEntity);
            approvalHistory.setDefaultAppUser(obj.getNotifyAppUser());
            listForSave.add(approvalHistory);
        }*/
        repo.saveAll(listForSave);
    }


    private ApprovalHistory getApprovalHistory(ApprovalHistory savedEntity){
        ApprovalHistory approvalHistory = new ApprovalHistory();
        approvalHistory.setEntryUser(savedEntity.getEntryUser());
        commonUtils.setEntryUserInfo(approvalHistory);
        approvalHistory.setDepartment(savedEntity.getDepartment());
        approvalHistory.setTransactionTypeId(savedEntity.getTransactionTypeId());
        approvalHistory.setTransactionTypeName(savedEntity.getTransactionTypeName());
        approvalHistory.setTransactionId(savedEntity.getTransactionId());
        approvalHistory.setTransactionTableName(savedEntity.getTransactionTableName());
        approvalHistory.setFromAppUser(savedEntity.getFromAppUser());
        approvalHistory.setApprovalStatusName(savedEntity.getApprovalStatusName());
        return approvalHistory;
    }


}
