package com.gccws.common.repository;
import com.gccws.common.entity.AuditLogging;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLoggingRepository extends JpaRepository<AuditLogging, Integer> {

}

