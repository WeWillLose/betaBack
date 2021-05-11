package com.beta.backend.service.docx.docxCommon;

import com.fasterxml.jackson.databind.JsonNode;

public interface CustomDocFunction {
    String getSumAsTextByExpressionFromData(String expression, JsonNode data);
    boolean isSmthByExpressionFromData(String expression, JsonNode data);
}
