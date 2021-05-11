package com.beta.backend.service.docx.scoreList;

import com.beta.backend.model.Report;
import lombok.NonNull;
import org.springframework.core.io.InputStreamResource;

public interface ScoreListDocxService {

    InputStreamResource getScoreListInputStreamByReport(@NonNull Report report);
}
