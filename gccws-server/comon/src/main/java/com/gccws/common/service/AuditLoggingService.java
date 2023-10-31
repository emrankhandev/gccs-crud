package com.gccws.common.service;

/**
 * @Author    Md. Tawhidul Islam
 * @Since     December 6, 2022
 * @version   1.0.0
 */

public interface AuditLoggingService {

    Boolean saveAuditLogging(Integer userId, String entityName, Object currentData);

    Boolean updateAuditLogging(Integer userId, String entityName, Object currentData, Object oldData);

    Boolean deleteAuditLogging(Integer userId, String entityName, Object currentData);

    Boolean getMethodAuditLogging(Integer userId, String entityName, String getMethod);
    Boolean SignInAuditLogging(Integer userId, String entityName);

}
