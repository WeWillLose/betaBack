package com.beta.backend.utils;

import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.UUID;

@Component
public  class FileNameUtils {
    private FileNameUtils(){}

    private final static String fileExtension = ".docx";


    public  static String getReportFileNameOrDefault(@Nullable String name){
        if(name == null || name.isBlank()){
            return String.format("report_%s"+fileExtension, UUID.randomUUID().toString());
        }else{
            String res = name.replace(" ","_");
            return String.format("report_%s"+fileExtension,res);
        }
    }


    public static String getScoreListFileNameOrDefault(@Nullable String name){
        if(name == null || name.isBlank()){
            return String.format("scoreList_%s"+fileExtension, UUID.randomUUID().toString());
        }else{
            String res = name.replace(" ","_");
            return String.format("scoreList_%s"+fileExtension,res);
        }
    }

    public static String generateRandomNameIfEmpty(@Nullable String name){
        if(name ==null || name.isBlank()){
            return String.format("%s",UUID.randomUUID().toString());
        }
        return name;
    }
}
