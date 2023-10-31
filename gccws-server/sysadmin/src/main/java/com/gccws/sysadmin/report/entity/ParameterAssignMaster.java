package com.gccws.sysadmin.report.entity;


import com.gccws.base.entity.BaseEntity;
import com.gccws.common.entity.MenuItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_PARAMETER_ASSIGN_MASTER")
public class ParameterAssignMaster extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "MENU_ITEM_ID")
    private MenuItem menuItem;

}
