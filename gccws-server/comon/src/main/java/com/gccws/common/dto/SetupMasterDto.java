package com.gccws.common.dto;


import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author    Emran Khan
 * @Since     February 1, 2022
 * @version   1.0.0
 */


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SetupMasterDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer parentId;
    private String parentName;

    private Integer devCode;

    private Integer moduleId;

}