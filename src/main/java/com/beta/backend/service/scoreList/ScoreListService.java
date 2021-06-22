package com.beta.backend.service.scoreList;


import com.beta.backend.domain.dto.InputStreamResourceDTO;

public interface ScoreListService {
    InputStreamResourceDTO generateScoreListByReportId(long reportId) ;
}
