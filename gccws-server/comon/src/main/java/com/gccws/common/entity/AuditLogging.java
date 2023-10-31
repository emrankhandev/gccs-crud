package com.gccws.common.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author    Md. Tawhidul Islam
 * @Since     November 24 , 2022
 * @version   1.0.0
 */


@Data
@Entity
@Table(name = "COMMON_AUDIT_LOGGING")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class AuditLogging {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Integer id;

    @Column(name = "ENTRY_USER")
    private Integer entryUser;

    @Column(name = "ENTRY_DATE")
    private Date entryDate;

    @Column(name = "TABLE_NAME", length = 100)
    private String tableName;

    @Column(name = "AUDIT_TYPE", length = 20)
    private String auditType;

    @Type(type = "jsonb")
    @Column(name = "CURRENT_DATA", columnDefinition = "jsonb")
    private Object currentData;

    @Type(type = "jsonb")
    @Column(name = "OLD_DATA", columnDefinition = "jsonb")
    private Object oldData;


    @Column(name = "GET_METHOD")
    private String getMethod;
}
