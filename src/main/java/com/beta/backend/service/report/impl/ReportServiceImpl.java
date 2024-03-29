package com.beta.backend.service.report.impl;

import com.beta.backend.domain.dto.InputStreamResourceDTO;
import com.beta.backend.env.ROLES;
import com.beta.backend.domain.exception.impl.ForbiddenExceptionImpl;
import com.beta.backend.domain.exception.impl.ReportNotFoundExceptionImpl;
import com.beta.backend.domain.exception.impl.UserNotFoundExceptionImpl;
import com.beta.backend.domain.exception.impl.ValidationExceptionImpl;
import com.beta.backend.domain.model.EReportStatus;
import com.beta.backend.domain.model.Report;
import com.beta.backend.domain.model.User;
import com.beta.backend.repo.ReportRepo;
import com.beta.backend.service.docx.report.ReportDocxService;
import com.beta.backend.service.report.ReportService;
import com.beta.backend.service.report.ReportValidationService;
import com.beta.backend.service.user.UserService;
import com.beta.backend.utils.FileNameUtils;
import com.beta.backend.utils.SecurityUtils;
import com.beta.backend.utils.UserUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReportRepo reportRepo;

    private final UserService userService;


    private final ReportDocxService reportDocxService;

    private final ReportValidationService reportValidationService;

    public ReportServiceImpl(ReportRepo reportRepo, @Lazy UserService userService,
                             @Lazy ReportDocxService reportDocxService,
                             ReportValidationService reportValidationService) {
        this.reportRepo = reportRepo;
        this.userService = userService;
        this.reportDocxService = reportDocxService;
        this.reportValidationService = reportValidationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Report> findByReportId(long reportId) {
        Optional<Report> byId = reportRepo.findById(reportId);
        log.info("find {} by {}", byId.orElse(null), reportId);
        return byId;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(long reportId) {
        return reportRepo.existsById(reportId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> findAll() {
        return reportRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> findAllByAuthorId(long authorId) {
        if (!userService.existsById(authorId)) {
            throw new UserNotFoundExceptionImpl(authorId);
        }
        return reportRepo.findAllByAuthorId(authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> findFollowersReports(long chairmanId) {
        List<User> followers = userService.findFollowersByChairmanId(chairmanId);

        if (followers == null) {
            throw new UserNotFoundExceptionImpl(chairmanId);
        }

        List<Report> reports = new ArrayList<>();

        for (User follower : followers) {
            reports.addAll(findAllByAuthorId(follower.getId()));
        }
        return reports;
    }

    @Override
    @Transactional
    public Report saveReport(@NonNull Report report, long authorId) {
        User author = userService.findById(authorId).orElse(null);

        if (author == null) {
            throw new UserNotFoundExceptionImpl(authorId);
        }

        List<String> errors = reportValidationService.validateReport(report);
        if (errors != null) {
            if (!errors.isEmpty()) {
                throw new ValidationExceptionImpl(errors.get(0));
            }
        }
        report.setReportName(FileNameUtils.generateRandomNameIfEmpty(report.getReportName()));
        if (author.getChairman() != null && report.getData().get("META").get("chairmanFIO") == null) {
            ((ObjectNode) report.getData().get("META")).put("chairmanFIO",
                    UserUtils.getShortFioFromUser(author.getChairman())
            );
        }

        report.setStatus(EReportStatus.UNCHECKED);
        report.setAuthor(author);
        log.info("report {} wos saved", report);
        return reportRepo.save(report);

    }

    @Override
    @Transactional
    public Report updateReportStatus(long reportId, @NonNull EReportStatus reportStatus) {
        Report report = findByReportId(reportId).orElse(null);
        if (report == null) {
            throw new ReportNotFoundExceptionImpl(reportId);
        }

        if (SecurityUtils.getCurrentUser() == null)
            throw new NullPointerException("IN updateReport currentUser is null");

        if ((SecurityUtils.hasCurrentUserThisRole(ROLES.CHAIRMAN)
                && userService.isUserInChairmanGroup(report.getAuthor().getId(), SecurityUtils.getCurrentUser().getId()))
                || SecurityUtils.isCurrentUserAdmin())
            report.setStatus(reportStatus);
        else
            throw new ForbiddenExceptionImpl();
        return reportRepo.save(report);

    }

    @Override
    @Transactional
    public Report updateReport(Long reportId, Report report) {
        Report reportById = findByReportId(reportId).orElse(null);
        if (reportById == null) {
            throw new ReportNotFoundExceptionImpl(reportId);
        }

        User currentUser = SecurityUtils.getCurrentUser();

        if (currentUser == null) throw new NullPointerException("IN updateReport currentUser is null");

        if (report.getReportName() != null && !report.getReportName().isBlank()) {
            if (reportById.getStatus() == EReportStatus.UNCHECKED) {
                if (reportById.getAuthor().getId().equals(currentUser.getId())
                        || (SecurityUtils.hasCurrentUserThisRole(ROLES.CHAIRMAN) && userService.isUserInChairmanGroup(reportById.getAuthor().getId(), SecurityUtils.getCurrentUser().getId()))
                        || SecurityUtils.isCurrentUserAdmin()) {
                    reportById.setReportName(report.getReportName());
                } else
                    throw new ForbiddenExceptionImpl();
            } else {
                if (SecurityUtils.hasCurrentUserThisRole(ROLES.CHAIRMAN) &&
                        userService.isUserInChairmanGroup(reportById.getAuthor().getId(), SecurityUtils.getCurrentUser().getId())
                        || SecurityUtils.isCurrentUserAdmin()) {
                    reportById.setReportName(report.getReportName());
                } else
                    throw new ForbiddenExceptionImpl();
            }
        }

        if (report.getStatus() != null) {
            if ((SecurityUtils.hasCurrentUserThisRole(ROLES.CHAIRMAN)
                    && userService.isUserInChairmanGroup(reportById.getAuthor().getId(), SecurityUtils.getCurrentUser().getId()))
                    || SecurityUtils.isCurrentUserAdmin())
                reportById.setStatus(report.getStatus());
            else
                throw new ForbiddenExceptionImpl();
        }

        if (report.getData() != null) {
            if (reportById.getStatus() == EReportStatus.UNCHECKED) {
                if (reportById.getAuthor().getId().equals(currentUser.getId())
                        || (SecurityUtils.hasCurrentUserThisRole(ROLES.CHAIRMAN) && userService.isUserInChairmanGroup(reportById.getAuthor().getId(), SecurityUtils.getCurrentUser().getId()))
                        || SecurityUtils.isCurrentUserAdmin()) {
                    reportById.setData(report.getData());
                } else
                    throw new ForbiddenExceptionImpl();
            } else {
                if (SecurityUtils.hasCurrentUserThisRole(ROLES.CHAIRMAN) &&
                        userService.isUserInChairmanGroup(reportById.getAuthor().getId(), SecurityUtils.getCurrentUser().getId())
                        || SecurityUtils.isCurrentUserAdmin()) {
                    reportById.setData(report.getData());
                } else
                    throw new ForbiddenExceptionImpl();

            }
        }
        User author = reportById.getAuthor();
        if (author != null && author.getChairman() != null && reportById.getData() != null && reportById.getData().get("META") != null && reportById.getData().get("META").get("chairmanFIO") == null) {
            ((ObjectNode) reportById.getData().get("META")).put("chairmanFIO",
                    UserUtils.getShortFioFromUser(author.getChairman())
            );
        }

        return reportRepo.save(reportById);
    }

    @Override
    @Transactional(readOnly = true)
    public InputStreamResourceDTO generateReportDocx(long reportId) {

        Report report = findByReportId(reportId).orElse(null);

        if (report == null) {
            throw new ReportNotFoundExceptionImpl(reportId);
        }
        String reportFileName = FileNameUtils.getReportFileNameOrDefault(report.getReportName());
        if (report.getData() == null) {
            log.error("IN generateReportDocx report.data is null, report: {}", report);
            throw new NullPointerException("IN generateReportDocx report.data is null");
        }
        return new InputStreamResourceDTO(reportDocxService.getReportDocxInputStreamResourceByReportData(report.getData()), reportFileName);
    }

    @Override
    @Transactional
    public void deleteReport(long id) {
        Report report = findByReportId(id).orElse(null);
        if (report == null) {
            throw new ReportNotFoundExceptionImpl(id);
        }
        if (SecurityUtils.getCurrentUser() == null)
            throw new NullPointerException("IN deleteReport SecurityUtils.getCurrentUser() is null");
        if (SecurityUtils.isCurrentUserAdmin()
                || userService.isUserInChairmanGroup(report.getAuthor().getId(), SecurityUtils.getCurrentUser().getId())) {
            reportRepo.delete(report);
        } else {
            throw new ForbiddenExceptionImpl();
        }


    }
}
