package com.beta.backend.controller;



import com.beta.backend.dto.InputStreamResourceDTO;
import com.beta.backend.service.scoreList.ScoreListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/scoreList")
@RequiredArgsConstructor
public class ScoreListController {

    private final ScoreListService scoreListService;

    @GetMapping("docx/{id:\\d+}")
    public ResponseEntity<?> getScoreList(@PathVariable Long id){
            InputStreamResourceDTO inputStreamResourceDTO = scoreListService.generateScoreListByReportId(id);
            HttpHeaders headers = new HttpHeaders();
            String format = String.format("attachment; filename=%s", inputStreamResourceDTO.getFileName());
            headers.add(HttpHeaders.CONTENT_DISPOSITION,format);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStreamResourceDTO.getInputStreamResource());
    }
}
