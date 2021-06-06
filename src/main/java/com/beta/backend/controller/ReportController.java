package com.beta.backend.controller;


import com.beta.backend.dto.InputStreamResourceDTO;
import com.beta.backend.dto.ReportDTO;
import com.beta.backend.dto.ReportStatusDTO;
import com.beta.backend.model.User;
import com.beta.backend.service.mapper.IReportMapper;
import com.beta.backend.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("api/v1/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    private final IReportMapper reportMapperService;

    @PostMapping("")
    public ResponseEntity<ReportDTO> saveReport(@RequestBody ReportDTO report, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(reportMapperService.reportToReportDTO(reportService.saveReport(reportMapperService.reportDTOToReport(report), user.getId())));

    }

    @GetMapping("docx/{id:\\d+}")
    public ResponseEntity<ByteArrayResource> getReportDocx(@PathVariable Long id){
        InputStreamResourceDTO inputStreamResource = reportService.generateReportDocx(id);
        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(inputStreamResource.getFileName(), StandardCharsets.UTF_8)
                .build();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(inputStreamResource.getInputStreamResource().contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource.getInputStreamResource());
    }

    @GetMapping("author/current")
    public ResponseEntity<List<ReportDTO>> getReportsByCurrentUser(@AuthenticationPrincipal User user) {
        List<ReportDTO> reportDTOS = reportMapperService.reportToReportDTOsWithoutData(reportService.findAllByAuthorId(user.getId()));
        return ResponseEntity.ok().body(reportDTOS);
    }

    @GetMapping("followers/{id}")
    public ResponseEntity<List<ReportDTO>> getReportsByChairmanId(@PathVariable(name = "id") Long chairmanID) {
        List<ReportDTO> followersReports = reportMapperService.reportToReportDTOsWithoutData(reportService.findFollowersReports(chairmanID));
        return ResponseEntity.ok().body(followersReports);
    }

    @GetMapping("followers/current")
    public ResponseEntity<List<ReportDTO>> getFollowerReportsByCurrentChairmanId(@AuthenticationPrincipal User user) {
        List<ReportDTO> followersReports = reportMapperService.reportToReportDTOsWithoutData(reportService.findFollowersReports(user.getId()));
        return ResponseEntity.ok().body(followersReports);
    }

    @GetMapping("all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        List<ReportDTO> reports = reportMapperService.reportToReportDTOsWithoutData(reportService.findAll());
        return ResponseEntity.ok().body(reports);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteReportById(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("author/{id}")
    public ResponseEntity<List<ReportDTO>> getReportByAuthor(@PathVariable Long id) {
        return ResponseEntity.ok().body(reportMapperService.reportToReportDTOs(reportService.findAllByAuthorId(id)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ReportDTO> getReportByReportId(@PathVariable(name = "id") Long reportId) {
        return ResponseEntity.ok().body(reportMapperService.reportToReportDTO(reportService.findByReportId(reportId).orElse(null)));
    }

    @GetMapping("withoutdata/{id}")
    public ResponseEntity<ReportDTO> getReportByReportIdWithoutData(@PathVariable(name = "id") Long reportId) {
        return ResponseEntity.ok().body(reportMapperService.reportToReportDTOWithoutData(reportService.findByReportId(reportId).orElse(null)));
    }

    @PutMapping("{id}")
    public ResponseEntity<ReportDTO> updateReport(@PathVariable(name = "id") Long reportId, @RequestBody ReportDTO reportDTO) {
        return ResponseEntity.ok().body(reportMapperService.reportToReportDTO(reportService.updateReport(reportId,
                reportMapperService.reportDTOToReport(reportDTO))));
    }

    @PutMapping("status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CHAIRMAN')")
    public ResponseEntity<ReportDTO> updateReportStatus(@PathVariable(name = "id") Long reportId, @Valid @RequestBody ReportStatusDTO reportStatusDTO) {
        return ResponseEntity.ok().body(reportMapperService.reportToReportDTO(reportService.updateReportStatus(reportId, reportStatusDTO.getStatus())));
    }
}
