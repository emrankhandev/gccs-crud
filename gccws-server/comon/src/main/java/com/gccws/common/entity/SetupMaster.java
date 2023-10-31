package com.gccws.common.entity;


import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @Author    Emran Khan
 * @Since     February 1, 2022
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "COMMON_SETUP_MASTER")
public class SetupMaster extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", length=200, nullable = false, unique = true)
    private String name;

    @Column(name = "BANGLA_NAME", length=200, nullable = false, unique = true)
    private String banglaName;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private SetupMaster parent;

    @Column(name = "DEV_CODE", length=30)
    private Integer devCode;

    @Column(name = "MODULE_ID")
    private Integer moduleId;

}
