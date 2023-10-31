package com.gccws.sysadmin.report.service;

import java.util.List;

import javax.annotation.PostConstruct;

import com.gccws.sysadmin.report.dto.ReportDropdownListDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

/**
 * @Author		Md. Mizanur Rahman
 * @Since		August 28, 2022
 * @version		1.0.0
 */
@Service
@AllArgsConstructor
public class ReportService{
	
	@SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ReportService.class);
	
	private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@PostConstruct
    public void init() {
		
    }
	
	public List<ReportDropdownListDataDto> getDropdownListData(String sql) {

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new ReportDropdownListDataDto(
                                rs.getInt("report_dropdown_id"),
                                rs.getString("report_dropdown_name"),
                                rs.getString("report_dropdown_bangla_name")
                        )
        );
    }

    public List<ReportDropdownListDataDto> getChildDropdownListData(String childSql, Integer paramId) {
        SqlParameterSource parameters = new MapSqlParameterSource("PARAM_ID", paramId);
        return namedParameterJdbcTemplate.query(
                childSql, parameters,
                (rs, rowNum) ->
                        new ReportDropdownListDataDto(
                                rs.getInt("report_dropdown_id"),
                                rs.getString("report_dropdown_name"),
                                rs.getString("report_dropdown_bangla_name")
                        )
        );
    }
    
}
