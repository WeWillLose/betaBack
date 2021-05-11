package com.beta.backend.service.docx.scoreList;

import com.fasterxml.jackson.databind.JsonNode;

public interface ComputedValuesService {
    Double getScoreSum(JsonNode dataRows);

    Double getScoreSum(JsonNode dataRows, int max);

    Double getScoreSum(JsonNode dataRows, int rowInd, Integer max);

    double getProcent1(double score);

    double getProcent2(double score);

    String IsSmth(JsonNode data);
}
