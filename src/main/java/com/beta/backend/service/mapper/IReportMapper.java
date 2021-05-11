package com.beta.backend.service.mapper;

import com.beta.backend.model.Report;
import com.beta.backend.dto.ReportDTO;

import java.util.List;

public interface IReportMapper {
    List<ReportDTO> reportToReportDTOs(List<Report> reports);

    ReportDTO reportToReportDTO(Report report);

    List<ReportDTO> reportToReportDTOsWithoutData(List<Report> reports);

    ReportDTO reportToReportDTOWithoutData(Report report);

    List<Report> reportDTOsToReport(List<ReportDTO> reportDTOS);

    Report reportDTOToReport(ReportDTO reportDTO);
}
