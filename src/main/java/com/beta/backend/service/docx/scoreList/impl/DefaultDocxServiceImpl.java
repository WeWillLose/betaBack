package com.beta.backend.service.docx.scoreList.impl;


import com.beta.backend.model.Report;
import com.beta.backend.service.docx.docxCommon.XWPFDocxCommonService;
import com.beta.backend.service.docx.scoreList.ScoreListDocxService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultDocxServiceImpl implements ScoreListDocxService {

    @Value("${doc.scoreList.pathToScoreListTemplate}")
    private String pathToScoreListTemplate;

    @Value("${doc.scoreList.placeholderRegexp}")
    private String REGEXP;

    private final XWPFDocxCommonService xwpfDocxCommonService;

    private final ComputedValuesServiceImpl computedValuesService;



    private ByteArrayResource getInputStream(@NonNull  XWPFDocument docx) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        docx.write(byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }

    private void parsScoreListTemplate(@NonNull XWPFDocument docx,@NonNull  String REGEXP,@NonNull JsonNode reportData){
        List<XWPFTable> tables = docx.getTables();
        if (tables.isEmpty()) {
            return;
        }
        ObjectNode data = getData(reportData);

        Pattern regexp = Pattern.compile(REGEXP);

        for (XWPFTable table : docx.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell tableCell : row.getTableCells()) {
                    for (XWPFParagraph p : tableCell.getParagraphs()) {
                        // Collate text of all runs
                        StringBuilder sb = xwpfDocxCommonService.getTextFromAllRuns(p.getRuns());
                        // Continue if there is text
                        if (sb.length() > 0) {
                            Matcher matcher = regexp.matcher(sb.toString());
                            while (matcher.find()) {
                                // Remove all existing runs
                                xwpfDocxCommonService.removeAllRuns(p);

                                String group0 = matcher.group(0);
                                String group1 = matcher.group(1);

                                JsonNode value = data.get(group1);

                                if(value == null){
                                    value = data.get("computed").get(group1);
                                }
                                if(value == null){
                                    value = data.get("META").get(group1);
                                }
                                if(value == null){
                                    value = data.get("tables").get(group1);
                                }

                                String text;
                                if (value != null) {
                                    log.info("IN replacePlaceholders {} replacedBy: {}", group1,value);
                                    text = sb.toString().replace(group0, value.asText());
                                } else {
                                    log.warn("IN replacePlaceholders {} not found in data", group1);
                                    text = sb.toString().replace(group0, "");
                                }
                                sb.delete(0,sb.length());
                                sb.append(text);
                                // Add new run with updated text
                                XWPFRun run = p.createRun();
                                run.setText(text);
                            }
                        }
                    }
                }
            }
        }
        xwpfDocxCommonService.replacePlaceholdersInParagraphsFromData(docx.getParagraphs(),data,REGEXP);
    }

    private ObjectNode getData(@NonNull JsonNode reportData) {
        ObjectNode jsonNodes = JsonNodeFactory.instance.objectNode();
        jsonNodes.set("computed",reportData.get("computed"));
        jsonNodes.set("META",reportData.get("META"));
        jsonNodes.set("tables",reportData.get("tables"));

        jsonNodes.put("1", computedValuesService.getScoreSum(reportData.get("tables").get("comment"),8));
        jsonNodes.put("1.1", computedValuesService.getScoreSum(reportData.get("tables").get("comment")));
        jsonNodes.put("2.1", computedValuesService.getScoreSum(reportData.get("tables").get("creation")));
        jsonNodes.put("2.2", computedValuesService.getScoreSum(reportData.get("tables").get("proective")));
        jsonNodes.put("2.2.1", computedValuesService.getScoreSum(reportData.get("tables").get("proective"),0,null));
        jsonNodes.put("2.2.2", computedValuesService.getScoreSum(reportData.get("tables").get("proective"),1,null));
        double g2 = jsonNodes.get("2.1").asDouble(0) + jsonNodes.get("2.2").asDouble(0);
        jsonNodes.put("2",g2>8?8:g2);
        jsonNodes.put("procent1", computedValuesService.getProcent1(jsonNodes.get("computed").get("sum1").asDouble(0)));
        jsonNodes.put("3.1", computedValuesService.IsSmth(reportData.get("tables").get("working_program")));
        jsonNodes.put("3.2.1", computedValuesService.getScoreSum(reportData.get("tables").get("class_rooms")));
        jsonNodes.put("3.2.2", computedValuesService.getScoreSum(reportData.get("tables").get("programs")));
        jsonNodes.put("3.2.3", computedValuesService.getScoreSum(reportData.get("tables").get("reconstruction")));
        jsonNodes.put("3.2", jsonNodes.get("3.2.1").asDouble(0) + jsonNodes.get("3.2.2").asDouble(0) + jsonNodes.get("3.2.3").asDouble(0));
        jsonNodes.put("3.3", computedValuesService.getScoreSum(reportData.get("tables").get("complex")));
        jsonNodes.put("3.4", computedValuesService.getScoreSum(reportData.get("tables").get("teachingaids")));
        jsonNodes.put("3.5", computedValuesService.getScoreSum(reportData.get("tables").get("education")));
        jsonNodes.put("3.6", computedValuesService.getScoreSum(reportData.get("tables").get("sdo")));
        double g3 = jsonNodes.get("3.2").asDouble(0) + jsonNodes.get("3.3").asDouble(0) + jsonNodes.get("3.4").asDouble(0) + jsonNodes.get("3.5").asDouble(0) + jsonNodes.get("3.6").asDouble(0);
        jsonNodes.put("3",g3>13?13:g3 );
        jsonNodes.put("4.1", computedValuesService.getScoreSum(reportData.get("tables").get("plan")));
        jsonNodes.put("4.2", computedValuesService.getScoreSum(reportData.get("tables").get("circle")));
        jsonNodes.put("4.3", computedValuesService.getScoreSum(reportData.get("tables").get("institutions")));
        jsonNodes.put("4.4", computedValuesService.getScoreSum(reportData.get("tables").get("events")));
        double g4 = jsonNodes.get("4.1").asDouble(0) + jsonNodes.get("4.2").asDouble(0) + jsonNodes.get("4.3").asDouble(0) + jsonNodes.get("4.4").asDouble(0);
        jsonNodes.put("4", g4>6?6:g4);
        jsonNodes.put("5.1", computedValuesService.IsSmth(reportData.get("tables").get("plan_group")));
        jsonNodes.put("5.2", computedValuesService.getScoreSum(reportData.get("tables").get("coolhours")));
        jsonNodes.put("5.3", computedValuesService.getScoreSum(reportData.get("tables").get("activity")));
        jsonNodes.put("5.4", computedValuesService.getScoreSum(reportData.get("tables").get("obz")));
        double g5 = jsonNodes.get("5.2").asDouble(0) + jsonNodes.get("5.3").asDouble(0) + jsonNodes.get("5.4").asDouble(0);
        jsonNodes.put("5", g5>4?4:g5);
        jsonNodes.put("6.1.1", computedValuesService.getScoreSum(reportData.get("tables").get("selfeducation")));
        jsonNodes.put("6.1.2", computedValuesService.getScoreSum(reportData.get("tables").get("qualification")));
        jsonNodes.put("6.1.3", computedValuesService.getScoreSum(reportData.get("tables").get("seminars")));
        jsonNodes.put("6.1.4", computedValuesService.getScoreSum(reportData.get("tables").get("participation")));
        jsonNodes.put("6.1", jsonNodes.get("6.1.1").asDouble(0) + jsonNodes.get("6.1.2").asDouble(0) + jsonNodes.get("6.1.3").asDouble(0) + jsonNodes.get("6.1.4").asDouble(0));
        jsonNodes.put("6.2", computedValuesService.getScoreSum(reportData.get("tables").get("contest")));
        double g6 = jsonNodes.get("6.1").asDouble(0) + jsonNodes.get("6.2").asDouble(0);
        jsonNodes.put("6", g6>7?7:g6);
        jsonNodes.put("7", computedValuesService.getScoreSum(reportData.get("tables").get("technologies"),2));
        jsonNodes.put("8", computedValuesService.getScoreSum(reportData.get("tables").get("experience"),4));
        jsonNodes.put("9", computedValuesService.getScoreSum(reportData.get("tables").get("interaction"),4));
        jsonNodes.put("9.1", computedValuesService.getScoreSum(reportData.get("tables").get("interaction"),0,null));
        jsonNodes.put("9.2", computedValuesService.getScoreSum(reportData.get("tables").get("interaction"),1,null));
        jsonNodes.put("9.3", computedValuesService.getScoreSum(reportData.get("tables").get("interaction"),2,null));
        jsonNodes.put("9.4", computedValuesService.getScoreSum(reportData.get("tables").get("interaction"),3,null));
        jsonNodes.put("9.5", computedValuesService.getScoreSum(reportData.get("tables").get("interaction"),4,null));
        jsonNodes.put("10", computedValuesService.getScoreSum(reportData.get("tables").get("subject"),1));
        jsonNodes.put("10.1", computedValuesService.getScoreSum(reportData.get("tables").get("subject"),1));
        jsonNodes.put("11", computedValuesService.getScoreSum(reportData.get("tables").get("manual"),20));
        jsonNodes.put("procent2", computedValuesService.getProcent2(jsonNodes.get("computed").get("sum2").asDouble(0)));
        return jsonNodes;
    }

    @Override
    public ByteArrayResource getScoreListInputStreamByReport(Report report)  {
        try (FileInputStream fos = new FileInputStream(pathToScoreListTemplate)) {
            XWPFDocument docx = new XWPFDocument(fos);
            parsScoreListTemplate(docx, REGEXP, report.getData());
            return getInputStream(docx);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
