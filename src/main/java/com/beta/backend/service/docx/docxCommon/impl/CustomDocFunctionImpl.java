package com.beta.backend.service.docx.docxCommon.impl;


import com.beta.backend.service.docx.docxCommon.CustomDocFunction;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomDocFunctionImpl implements CustomDocFunction {
    @Value("${doc.func.sum.validateRegexp}")
    private String validateSumRegexp;
    @Value("${doc.func.sum.prefix}")
    private String sumPrefix;

    @Value("${doc.func.isSmth.validateRegexp}")
    private String validateIsSmthRegexp;

    @Value("${doc.func.isSmth.prefix}")
    private String isSmthPrefix;

    @Value("${doc.func.argsSeparator}")
    private String argsSeparator;

    @Value("${doc.func.tableFiledSeparator}")
    private String tableFiledSeparator;

    private List<String> getArgs(String expression,String prefix,String argsSeparator){
        if(expression == null || expression.isBlank() || prefix ==null || argsSeparator == null){
            return new ArrayList<String>();
        }
        return Arrays.stream(expression.replace(prefix, "").replace("(", "").replace(")", "").split(argsSeparator)).collect(Collectors.toList());
    }

    private List<String> splitArgBySeparatorAndDeleteBlackOrEmptyValue(String arg,String argSeparator){
        if(arg == null || arg.isBlank() || argSeparator ==null ){
            return new ArrayList<String>();
        }
        return Arrays.stream(arg.split(tableFiledSeparator)).filter(t->t!=null&&!t.isBlank()).collect(Collectors.toList());
    }

    private boolean isSmth(JsonNode value){
        if(value ==null || value.isEmpty()) return false;
        if(value.asText().isBlank()) return false;
        return true;
    }

    @Override
    public String getSumAsTextByExpressionFromData(@Nullable String expression, @Nullable JsonNode data){
        if(expression == null || data == null || data.isEmpty()){
            return "0";
        }
        List<String> args = getArgs(expression, sumPrefix,argsSeparator);
        if(args.size()<=0) return "0";
        List<String> tableFieldName;
        double sum = 0;
        for(String arg : args){
            tableFieldName = splitArgBySeparatorAndDeleteBlackOrEmptyValue(arg,tableFiledSeparator);
            if(tableFieldName.size() >=3 || tableFieldName.size()<=0){
                throw new RuntimeException(String.format("IN getSumByPlaceholderFromData tableFieldName.length is %s",tableFieldName.size()));
            }
            if(tableFieldName.size() == 2){
                if(data.get(tableFieldName.get(0))!=null){
                    if(data.get(tableFieldName.get(0)).isArray()){
                        for(JsonNode obj : data.get(tableFieldName.get(0))){
                            if(obj!=null && obj.get(tableFieldName.get(1))!=null){
                                sum+= obj.get(tableFieldName.get(1)).asDouble(0);
                            }
                        }
                    }else {
                        if(data.get(tableFieldName.get(0)).get(tableFieldName.get(1)) !=null)
                            sum+= data.get(tableFieldName.get(0)).get(tableFieldName.get(1)).asDouble(0);
                    }
                }
            }else{
                if(data.get(tableFieldName.get(0))!=null){
                    sum+= data.get(tableFieldName.get(0)).asDouble(0);
                }
            }
        }
        return String.valueOf(sum);
    }

    @Override
    public boolean isSmthByExpressionFromData(String expression, JsonNode data) {
        if(expression == null || data == null || data.isEmpty()){
            return false;
        }

        List<String> args = getArgs(expression, isSmthPrefix,argsSeparator);

        if(args.size()<=0) return false;

        List<String> tableFieldName;

        boolean res = true;
        for(String arg : args){
            tableFieldName = splitArgBySeparatorAndDeleteBlackOrEmptyValue(arg,tableFiledSeparator);;
            if(tableFieldName.size() >=3 || tableFieldName.size()<=0){
                throw new RuntimeException(String.format("IN isSmthByExpressionFromData tableFieldName.length is %s",tableFieldName.size()));
            }
            if(tableFieldName.size() == 2){
                if(data.get(tableFieldName.get(0))!=null){
                    if(data.get(tableFieldName.get(0)).isArray()){
                        for(JsonNode obj : data.get(tableFieldName.get(0))){
                            if(obj!=null && obj.get(tableFieldName.get(1))!=null){
                                res &= !isSmth(obj.get(tableFieldName.get(1)));
                            }
                        }
                    }else {
                        if(data.get(tableFieldName.get(0)).get(tableFieldName.get(1)) !=null)
                            res &= !isSmth(data.get(tableFieldName.get(0)).get(tableFieldName.get(1)));
                    }
                }
            }else{
                if(data.get(tableFieldName.get(0))!=null){
                    res &= !isSmth(data.get(tableFieldName.get(0)));
                }
            }
        }
        return res;
    }
}
