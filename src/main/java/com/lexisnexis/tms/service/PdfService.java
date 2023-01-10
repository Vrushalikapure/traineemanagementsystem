package com.lexisnexis.tms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.repository.WorkHistoryRepository;

@Service
public class PdfService {

	@Autowired
	WorkHistoryRepository workHistoryRepository;
	
	public List<WorkHistory> getAll(){		
		return workHistoryRepository.findAll();
	}
}
