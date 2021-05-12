package com.beta.backend.service.mapper.impl;

import com.beta.backend.dto.InputStreamResourceDTO;
import com.beta.backend.service.mapper.IInputStreamResourceMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InputStreamResourceMapperImpl implements IInputStreamResourceMapper {

    @Override
    public InputStreamResourceDTO reportToInputStreamResourceDTO(ByteArrayResource inputStreamResource, String filename) {
        if (inputStreamResource == null) {
            return null;
        } else {
            return InputStreamResourceDTO.builder()
                .fileName(StringUtils.hasText(filename)?filename:"")
                .inputStreamResource(inputStreamResource)
                .build();
        }
    }

}
