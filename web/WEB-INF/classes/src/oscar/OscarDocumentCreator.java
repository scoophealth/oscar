package oscar;

import java.io.*;
import java.sql.*;
import java.util.*;

import dori.jasper.engine.*;

public class OscarDocumentCreator {
  public static final String PDF = "pdf";
  public static final String CVS = "cvs";
  public OscarDocumentCreator() {
  }

  public void fillDocumentStream(HashMap parameters, OutputStream sos,
                                 String docType, InputStream xmlDesign,
                                 Connection con) {
    try {
      JasperReport jasperReport = null;
      JasperPrint print = null;
      jasperReport = JasperCompileManager.compileReport(
          xmlDesign);
      print = JasperFillManager.fillReport(jasperReport, parameters,
                                           con);
      if (docType.equals(this.PDF)) {
        JasperExportManager.exportReportToPdfStream(print, sos);
      }
      else if (docType.equals(this.CVS)) {

      }
    }
    catch (JRException ex) {
      ex.printStackTrace();
    }

  }

}
