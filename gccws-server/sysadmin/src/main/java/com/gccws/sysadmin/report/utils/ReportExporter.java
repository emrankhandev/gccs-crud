package com.gccws.sysadmin.report.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @Author		Md. Mizanur Rahman
 * @Since		August 28, 2022
 * @version		1.0.0
 */
@Component
public class ReportExporter {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ReportExporter.class);
	
	@Autowired
	DataSource dataSource;
	
	
	public byte[] uploadPDFReport(Integer id, JasperReport report, Map<String, Object> parameters, HttpServletResponse response) throws JRException, SQLException, IOException {
		byte[] bytes = null;
		Connection connection = dataSource.getConnection();
		try {
			  JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);
		      var input = new SimpleExporterInput(print);
		      try (var byteArray = new ByteArrayOutputStream()) {
			        var output = new SimpleOutputStreamExporterOutput(byteArray);
			        var exporter = new JRPdfExporter();
			        exporter.setExporterInput(input);
			        exporter.setExporterOutput(output);
			        exporter.exportReport();
			        bytes = byteArray.toByteArray();
			        output.close();
		      } catch (IOException e) {
		      }
		      return bytes;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }finally {
			connection.close();
		}
	    return bytes;
	}
	
	
	public byte[] uploadXlsxReport(Integer id, JasperReport report, Map<String, Object> parameters, HttpServletResponse response) throws JRException, SQLException, IOException {
		byte[] bytes = null;
		Connection connection = dataSource.getConnection();
	    try {
    	  JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);
	      var input = new SimpleExporterInput(print);
	      try (var byteArray = new ByteArrayOutputStream()) {
	        var output = new SimpleOutputStreamExporterOutput(byteArray);
	        var exporter = new JRXlsxExporter();
	        exporter.setExporterInput(input);
	        exporter.setExporterOutput(output);
	        exporter.exportReport();
	        bytes = byteArray.toByteArray();
	        output.close();
	      } catch (IOException e) {
	      }
	      return bytes;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }finally {
			connection.close();
		}
	    return bytes;
	}
	
	
	
	public byte[] uploadRTF(Integer id, JasperReport report, Map<String, Object> parameters, HttpServletResponse response) throws JRException, SQLException, IOException {
		byte[] bytes = null;
		Connection connection = dataSource.getConnection();
	    try {
	    	JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);
	      var input = new SimpleExporterInput(print);
	      try (var byteArray = new ByteArrayOutputStream()) {
	        var output = new SimpleWriterExporterOutput(byteArray);
	        var exporter = new JRRtfExporter();
	        exporter.setExporterInput(input);
	        exporter.setExporterOutput(output);
	        exporter.exportReport();
	        bytes = byteArray.toByteArray();
	        output.close();
	      } catch (IOException e) {
	      }
	      return bytes;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }finally {
			connection.close();
		}
	    return bytes;
	}
}
