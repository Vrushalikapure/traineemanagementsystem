package com.lexisnexis.tms.serviceImpl;

import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.repository.WorkHistoryRepository;
import com.lexisnexis.tms.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {
    @Autowired
    WorkHistoryRepository workHistoryRepository;
    public List<WorkHistory> getAll(){
        return workHistoryRepository.findAll();
    }
}
