package com.gccws.sysadmin.approve.model;

import com.gccws.sysadmin.approve.dto.ApprovalTeamDetailsDto;
import com.gccws.sysadmin.approve.dto.ApprovalTeamMasterDto;
import lombok.Data;

import java.util.List;

@Data
public class ApprovalTeamModel {
    private ApprovalTeamMasterDto master;
    private List<ApprovalTeamDetailsDto> detailsList;

}
