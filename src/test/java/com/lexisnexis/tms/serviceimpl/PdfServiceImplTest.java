package com.lexisnexis.tms.serviceimpl;

import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.repository.WorkHistoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PdfServiceImplTest {

    @InjectMocks
    PdfServiceImpl pdfServiceimpl;

    @Mock
    WorkHistoryRepository workHistoryRepository;

    List<WorkHistory> workHistoryList;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);
        workHistoryList=new ArrayList<>();
        WorkHistory workHistoryUser1=new WorkHistory();
        workHistoryUser1.setUserName("ravikant");
        workHistoryUser1.setWorkingArea("Registration of TMS");
        workHistoryUser1.setComments("Done..");
        WorkHistory workHistoryUser2=new WorkHistory();
        workHistoryUser2.setUserName("Prem");
        workHistoryUser2.setWorkingArea("Registration of TMS");
        workHistoryUser2.setComments("Done..");
        workHistoryList.add(workHistoryUser2);
        workHistoryList.add(workHistoryUser1);
    }

    @Test
    void testGetAllWillReturnAllWorkHistory() {
        Mockito.when(workHistoryRepository.findAll()).thenReturn(workHistoryList);
        assertEquals(2,pdfServiceimpl.getAll() == null ? 0 : pdfServiceimpl.getAll().size());
    }
}