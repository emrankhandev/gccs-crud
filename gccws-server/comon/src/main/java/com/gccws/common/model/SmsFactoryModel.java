package com.gccws.common.model;

import com.gccws.common.dto.SmsFactoryDto;
import lombok.Data;

import java.util.List;

@Data
public class SmsFactoryModel {
    private List<SmsFactoryDto> detailsList;

}
