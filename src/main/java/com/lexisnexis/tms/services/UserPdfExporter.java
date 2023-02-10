package com.lexisnexis.tms.services;

import com.lexisnexis.tms.entity.WorkHistory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserPdfExporter {

    void export(HttpServletResponse response, List<WorkHistory> workHistoryList) throws IOException;

}
