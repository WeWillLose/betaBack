package com.beta.backend.service.docx.report;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.io.InputStreamResource;

public interface ReportDocxService {
    InputStreamResource getReportDocxInputStreamResourceByReportData(JsonNode data) ;
}
