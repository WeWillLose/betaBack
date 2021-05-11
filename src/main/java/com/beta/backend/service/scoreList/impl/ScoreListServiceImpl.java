package com.beta.backend.service.scoreList.impl;

import com.beta.backend.model.Report;
import com.beta.backend.dto.InputStreamResourceDTO;
import com.beta.backend.exception.impl.ReportNotFoundExceptionImpl;
import com.beta.backend.service.docx.scoreList.ScoreListDocxService;
import com.beta.backend.service.report.ReportService;
import com.beta.backend.service.scoreList.ScoreListService;
import com.beta.backend.utils.FileNameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScoreListServiceImpl implements ScoreListService {

    private final ReportService reportService;

    private final ScoreListDocxService scoreListDocxService;

    @Override
    public InputStreamResourceDTO generateScoreListByReportId(long reportId)  {
        Report byReportId = reportService.findByReportId(reportId).orElse(null);
        if (byReportId != null) {
            return new InputStreamResourceDTO(scoreListDocxService.getScoreListInputStreamByReport(byReportId),FileNameUtils.getScoreListFileNameOrDefault(byReportId.getReportName()));
        }else{
            throw new ReportNotFoundExceptionImpl(reportId);
        }
    }

}
