package com.beta.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputStreamResourceDTO {

    private InputStreamResource inputStreamResource;

    private String fileName;
}
