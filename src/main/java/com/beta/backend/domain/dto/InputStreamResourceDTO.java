package com.beta.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputStreamResourceDTO {

    private ByteArrayResource inputStreamResource;

    private String fileName;
}
