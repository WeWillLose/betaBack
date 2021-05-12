package com.beta.backend.service.mapper;

import com.beta.backend.dto.InputStreamResourceDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;

public interface IInputStreamResourceMapper {
    InputStreamResourceDTO reportToInputStreamResourceDTO(ByteArrayResource inputStreamResource, String filename);
}
