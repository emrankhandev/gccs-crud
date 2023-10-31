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
@Table(name = "COMMON_SETUP_DETAILS")
public class SetupDetails extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "MASTER_ID", nullable = false)
    private SetupMaster master;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private SetupDetails parent;

    @Column(name = "NAME", length = 200, nullable = false)
    private String name;

    @Column(name = "BANGLA_NAME", length = 200, nullable = false)
    private String banglaName;

    @Column(name = "SHORT_CODE", length = 30)
    private String shortCode;

    @Column(name = "MODULE_ID")
    private Integer moduleId;

}
