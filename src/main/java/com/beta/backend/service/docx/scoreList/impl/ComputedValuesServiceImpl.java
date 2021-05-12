package com.beta.backend.service.docx.scoreList.impl;

import com.beta.backend.service.docx.scoreList.ComputedValuesService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class ComputedValuesServiceImpl implements ComputedValuesService {

    @Override
    public Double getScoreSum(JsonNode dataRows) {
        double score = 0;
        if(dataRows == null){
            return score;
        }
        for (JsonNode datum : dataRows) {
            if (datum.get("score") != null) {
                try {
                    score += datum.get("score").asDouble();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return score;
    }

    @Override
    public Double getScoreSum(JsonNode dataRows, int max) {
        double score = 0;
        if(dataRows == null){
            return score;
        }
        for (JsonNode datum : dataRows) {
            if (datum.get("score") != null) {
                try {
                    score += datum.get("score").asDouble();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return score>max?max:score;
    }

    @Override
    public Double getScoreSum(JsonNode dataRows, int rowInd, Integer max) {
        if(dataRows == null){
            return 0D;
        }
        if (dataRows.get(rowInd) != null && dataRows.get(rowInd).get("score") !=null) {
            try {
                double score = dataRows.get(rowInd).get("score").asDouble(0);
                if(max!=null)
                    return score >max?max:score;
                else return score;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return 0D;
    }
    @Override
    public double getProcent1(double score){
        if(score>=11 && score<=15){
            return 15;
        }
        if(score>=6 && score<=10){
            return 10;
        }
        if(score>=4 && score<=5){
            return 5;
        }
        return 0;
    }


    @Override
    public double getProcent2(double score){
        if(score>40){
            return 30;
        }
        if(score>=31 && score<=40){
            return 20;
        }
        if(score>=21 && score<=30){
            return 15;
        }
        if(score>=11 && score<=20){
            return 10;
        }
        if(score<=10){
            return 5;
        }
        return -1;
    }
    @Override
    public String IsSmth(JsonNode data){
        if(data == null || data.isEmpty()){
            return "-";
        }
        if(data.get(0) == null || data.get(0).isEmpty()){
            return "-";
        }
        return "+";
    }
}
