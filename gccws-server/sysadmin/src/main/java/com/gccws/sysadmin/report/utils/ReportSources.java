package com.gccws.sysadmin.report.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Author    Md. Tawhidul Islam
 * @Since     September 28, 2022
 * @version   1.0.0
 */
@Component
public class ReportSources {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ReportSources.class);

    @Autowired
    Environment env;

    public String getSourceReport(String filename) {
        return String.format("%s%s", env.getProperty("report.source.dir"), filename);
    }


    public String getSourceSubReport(String filename) {
        return String.format("%s%s", env.getProperty("report.source.subreport.dir"), filename);
    }

    public String getCompiledReport(String filename) {
        return String.format("%s%s", env.getProperty("report.compile.dir"), filename);
    }

    public String getImage(String image) {
        return String.format("%s%s", env.getProperty("report.image.dir"), image);
    }

    public String getOutputReport(String filename) {
        return String.format("%s%s", env.getProperty("report.output.dir"), filename);
    }

}
