package com.gccws.sysadmin.report.entity;

import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_PARAMETER_ASSIGN_DETAILS")
public class ParameterAssignDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "PARAMETER_ASSIGN_MASTER_ID")
    private ParameterAssignMaster parameterAssignMaster;

    @ManyToOne
    @JoinColumn(name = "PARAMETER_MASTER_ID")
    private ParameterMaster parameterMaster;

    @Column(name = "IS_REQUIRED", columnDefinition = "boolean default true")
    private Boolean isRequired= true;

    @Column(name = "SERIAL_NO")
    private Integer serialNo;

    @Column(name = "IS_DEPENDENT", columnDefinition = "boolean default false")
    private Boolean isDependent= false;

}
