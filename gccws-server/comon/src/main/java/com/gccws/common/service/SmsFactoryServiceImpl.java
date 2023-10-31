/**
 * 
 */
package com.gccws.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import com.gccws.common.dto.SmsFactoryDto;
import com.gccws.common.entity.SmsFactory;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.SmsFactoryModel;
import com.gccws.common.utils.CommonUtils;
import com.gccws.common.repository.SMSFactoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

/**
 * @Author    Rima
 * @Since     August 28, 2022
 * @version   1.0.0
 */
@Service
@AllArgsConstructor
public class SmsFactoryServiceImpl implements SmsFactoryService{

	private final SMSFactoryRepository repo;

	/*Common Utils*/
	private final CommonUtils commonUtils;

	/*extra code*/
	private final String ENTITY_NAME = SmsFactory.class.getSimpleName();
	
	@Transactional
	@Override
	public SmsFactoryModel save(SmsFactoryModel obj, int userId) {
		List<SmsFactoryDto> detailsList = obj.getDetailsList();
		/*save sms*/
		List<SmsFactory> listForSave = new ArrayList<>();
		Date entryDate = new Date();
		for(SmsFactoryDto dto: detailsList){
			SmsFactory entity = new SmsFactory();
	    	BeanUtils.copyProperties(dto, entity);
	    	entity.setEntryUser(userId);
	    	entity.setEntryDate(entryDate);
			listForSave.add(entity);
        }
		commonUtils.auditLoggingForSave(userId,  ENTITY_NAME, listForSave);
		repo.saveAll(listForSave);
		return null;
	}

	@Transactional
	@Override
	public SmsFactoryModel update(SmsFactoryModel obj, int userId) {
		return null;
	}

	@Transactional
	@Override
	public Boolean delete(SmsFactoryModel obj, int userId) {
		return null;
	}

	@Override
	public SmsFactoryModel getById(int id, int userId) {
		return null;
	}
	
	@Override
    public List<CommonDropdownModel> getDropdownList(int userId) {
		return null;
    }

	@Override
	public Page<SmsFactoryModel> getPageableListData(CommonPageableData pageableData, int userId) {
		return null;
	}

	

	
	//..................... Generate Model....................//

	    
}
