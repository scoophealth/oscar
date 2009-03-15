<%@page import="org.apache.poi.hssf.usermodel.HSSFRow,org.apache.poi.hssf.usermodel.HSSFSheet,org.apache.poi.hssf.usermodel.HSSFWorkbook,com.Ostermiller.util.CSVParser"%><%

    String csv = (String) session.getAttribute("clinicalReportCSV");
    String action = request.getParameter("getCSV");
        if (action != null) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"oscarReport.csv\"");
            try {
                response.getWriter().write(csv);
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
        action = request.getParameter("getXLS");
        if (action != null) {
            System.out.println("Generating Spread Sheet file for the 'report by template' module ..");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"oscarReport.xls\"");
            String[][] data = CSVParser.parse(csv);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("OSCAR_Report");
            for (int x=0; x<data.length; x++) {
                HSSFRow row = sheet.createRow((short)x);
                for (int y=0; y<data[x].length; y++) {
                    try{
                       double d = Double.parseDouble(data[x][y]);
                        row.createCell((short)y).setCellValue(d);
                    }catch(Exception e){
                       row.createCell((short)y).setCellValue(data[x][y]);
                    }
                }
            }
            try {    
                wb.write(response.getOutputStream());
            } catch(Exception e) {
                e.printStackTrace();   
            }
            
        }
       

%>