package com.beta.backend.service.docx.scoreList;

import com.beta.backend.domain.model.Report;
import lombok.NonNull;
import org.springframework.core.io.ByteArrayResource;

public interface ScoreListDocxService {

    ByteArrayResource getScoreListInputStreamByReport(@NonNull Report report);
}
