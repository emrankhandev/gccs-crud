package com.gccws.common.service;

import com.gccws.common.utils.CommonUtils;
import com.gccws.common.repository.AuditLoggingRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @Author    Md. Tawhidul Islam
 * @Since     December 6, 2022
 * @version   1.0.0
 */

@AllArgsConstructor
@Service
public class AuditLoggingServiceImpl implements AuditLoggingService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLoggingServiceImpl.class);
    /*repository*/
    private final AuditLoggingRepository repo;

    /*utils*/
    private final CommonUtils commonUtils;


    @Transactional
    @Override
    public Boolean saveAuditLogging(Integer userId, String entityName, Object currentData){
        try {
            commonUtils.auditLoggingForSave(userId, entityName, currentData);
            return true;
        }catch(Exception e){
            LOG.info("Exception for audit logging: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    @Override
    public Boolean updateAuditLogging(Integer userId, String entityName, Object currentData, Object oldData){
        try {
            commonUtils.auditLoggingForUpdate(userId, entityName, currentData, oldData);
            return true;
        }catch(Exception e){
            LOG.info("Exception for audit logging: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    @Override
    public Boolean deleteAuditLogging(Integer userId, String entityName, Object currentData) {
        try {
            commonUtils.auditLoggingForDelete(userId, entityName, currentData);
            return true;
        }catch(Exception e){
            LOG.info("Exception for audit logging: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    @Override
    public Boolean getMethodAuditLogging(Integer userId, String entityName, String getMethod) {
        try {
            commonUtils.auditLoggingForGet(userId, entityName, getMethod);
            return true;
        } catch (Exception e) {
            LOG.info("Exception for audit logging: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    @Override
    public Boolean SignInAuditLogging(Integer userId, String entityName) {
        try {
            commonUtils.auditLoggingForSignIn(userId, entityName);
            return true;
        } catch (Exception e) {
            LOG.info("Exception for audit logging: {}", e.getMessage());
            return false;
        }
    }
}

