package com.beta.backend.service.report;

import com.beta.backend.model.EReportStatus;
import com.beta.backend.model.Report;
import com.beta.backend.dto.InputStreamResourceDTO;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReportService {
    @Transactional(readOnly = true)
    Optional<Report> findByReportId(long reportId);

    @Transactional(readOnly = true)
    boolean existsById(long reportId);

    @Transactional(readOnly = true)
    List<Report> findAll();

    @Transactional(readOnly = true)
    List<Report> findAllByAuthorId(long authorId);

    @Transactional(readOnly = true)
    List<Report> findFollowersReports(long chairmanId);

    @Transactional
    Report saveReport(@NonNull Report report, long authorId);

    @Transactional
    Report updateReportStatus(long reportId, @NonNull EReportStatus reportStatus);

    @Transactional
    void deleteReport(long id);

    @Transactional(readOnly = true)
    InputStreamResourceDTO generateReportDocx(long reportId);

    @Transactional
    Report updateReport(Long reportId, Report reportDTOToReport);
}
