package com.gccws.common.model;

import com.gccws.base.model.PageableData;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommonPageableData  extends PageableData {

    private Integer intParam1;
    private Integer intParam2;
    private Integer intParam3;
    private Integer intParam4;
    private Integer intParam5;
    private Integer intParam6;
    private String stringParam1;
    private Date dateParam1;
    private Date dateParam2;

    private List<Integer> listIntParam1;

}
