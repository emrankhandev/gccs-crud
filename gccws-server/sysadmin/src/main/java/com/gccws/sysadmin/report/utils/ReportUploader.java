package com.gccws.sysadmin.report.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author    Md. Tawhidul Islam
 * @Since     September 28, 2022
 * @version   1.0.0
 */

@Component
public class ReportUploader {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ReportUploader.class);

    @Autowired
    Environment env;

    public Map<String, String> uploadMasterReportToServer(MultipartFile file, String fileName) throws IOException {

        Map<String, String> response = new HashMap<>();

        //dir
        String dir = env.getProperty("report.source.dir");
        dir = dir.replace("\\", "/");

        //String dir = getImageDir();

        String englishFileName = file.getOriginalFilename();

        if(!ObjectUtils.isEmpty(fileName)) {
            englishFileName = fileName + ".jrxml";
        }

        //check file dir
        Path root = Paths.get(dir);
        if (!Files.exists(root)) {
            Files.createDirectories(Paths.get(dir));
        }

        //chenge file name
        byte[] bytes = file.getBytes();
        String insPathEnglish = dir + englishFileName;

        //save to dir
        Files.write(Paths.get(insPathEnglish), bytes);

        response.put("fileNameEng", englishFileName);
        response.put("fileNameJasperEng", fileName+".jasper");
        response.put("fileLocation", dir);

        return response;
    }


    public Map<String, String> uploadSubReportToServer(MultipartFile file, String fileName) throws IOException {

        Map<String, String> response = new HashMap<>();

        //dir
        String dir = env.getProperty("report.source.subreport.dir");
        dir = dir.replace("\\", "/");

        //String dir = getImageDir();

        String englishFileName = file.getOriginalFilename();

        if(!ObjectUtils.isEmpty(fileName)) {
            englishFileName = fileName + ".jrxml";
        }

        //check file dir
        Path root = Paths.get(dir);
        if (!Files.exists(root)) {
            Files.createDirectories(Paths.get(dir));
        }

        //chenge file name
        byte[] bytes = file.getBytes();
        String insPathEnglish = dir + englishFileName;

        //save to dir
        Files.write(Paths.get(insPathEnglish), bytes);

        response.put("fileNameEng", englishFileName);
        response.put("fileNameJasperEng", fileName+".jasper");
        response.put("fileLocation", dir);

        return response;
    }


    public Boolean deleteMasterReportFromServer(String path) throws IOException {
        Boolean response = false;
        Path filePath = Paths.get(path);
        Files.delete(filePath);
        response = true;
        return response;
    }

    public Boolean deleteSubReportFromServer(String path) throws IOException {
        Boolean response = false;
        Path filePath = Paths.get(path);
        Files.delete(filePath);
        response = true;
        return response;
    }



}
