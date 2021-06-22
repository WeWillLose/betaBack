package com.beta.backend.service.report;


import com.beta.backend.domain.model.Report;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface ReportValidationService {
    List<String> validateReport(Report report);

    List<String> validateReportData(JsonNode data);
}
