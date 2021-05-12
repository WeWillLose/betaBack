package com.beta.backend.service.docx.report;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;

public interface ReportDocxService {
    ByteArrayResource getReportDocxInputStreamResourceByReportData(JsonNode data);
}
