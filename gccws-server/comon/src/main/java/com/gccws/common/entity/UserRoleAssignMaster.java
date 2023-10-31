package com.gccws.common.entity;

import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @Author    Md. Chabed Alam
 * @Since     January 09, 2023
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_USER_ROLE_ASSIGN_MASTER")
public class UserRoleAssignMaster extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne
    @JoinColumn(name = "APP_USER_ID", nullable = false, unique = true)
    private AppUser appUser;

}
