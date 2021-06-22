package com.beta.backend.service.docx.docxCommon.impl;


import com.beta.backend.service.docx.docxCommon.XWPFDocxCommonService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class XWPFXWPFDocxCommonServiceImpl implements XWPFDocxCommonService {

    @Override
    public void removeAllRuns(XWPFParagraph p) {

        if (p == null) {
            return;
        }
        int runsCount = p.getRuns().size();
        for (int i = 0; i < runsCount; i++) {
            p.removeRun(0);
        }
    }

    @Override
    public StringBuilder getTextFromAllRuns(List<XWPFRun> runs) {
        if(runs ==null || runs.isEmpty()) return new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (XWPFRun r : runs) {
            int pos = r.getTextPosition();
            if (r.getText(pos) != null) {
                sb.append(r.getText(pos));
            }
        }
        return sb;
    }
    @Override
    public void deletePlaceholdersByColor(XWPFTableCell xwpfTableCell, String colorToDelete) {
        if(xwpfTableCell == null){
            log.warn("IN deletePlaceholdersByColor xwpfTableCell is null");
            return;
        }
        if(colorToDelete == null || colorToDelete.isBlank()){
            log.warn("IN deletePlaceholdersByColor colorToDelete is null or blank");
        }
        String color;
        for (XWPFParagraph paragraph : xwpfTableCell.getParagraphs()) {
            for (XWPFRun r : paragraph.getRuns()) {
                color = r.getColor();
                if (color != null && color.equals(colorToDelete)) {
                    r.setText("", 0);
                }
            }
        }
    }
    @Override
    public void replacePlaceholdersInParagraphsFromData(@NonNull List<XWPFParagraph> paragraphs, @NonNull JsonNode data, @NonNull String regexp) {
        if(paragraphs.isEmpty()) {
            log.warn("In replacePlaceholdersInParagraphs paragraphs is empty");
        }

        Pattern pattern = Pattern.compile(regexp);
        String placeholder;
        String valueInPlaceholder;
        JsonNode valueFromData;
        String text;

        for (XWPFParagraph p : paragraphs) {

            StringBuilder textFromAllRunsStringBuilder = getTextFromAllRuns(p.getRuns());

            if (textFromAllRunsStringBuilder.length() > 0) {
                //Find placeholders
                Matcher matcher = pattern.matcher(textFromAllRunsStringBuilder.toString());
                //While placeholders found (Count of placeholders)
                while (matcher.find()) {
                    valueFromData = null;
                    removeAllRuns(p);

                    placeholder = matcher.group(0);
                    valueInPlaceholder = matcher.group(1);

                    if(data.get("data")!=null){
                        valueFromData = data.get("data").get(valueInPlaceholder);
                    }
                    if(valueFromData == null && data.get("META")!=null){
                        valueFromData = data.get("META").get(valueInPlaceholder);
                    }
                    if(valueFromData == null && data.get("computed")!=null){
                        valueFromData = data.get("computed").get(valueInPlaceholder);
                    }
                    if(valueFromData == null && data.get("tables")!=null){
                        valueFromData = data.get("tables").get(valueInPlaceholder);
                    }
                    if(valueFromData == null){
                        valueFromData = data.get(valueInPlaceholder);
                    }

                    //replace placeholder
                    if (valueFromData != null) {
                        log.info("IN replacePlaceholdersInParagraphs placeholderValue: {} replaced by: {} ", valueInPlaceholder,valueFromData);
                        text = textFromAllRunsStringBuilder.toString().replace(placeholder, valueFromData.asText());
                    } else {
                        log.warn("IN replacePlaceholdersInParagraphs placeholderValue {} not found in data: {}",valueInPlaceholder,data);
                        text = textFromAllRunsStringBuilder.toString().replace(placeholder, "");
                    }

                    //clear text
                    textFromAllRunsStringBuilder.delete(0, textFromAllRunsStringBuilder.length());
                    //set text with replaced placeholder and another text
                    textFromAllRunsStringBuilder.append(text);
                    // Add new run with updated text
                    XWPFRun run = p.createRun();
                    run.setText(text);
                }
            }
        }
    }


}
