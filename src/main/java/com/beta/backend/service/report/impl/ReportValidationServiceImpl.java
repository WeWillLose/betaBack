package com.beta.backend.service.report.impl;


import com.beta.backend.domain.model.Report;
import com.beta.backend.service.report.ReportValidationService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportValidationServiceImpl implements ReportValidationService {
    @Override
    public List<String> validateReport(Report report){
        if(report == null) return new ArrayList<>(){{add("Отчет пуст");}};
        return validateReportData(report.getData());
    }
    @Override
    public List<String> validateReportData(JsonNode data){
        ArrayList<String> errors = new ArrayList<>();
        if(data == null){
            errors.add("Данные отчета пусты");
            return errors;
        }

        if(data.get("META") ==null){
            log.error("IN saveReport data doesnt contains key META, data: {}",data);
            errors.add("Данные не содержат ключ META");
        }else if(!data.get("META").isObject()){
            log.error("IN saveReport data.META is not object, data: {}, type: {}",data,data.getNodeType());
            errors.add("Данные ключа META должны быть обьектом");
        }

        if(data.get("computed") ==null){
            log.error("IN saveReport data doesnt contains key computed, data: {}",data);
            errors.add("Данные не содержат ключ computed");
        }else if(!data.get("computed").isObject()){
            log.error("IN saveReport data.computed is not object, data: {}",data);
            errors.add("Данные ключа computed должны быть обьектом");
        }

        if(data.get("tables") ==null){
            log.error("IN saveReport data doesnt contains key data, data: {}",data);
            errors.add("Данные не содержат ключ data");
        }else if(!data.get("tables").isObject()){
            log.error("IN saveReport data.data is not object, data: {}",data);
            errors.add("Данные ключа data должны быть обьектом");
        }
        return errors;
    }
}
