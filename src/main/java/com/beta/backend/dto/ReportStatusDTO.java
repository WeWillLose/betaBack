package com.beta.backend.dto;

import com.beta.backend.model.EReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportStatusDTO {
    @NonNull
    private EReportStatus status;
}
