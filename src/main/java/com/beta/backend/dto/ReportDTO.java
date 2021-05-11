package com.beta.backend.dto;

import com.beta.backend.model.EReportStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class ReportDTO extends AuditBaseModel {

    private Long id;

    @NotNull
    private JsonNode data;

    private EReportStatus status;

    private UserDTO author;

    private String reportName;
}
