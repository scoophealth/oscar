package oscar;

import java.io.*;
import java.sql.*;
import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;

public class OscarDocumentCreator {
  public static final String PDF = "pdf";
  public static final String CSV = "csv";

  public OscarDocumentCreator() {

  }

  public FileInputStream getDocumentStream(String path) {
    FileInputStream reportInstream = null;
    try {
      reportInstream = new FileInputStream(path);
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    return reportInstream;
  }

  public void fillDocumentStream(HashMap parameters, OutputStream sos,
                                 String docType, InputStream xmlDesign,
                                 Object dataSrc) {
    try {
      JasperReport jasperReport = null;
      JasperPrint print = null;
      jasperReport = JasperCompileManager.compileReport(
          xmlDesign);
      if (dataSrc instanceof List) {
        JRDataSource ds = new JRBeanCollectionDataSource( (List) dataSrc);
        print = JasperFillManager.fillReport(jasperReport, parameters,
                                             ds);
      }
      else if (dataSrc instanceof java.sql.Connection) {
        print = JasperFillManager.fillReport(jasperReport, parameters,
                                             (Connection) dataSrc);
      }
      else if (dataSrc instanceof ResultSet) {
        JRDataSource ds = new JRResultSetDataSource( (ResultSet) dataSrc);
        print = JasperFillManager.fillReport(jasperReport, parameters,
                                             ds);
      }
      if (docType.equals(this.PDF)) {
        JasperExportManager.exportReportToPdfStream(print, sos);
      }
      else if (docType.equals(this.CSV)) {
        this.exportReportToCSVStream(print, sos);

      }

      //System.err.println(sos);
    }
    catch (JRException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Fills a servletoutout stream with data from a JasperReport
   * @param jasperPrint JasperPrint
   * @param sos ServletOutputStream
   * @throws JRException
   */
  private void exportReportToCSVStream(JasperPrint jasperPrint,
                                       OutputStream sos) throws
      JRException {
    JRCsvExporter exp = new JRCsvExporter();
    exp.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
    exp.setParameter(JRExporterParameter.OUTPUT_STREAM, sos);
    exp.exportReport();
  }

}
