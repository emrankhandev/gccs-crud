package com.gccws.common.service;

import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.dto.MessageHistoryDto;
import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.MenuItem;
import com.gccws.common.entity.MessageHistory;
import com.gccws.common.repository.MessageHistoryRepository;
import com.gccws.common.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author  Md. Mizanur Rahman
 * @Email   kmmizanurrahmanjp@gmail.com
 * @since   March 14,2023
 * @version 1.0
 */


@AllArgsConstructor
@Service
public class MessageHistoryServiceImpl implements MessageHistoryService {

    /*repository*/
    private final MessageHistoryRepository repo;
    private ModelMapper modelMapper;
    private final CommonUtils commonUtils;
    private final String ENTITY_NAME = MessageHistory.class.getSimpleName();

    @Transactional
    @Override
    public MessageHistoryDto save(MessageHistoryDto obj, int userId) {
        MessageHistory savedEntity = repo.save(generateEntity(obj, userId, true));
        commonUtils.auditLoggingForSave(userId,  ENTITY_NAME, generateDto(savedEntity));
        return generateDto(savedEntity);
    }

    @Transactional
    @Override
    public MessageHistoryDto update(MessageHistoryDto obj, int userId) {
        MessageHistoryDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
        MessageHistory savedEntity = repo.save(generateEntity(obj,userId, false));
        commonUtils.auditLoggingForUpdate(userId,  ENTITY_NAME, generateDto(savedEntity), oldAuditDto);
        return generateDto(savedEntity);
    }

    @Transactional
    @Override
    public Boolean delete(MessageHistoryDto obj,int userId) {return false;}

    @Override
    public MessageHistoryDto getById(int id,int userId) {return null;}

    @Override
    public List<CommonDropdownModel> getDropdownList(int userId) {return null; }

    @Override
    public Page<MessageHistoryDto> getPageableListData(CommonPageableData pageableData, int userId) {
        return null;
    }

    @Override
    public List<MessageHistoryDto> getByTransactionIdAndTransactionTable(Integer transactionId, String transactionTable) {
        return convertEntityListToDtoList(repo.findByTransactionIdAndTransactionTable(transactionId, transactionTable).stream());
    }

    @Override
    public List<MessageHistoryDto> getByUserId(Integer userId) {
        return convertEntityListToDtoList(repo.findByUserId(userId).stream());
    }

    //..................... Generate Model....................//

    public MessageHistory generateEntity(MessageHistoryDto dto, int userId, Boolean isSaved){
        MessageHistory entity = new MessageHistory();
        BeanUtils.copyProperties(dto, entity);
        if(isSaved){
            entity.setEntryUser(userId);
            commonUtils.setEntryUserInfo(entity);
        } else {
            MessageHistory dbEntity = repo.findById(dto.getId()).get();
            entity.setUpdateUser(userId);
            commonUtils.setUpdateUserInfo(entity, dbEntity);
        }
        if(!ObjectUtils.isEmpty(dto.getModuleId())){
            MenuItem obj = new MenuItem();
            obj.setId(dto.getModuleId());
            entity.setModule(obj);
        }
        if(!ObjectUtils.isEmpty(dto.getAuthorityId())){
            AppUser obj = new AppUser();
            obj.setId(dto.getAuthorityId());
            entity.setAuthority(obj);
        }
        if(!ObjectUtils.isEmpty(dto.getReceivedUserId())){
            AppUser obj = new AppUser();
            obj.setId(dto.getReceivedUserId());
            entity.setReceivedUser(obj);
        }
        return  entity;
    }

    public List<MessageHistoryDto> convertEntityListToDtoList(Stream<MessageHistory> entityList){
        return entityList.map(this::generateDto).collect(Collectors.toList());
    }

    public MessageHistoryDto generateDto(MessageHistory entity){
        MessageHistoryDto dto = modelMapper.map(entity, MessageHistoryDto.class);

        if(!ObjectUtils.isEmpty(entity.getModule())){
            dto.setModuleId(entity.getModule().getId());
            dto.setModuleName(entity.getModule().getName());
        }
        if(!ObjectUtils.isEmpty(entity.getAuthority())){
            dto.setAuthorityId(entity.getAuthority().getId());
            dto.setAuthorityName(entity.getAuthority().getDisplayName());
        }
        if(!ObjectUtils.isEmpty(entity.getReceivedUser())){
            dto.setReceivedUserId(entity.getReceivedUser().getId());
            dto.setReceivedUserName(entity.getReceivedUser().getDisplayName());
        }
        return dto;
    }


}

