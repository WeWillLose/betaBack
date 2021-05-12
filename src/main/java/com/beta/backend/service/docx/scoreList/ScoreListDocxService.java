package com.beta.backend.service.docx.scoreList;

import com.beta.backend.model.Report;
import lombok.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;

public interface ScoreListDocxService {

    ByteArrayResource getScoreListInputStreamByReport(@NonNull Report report);
}
