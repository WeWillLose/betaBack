package com.beta.backend.service.mapper.impl;

import com.beta.backend.model.Report;
import com.beta.backend.dto.ReportDTO;
import com.beta.backend.service.mapper.IReportMapper;
import com.beta.backend.service.mapper.IUserMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportMapperImpl implements IReportMapper {
    private final IUserMapper userMapper;

    public ReportMapperImpl(@Lazy IUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<ReportDTO> reportToReportDTOs(List<Report> reports) {
        return reports.stream().map(this::reportToReportDTO).collect(Collectors.toList());
    }

    @Override
    public ReportDTO reportToReportDTO(Report report) {
        return report == null ? null : ReportDTO.builder()
                .author(userMapper.userToUserDTO(report.getAuthor()))
                .data(report.getData())
                .id(report.getId())
                .reportName(report.getReportName())
                .status(report.getStatus())
                .createdDate((report.getCreatedDate()))
                .createdBy(report.getCreatedBy())
                .build();
    }

    @Override
    public List<ReportDTO> reportToReportDTOsWithoutData(List<Report> reports) {
        return reports.stream().map(this::reportToReportDTOWithoutData).collect(Collectors.toList());
    }

    @Override
    public ReportDTO reportToReportDTOWithoutData(Report report) {
        return report == null ? null : ReportDTO.builder()
                .author(userMapper.userToUserDTO(report.getAuthor()))
                .id(report.getId())
                .reportName(report.getReportName())
                .status(report.getStatus())
                .createdDate((report.getCreatedDate()))
                .createdBy(report.getCreatedBy())
                .build();
    }

    @Override
    public List<Report> reportDTOsToReport(List<ReportDTO> reportDTOS) {
        return reportDTOS.stream().map(this::reportDTOToReport).collect(Collectors.toList());
    }

    @Override
    public Report reportDTOToReport(ReportDTO reportDTO) {
        return reportDTO == null ? null : Report.builder()
                .author(userMapper.userDTOToUser(reportDTO.getAuthor()))
                .data(reportDTO.getData())
                .id(reportDTO.getId())
                .reportName(reportDTO.getReportName())
                .status(reportDTO.getStatus())
                .createdBy(reportDTO.getCreatedBy())
                .createdDate(reportDTO.getCreatedDate())
                .build();
    }


}
