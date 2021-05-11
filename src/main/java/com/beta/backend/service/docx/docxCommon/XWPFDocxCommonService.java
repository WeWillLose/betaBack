package com.beta.backend.service.docx.docxCommon;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;

import java.util.List;

public interface XWPFDocxCommonService {
    void removeAllRuns(XWPFParagraph p);

    StringBuilder getTextFromAllRuns(List<XWPFRun> runs);

    void deletePlaceholdersByColor(XWPFTableCell xwpfTableCell, String colorToDelete);

    void replacePlaceholdersInParagraphsFromData(List<XWPFParagraph> paragraphs, JsonNode data, String regexp);
}
