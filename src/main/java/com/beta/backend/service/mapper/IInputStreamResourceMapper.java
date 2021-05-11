package com.beta.backend.service.mapper;

import com.beta.backend.dto.InputStreamResourceDTO;
import org.springframework.core.io.InputStreamResource;

public interface IInputStreamResourceMapper {
    InputStreamResourceDTO reportToInputStreamResourceDTO(InputStreamResource inputStreamResource, String filename);
}
