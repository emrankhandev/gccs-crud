package com.gccws.common.entity;

import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Author    Rima
 * @Since     February 22, 2023
 * @version   1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_APPROVAL_TEAM_MASTER")
public class ApprovalTeamMaster extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", length=100)
    private String name;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "DEV_CODE")
    private Integer devCode;

}
