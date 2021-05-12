package com.beta.backend.service.docx.report.impl;

import com.beta.backend.service.docx.docxCommon.XWPFDocxCommonService;
import com.beta.backend.service.docx.report.ReportDocxService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReportDocxServiceImpl implements ReportDocxService {
    @Value("${doc.report.pathToReportTemplate}")
    private String pathToReportTemplate;
    @Value("${doc.report.placeholderRegexp}")
    private String sourceRegexp;
    @Value("${doc.report.placeholderColor}")
    private String placeholderColor;

    private final XWPFDocxCommonService XWPFDocxCommonService;


    private void fillTable(@NonNull XWPFTable table, @NonNull Map<Integer, String> colField, @NonNull JsonNode rowsData) {
        if(rowsData.isEmpty()){
            log.warn("IN fillTable rowsData is empty");
        }
        for (JsonNode rowData : rowsData) {
            XWPFTableRow row = table.createRow();
            for (int cellInd = 0; cellInd < row.getTableCells().size(); cellInd++) {
                if (colField.containsKey(cellInd)) {
                    JsonNode value = rowData.get(colField.get(cellInd));
                    if (value != null) {
                        row.getCell(cellInd).setText(value.asText());
                    } else {
                        log.warn("IN fillTable rowdata: {} not contains key: {}",rowData,colField.get(cellInd));
                        row.getCell(cellInd).setText("");
                    }
                } else {
                    row.getCell(cellInd).setText("");
                }
            }
        }
    }

    private void replacePlaceholdersInTablesAndFillTableFromData(@NonNull List<XWPFTable> tables,@NonNull JsonNode data,@NonNull String regexp) {
        if(regexp.isBlank()){
            log.warn("IN replacePlaceholdersInTablesAndFillTableFromData regexp is blank");
        }

        if (tables.isEmpty()) {
            log.warn("IN replacePlaceholdersInTablesAndFillTableFromData tables is empty");
        }

        Pattern pattern = Pattern.compile(regexp);

        for (XWPFTable table : tables) {
            if (table.getRows().size() != 2) {
                continue;
            }
            //find tablename from first row first cell
            Matcher matcher = pattern.matcher(table.getRow(0).getCell(0).getText());

            if (matcher.find()) {

                String tableName = matcher.group(1);
                //method to get Columns metadata (ColumnInd, filedName in <tablename>.data) And delete placeholder in tablecell by row
                Map<Integer, String> columnIndexAndFieldName = parsRow(table.getRow(1), regexp);

                JsonNode rowsData = data.get("tables").get(tableName);

                if (rowsData != null) {
                    if (!rowsData.isArray()) {
                        log.error("IN replacePlaceholdersInTablesAndFillTableFromData type of rowsData is {}, but must be array", rowsData.getNodeType());
                        throw new MissingFormatArgumentException(String.format("IN replacePlaceholdersInTablesAndFillTableFromData rowsData is %s, but must be array", rowsData.getNodeType().toString()));
                    }
                    //method to fill table (addRow) by columnIndexAndFieldName(colInd, fieldName in rowsData)
                    fillTable(table, columnIndexAndFieldName, rowsData);
                }else{
                    log.warn("IN replacePlaceholdersInTablesAndFillTableFromData data.data:{} not contains tablename: {}",data,tableName);
                }
                table.removeRow(0);
            }
        }
    }

    // handler to get ColumnMetadata from row and replace placeholder
    private Map<Integer, String> parsRow(XWPFTableRow row, String pattern) {
        Map<Integer, String> res = new HashMap<>();
        if (row == null) {
            return res;
        }
        for (int i = 0; i < row.getTableCells().size(); i++) {
            String filedName = parsCellAndDeletePlaceholder(row.getCell(i), pattern);
            if (filedName != null) {
                res.put(i, filedName);
            }
        }
        return res;
    }

    private String parsCellAndDeletePlaceholder(XWPFTableCell xwpfTableCell, String pattern) {
        if (xwpfTableCell == null || pattern == null) {
            return null;
        }
        Pattern regexp = Pattern.compile(pattern);
        Matcher matcher = regexp.matcher(xwpfTableCell.getText());
        // if found placeholder in cell
        if (matcher.find()) {
            //placeholder
            //String replace = matcher.group(0);

            //value in placeholder
            String fieldName = matcher.group(1);

            XWPFDocxCommonService.deletePlaceholdersByColor(xwpfTableCell,placeholderColor);
            return fieldName;
        }
        return null;
    }

    //Handler to replace data
    private void parsReportTemplate(@NonNull XWPFDocument reportTemplate,@NonNull JsonNode data,@NonNull String regexp) {

        replacePlaceholdersInTablesAndFillTableFromData(reportTemplate.getTables(), data,regexp);
        XWPFDocxCommonService.replacePlaceholdersInParagraphsFromData(reportTemplate.getParagraphs(), data,regexp);
    }
    private ByteArrayResource getInputStreamResourceFromXWPFDoc(@NonNull XWPFDocument doc) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            doc.write(byteArrayOutputStream);
            byteArrayOutputStream.flush();
            return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        }
    }
    @Override
    public ByteArrayResource getReportDocxInputStreamResourceByReportData(@NonNull JsonNode data){
        try(FileInputStream fos = new FileInputStream(pathToReportTemplate)) {
            if(fos.available() <=0) throw new FileSystemException("IN getReportDocxInputStreamResourceByReport fos.available() <=0");
            XWPFDocument reportTemplate = new XWPFDocument(fos);

            parsReportTemplate(reportTemplate,data, String.valueOf(sourceRegexp));

            return getInputStreamResourceFromXWPFDoc(reportTemplate);
        } catch (Exception e) {
            log.error("fatal error",e);
            throw new RuntimeException(e);
        }

    }

}
