package com.beta.backend.service.docx.scoreList.impl;


import com.beta.backend.model.Report;
import com.beta.backend.service.docx.docxCommon.CustomDocFunction;
import com.beta.backend.service.docx.scoreList.ScoreListDocxService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

//Not working
@RequiredArgsConstructor
@Slf4j
@Lazy
public class ScoreListDocxServiceImpl implements ScoreListDocxService {
    @Value("${doc.scoreList.placeholderRegexp}")
    private String sourceRegexp;

    @Value("${doc.scoreList.pathToScoreListTemplate}")
    private String pathToScoreListTemplate;

    private final com.beta.backend.service.docx.docxCommon.XWPFDocxCommonService XWPFDocxCommonService;

    private final CustomDocFunction customDocFunction;

    private void replacePlaceholdersInTables(@NonNull List<XWPFTable> tables,@NonNull JsonNode data,@NonNull String regexp){
        if(tables.isEmpty()){
            log.warn("IN replacePlaceholdersInTables tables is empty");
        }
        Pattern pattern = Pattern.compile(regexp);
        for (XWPFTable table : tables) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell tableCell : row.getTableCells()) {
                    for (XWPFParagraph p : tableCell.getParagraphs()) {
                        // Collate text of all runs
                        StringBuilder sb = XWPFDocxCommonService.getTextFromAllRuns(p.getRuns());
                        // Continue if there is text and contains "test"
                        if (sb.length() > 0) {
                            if(Pattern.matches(regexp,sb.toString())){
                                String placeholder = sb.toString();
                                String placeholderValue = placeholder.replace("{{", "").replace("}}", "");
                                String value;
                                if(placeholderValue.startsWith("sum(")){
                                    value = customDocFunction.getSumAsTextByExpressionFromData(placeholderValue,data);
                                }else if(placeholderValue.startsWith("issmth(")){
                                    value = customDocFunction.isSmthByExpressionFromData(placeholderValue,data)?"+":"-";
                                }else{
                                    value = data.get(placeholderValue)==null?null:data.get(placeholderValue).asText();
                                }
                                String text;
                                if (value != null) {
                                    text = sb.toString().replace(placeholder, value);
                                } else {
                                    log.warn("IN replacePlaceholders {} not found in data", placeholderValue);
                                    text = sb.toString().replace(placeholder, "");
                                }
                                sb.delete(0,sb.length());
                                sb.append(text);
                                //delete all runs
                                XWPFDocxCommonService.removeAllRuns(p);
                                // Add new run with updated text
                                XWPFRun run = p.createRun();
                                run.setText(text);
                            }
                        }
                    }
                }
            }
        }
    }

    private void parsScoreListTemplate(@NonNull XWPFDocument scoreListTemplate,@NonNull  JsonNode data,@NonNull String regexp){
        replacePlaceholdersInTables(scoreListTemplate.getTables(),data,regexp);
        XWPFDocxCommonService.replacePlaceholdersInParagraphsFromData(scoreListTemplate.getParagraphs(), data,regexp);
    }

    private ByteArrayResource getInputStreamFromDoc(@NonNull XWPFDocument docx) throws IOException {

        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
        docx.write(byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        }
    }

    @Override
    public ByteArrayResource getScoreListInputStreamByReport(@NonNull Report report)  {
        if(report.getData() == null){
            throw new NullPointerException("IN getScoreListInputStreamByReport report.data is null");
        }

        try (FileInputStream fos = new FileInputStream(pathToScoreListTemplate)) {
            XWPFDocument scoreListTemplate = new XWPFDocument(fos);
            parsScoreListTemplate(scoreListTemplate, report.getData(),String.valueOf(sourceRegexp));
            return getInputStreamFromDoc(scoreListTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
