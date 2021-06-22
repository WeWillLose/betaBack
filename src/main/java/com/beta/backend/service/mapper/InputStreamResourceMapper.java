package com.beta.backend.service.mapper;

import com.beta.backend.domain.dto.InputStreamResourceDTO;
import org.springframework.core.io.ByteArrayResource;

public interface InputStreamResourceMapper {
    InputStreamResourceDTO reportToInputStreamResourceDTO(ByteArrayResource inputStreamResource, String filename);
}
