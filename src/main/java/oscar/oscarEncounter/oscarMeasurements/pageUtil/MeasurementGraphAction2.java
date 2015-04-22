/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.awt.Color;
import java.awt.Paint;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.XYDataset;
import org.oscarehr.PMmodule.utility.UtilDateUtilities;
import org.oscarehr.common.dao.MeasurementsExtDao;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;
import oscar.oscarLab.ca.on.CommonLabTestValues;

/**
 *
 * @author jaygallagher
 */
public class MeasurementGraphAction2 extends Action {

    private static Logger log = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("In MeasurementGraphAction2");
        String userrole = (String) request.getSession().getAttribute("userrole");
        if (userrole == null) {
            response.sendRedirect("../logout.jsp");
        }

        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_measurement", "r", null)) {
			throw new SecurityException("missing required security object (_measurement)");
		}
        

        Integer demographicNo = Integer.valueOf(request.getParameter("demographic_no"));
        String typeIdName = request.getParameter("type");
        String typeIdName2 = request.getParameter("type2");

        String patientName = oscar.oscarDemographic.data.DemographicNameAgeString.getInstance().getNameAgeString(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);
        String chartTitle = "Data Graph for " + patientName;
        int width = 800;
        int height = 400;

        String method = request.getParameter("method");

        log.debug("Creating graph for demo " + demographicNo + " type1 :" + typeIdName + " type2 :" + typeIdName2);
        JFreeChart chart = null;
        if (method == null) {
            log.debug("Calling DefaultChart");
            chart = defaultChart(demographicNo, typeIdName, typeIdName2, patientName, chartTitle);
        }else if (method.equals("inclRef")) {
            chart = referenceRangeChart(demographicNo, typeIdName, typeIdName2, patientName, chartTitle);

        }else if (method.equals("rxincl")) {
            chart = rxAndLabChart(demographicNo, typeIdName, typeIdName2, patientName, chartTitle);
        }else if (method.equals("lab")) {
            chart = labChart(demographicNo, typeIdName, typeIdName2, patientName, chartTitle);
        }else if (method.equals("labRef")) {
            chart = labChartRef(demographicNo, typeIdName, typeIdName2, patientName, chartTitle);
        }else if (method.equals("actualLab")){
            String labType    = request.getParameter("labType");
            String identifier = request.getParameter("identifier");
            String testName   = request.getParameter("testName");
            String drugs[] = request.getParameterValues("drug");
            if (drugs == null){
               chart =  actualLabChartRef( demographicNo,  labType,  identifier, testName,  patientName,  chartTitle) ;
            }else{
                chart = actualLabChartRefPlusMeds( demographicNo,  labType,  identifier, testName,  patientName,  chartTitle,drugs) ;
            }
        }else if(method.equals("ChartMeds")){
            String drugs[] = request.getParameterValues("drug");
            chart =ChartMeds( demographicNo, patientName,  chartTitle, drugs);
            if (drugs != null && drugs.length >10){
                height = (drugs.length * 30);
            }
        }else{
            chart = defaultChart(demographicNo, typeIdName, typeIdName2, patientName, chartTitle);
        }


        response.setContentType("image/png");
        OutputStream o = response.getOutputStream();
        ChartUtilities.writeChartAsPNG(o, chart, width, height);
        o.close();
        return null;
    }

    ArrayList<EctMeasurementsDataBean> getList(Integer demographicNo, String typeIdName) {
        EctMeasurementsDataBeanHandler ectMeasure = new EctMeasurementsDataBeanHandler(demographicNo, typeIdName);
        Collection<EctMeasurementsDataBean> dataVector = ectMeasure.getMeasurementsDataVector();
        ArrayList<EctMeasurementsDataBean> list = new ArrayList<EctMeasurementsDataBean>(dataVector);
        return list;
    }

    private static XYTaskDataset getDrugDataSet(Integer demographicId,String[] dins){

        TaskSeriesCollection datasetDrug = new TaskSeriesCollection();
        oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();


        for(String din:dins){
             oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr =  prescriptData.getPrescriptionScriptsByPatientRegionalIdentifier(demographicId,din);
             TaskSeries ts  = new TaskSeries(arr[0].getBrandName());
             for(oscar.oscarRx.data.RxPrescriptionData.Prescription pres:arr){
                 ts.add(new Task(pres.getBrandName(),pres.getRxDate(),pres.getEndDate()));
             }
             datasetDrug.add(ts);
        }

        XYTaskDataset dataset = new XYTaskDataset(datasetDrug);
            dataset.setTransposed(true);
            dataset.setSeriesWidth(0.6);
        return dataset;
    }

    private static String[] getDrugSymbol(Integer demographic,String[] dins){
        String[] ret = new String[dins.length];
        ArrayList<String> list = new ArrayList<String>();
        oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
        for(String din:dins){
             oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr =  prescriptData.getPrescriptionScriptsByPatientRegionalIdentifier(demographic,din);
             list.add( arr[0].getBrandName() );

        }
        ret = list.toArray( new String[list.size()] );
        return ret;
    }






    JFreeChart referenceRangeChart(Integer demographicNo, String typeIdName, String typeIdName2, String patientName, String chartTitle) {
             org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();

        ArrayList<EctMeasurementsDataBean> list = getList(demographicNo, typeIdName);
        ArrayList<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();

        if (typeIdName.equals("BP")) {
            log.debug("Using BP LOGIC FOR type 1 ");
            EctMeasurementsDataBean sampleLine = list.get(0);
            TimeSeries systolic = new TimeSeries("Systolic", Day.class);
            TimeSeries diastolic = new TimeSeries("Diastolic", Day.class);
            for (EctMeasurementsDataBean mdb : list) { // dataVector) {
                String[] str = mdb.getDataField().split("/");

                systolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[0]));
                diastolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[1]));
            }
            dataset.addSeries(diastolic);
            dataset.addSeries(systolic);


        } else {
            log.debug("Not Using BP LOGIC FOR type 1 ");
            // get the name from the TimeSeries
            EctMeasurementsDataBean sampleLine = list.get(0);
            String typeLegendName = sampleLine.getTypeDisplayName();
            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (EctMeasurementsDataBean mdb : list) { //dataVector) {
                newSeries.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(mdb.getDataField()));

                try{
                    Hashtable h = getMeasurementsExt( mdb.getId());
                    if (h != null && h.containsKey("minimum")){
                        String min = (String) h.get("minimum");
                        String max = (String) h.get("maximum");
                        double open = Double.parseDouble(min.trim());
                        double high = Double.parseDouble(max.trim());
                        double low = Double.parseDouble(min.trim());
                        double close = Double.parseDouble(max.trim());
                        double volume = 1045;
                        dataItems.add(new OHLCDataItem(mdb.getDateObservedAsDate(), open, high, low, close, volume));
                    }
                }catch(Exception et){
                	MiscUtils.getLogger().error("Error", et);
                }

            }
            dataset.addSeries(newSeries);
        }

        OHLCDataItem[] ohlc = dataItems.toArray(new OHLCDataItem[dataItems.size()]);
        JFreeChart chart = ChartFactory.createHighLowChart("HighLowChartDemo2","Time","Value",new DefaultOHLCDataset("DREFERENCE RANGE", ohlc),true);
        XYPlot plot = (XYPlot) chart.getPlot();

//        HighLowRenderer renderer = (HighLowRenderer) plot.getRenderer();
//        renderer.
//        renderer.setOpenTickPaint(Color.green);
//        renderer.setCloseTickPaint(Color.black);

        plot.setDataset(1, dataset);

        plot.getDomainAxis().setAutoRange(true);


        log.debug("LEN " + plot.getDomainAxis().getLowerBound() + " ddd " + plot.getDomainAxis().getUpperMargin() + " eee " + plot.getDomainAxis().getLowerMargin());
            //plot.getDomainAxis().setUpperMargin(plot.getDomainAxis().getUpperMargin()*6);
            //plot.getDomainAxis().setLowerMargin(plot.getDomainAxis().getLowerMargin()*6);
            // plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin()*1.7);

        plot.getDomainAxis().setUpperMargin(0.9);
        plot.getDomainAxis().setLowerMargin(0.9);
        plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin() * 4);

        ValueAxis va = plot.getRangeAxis();
        va.setAutoRange(true);
        XYItemRenderer renderer = plot.getRenderer(); //DateFormat.getInstance()
        XYItemLabelGenerator generator = new StandardXYItemLabelGenerator("{1} \n {2}", new SimpleDateFormat("yyyy.MM.dd"), new DecimalFormat("0.00"));
        renderer.setSeriesItemLabelGenerator(0, generator);//setLabelGenerator(generator);

        renderer.setBaseItemLabelsVisible(true);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainCrosshairPaint(Color.GRAY);



        if (renderer instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer) renderer;
            rend.setBaseShapesVisible(true);
            rend.setBaseShapesFilled(true);
        }


        plot.setRenderer(renderer);
        chart.setBackgroundPaint(Color.white);
        return chart;
    }

    JFreeChart rxAndLabChart(Integer demographicNo, String typeIdName, String typeIdName2, String patientName, String chartTitle) {
        org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();

        ArrayList<EctMeasurementsDataBean> list = getList(demographicNo, typeIdName);

        String typeYAxisName = "";

        if (typeIdName.equals("BP")) {
            log.debug("Using BP LOGIC FOR type 1 ");
            EctMeasurementsDataBean sampleLine = list.get(0);
            typeYAxisName = sampleLine.getTypeDescription();
            TimeSeries systolic = new TimeSeries("Systolic", Day.class);
            TimeSeries diastolic = new TimeSeries("Diastolic", Day.class);
            for (EctMeasurementsDataBean mdb : list) { // dataVector) {
                String[] str = mdb.getDataField().split("/");

                systolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[0]));
                diastolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[1]));
            }
            dataset.addSeries(diastolic);
            dataset.addSeries(systolic);


        } else {
            log.debug("Not Using BP LOGIC FOR type 1 ");
            // get the name from the TimeSeries
            EctMeasurementsDataBean sampleLine = list.get(0);
            String typeLegendName = sampleLine.getTypeDisplayName();
            typeYAxisName = sampleLine.getTypeDescription(); // this should be the type of measurement
            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (EctMeasurementsDataBean mdb : list) { //dataVector) {
                newSeries.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(mdb.getDataField()));
            }
            dataset.addSeries(newSeries);
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, "Days", typeYAxisName, dataset, true, true, true);
        chart.setBackgroundPaint(Color.decode("#ccccff"));

            XYPlot plot = chart.getXYPlot();

            plot.getDomainAxis().setAutoRange(true);



            log.debug("LEN " + plot.getDomainAxis().getLowerBound() + " ddd " + plot.getDomainAxis().getUpperMargin() + " eee " + plot.getDomainAxis().getLowerMargin());
            //plot.getDomainAxis().setUpperMargin(plot.getDomainAxis().getUpperMargin()*6);
            //plot.getDomainAxis().setLowerMargin(plot.getDomainAxis().getLowerMargin()*6);
            // plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin()*1.7);

            plot.getDomainAxis().setUpperMargin(0.9);
            plot.getDomainAxis().setLowerMargin(0.9);
            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin() * 4);

            ValueAxis va = plot.getRangeAxis();
            va.setAutoRange(true);
            XYItemRenderer renderer = plot.getRenderer(); //DateFormat.getInstance()
            XYItemLabelGenerator generator = new StandardXYItemLabelGenerator("{1} \n {2}", new SimpleDateFormat("yyyy.MM.dd"), new DecimalFormat("0.00"));
            renderer.setSeriesItemLabelGenerator(0, generator);//setLabelGenerator(generator);

            renderer.setBaseItemLabelsVisible(true);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainCrosshairPaint(Color.GRAY);


            if (renderer instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer) renderer;
                rend.setBaseShapesVisible(true);
                rend.setBaseShapesFilled(true);
            }


            plot.setRenderer(renderer);


            ///////

            TaskSeriesCollection datasetDrug = new TaskSeriesCollection();
            TaskSeries s1 = new TaskSeries("WARFARIN");
            TaskSeries s2 = new TaskSeries("ALLOPUINOL");
            TaskSeries s3 = new TaskSeries("LIPITOR");

            s1.add(new Task("WARFARIN", UtilDateUtilities.StringToDate("2007-01-01"), UtilDateUtilities.StringToDate("2009-01-01")));
            s2.add(new Task("ALLOPUINOL", UtilDateUtilities.StringToDate("2008-01-01"), new Date()));
            s3.add(new Task("LIPITOR", UtilDateUtilities.StringToDate("2007-01-01"), UtilDateUtilities.StringToDate("2008-01-01")));


            datasetDrug.add(s1);
            datasetDrug.add(s2);
            datasetDrug.add(s3);

            XYTaskDataset dataset2 = new XYTaskDataset(datasetDrug);
            dataset2.setTransposed(true);
            dataset2.setSeriesWidth(0.6);

            DateAxis xAxis = new DateAxis("Date/Time");
            SymbolAxis yAxis = new SymbolAxis("Meds", new String[]{"WARFARIN", "ALLOPURINOL", "LIPITOR"});
            yAxis.setGridBandsVisible(false);
            XYBarRenderer xyrenderer = new XYBarRenderer();
            xyrenderer.setUseYInterval(true);
            xyrenderer.setBarPainter(new StandardXYBarPainter());

            xyrenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator("HAPPY{1} \n {2}", new SimpleDateFormat("yyyy.MM.dd"), new DecimalFormat("0.00")));
            XYPlot xyplot = new XYPlot(dataset2, xAxis, yAxis, xyrenderer);

            xyplot.getDomainAxis().setUpperMargin(0.9);
            xyplot.getDomainAxis().setLowerMargin(0.9);



            CombinedDomainXYPlot cplot = new CombinedDomainXYPlot(new DateAxis("Date/Time"));
            cplot.add(plot);
            cplot.add(xyplot);

            ///////



        chart = new JFreeChart("MED + LAB CHART", cplot);
            chart.setBackgroundPaint(Color.white);
            return chart;
    }





    JFreeChart labChart(Integer demographicNo, String typeIdName, String typeIdName2, String patientName, String chartTitle) {
        org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();
        ArrayList<EctMeasurementsDataBean> list = getList(demographicNo, typeIdName);
        String typeYAxisName = "";

        if (typeIdName.equals("BP")) {
            log.debug("Using BP LOGIC FOR type 1 ");
            EctMeasurementsDataBean sampleLine = list.get(0);
            typeYAxisName = sampleLine.getTypeDescription();
            TimeSeries systolic = new TimeSeries("Systolic", Day.class);
            TimeSeries diastolic = new TimeSeries("Diastolic", Day.class);
            for (EctMeasurementsDataBean mdb : list) { // dataVector) {
                String[] str = mdb.getDataField().split("/");

                systolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[0]));
                diastolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[1]));
            }
            dataset.addSeries(diastolic);
            dataset.addSeries(systolic);
        } else {
            log.debug("Not Using BP LOGIC FOR type 1 ");
            // get the name from the TimeSeries
            EctMeasurementsDataBean sampleLine = list.get(0);
            String typeLegendName = sampleLine.getTypeDisplayName();
            typeYAxisName = sampleLine.getTypeDescription(); // this should be the type of measurement
            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (EctMeasurementsDataBean mdb : list) { //dataVector) {
                newSeries.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(mdb.getDataField()));
            }
            dataset.addSeries(newSeries);
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, "Days", typeYAxisName, dataset, true, true, true);

            XYPlot plot = chart.getXYPlot();
            plot.getDomainAxis().setAutoRange(true);


            log.debug("LEN " + plot.getDomainAxis().getLowerBound() + " ddd " + plot.getDomainAxis().getUpperMargin() + " eee " + plot.getDomainAxis().getLowerMargin());
            plot.getDomainAxis().setUpperMargin(plot.getDomainAxis().getUpperMargin()*6);
            plot.getDomainAxis().setLowerMargin(plot.getDomainAxis().getLowerMargin()*6);
            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin()*1.7);

            plot.getDomainAxis().setUpperMargin(0.9);
            plot.getDomainAxis().setLowerMargin(0.9);
            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin() * 4);

            ValueAxis va = plot.getRangeAxis();
            va.setAutoRange(true);
            XYItemRenderer renderer = plot.getRenderer(); //DateFormat.getInstance()
            XYItemLabelGenerator generator = new StandardXYItemLabelGenerator("{1} \n {2}", new SimpleDateFormat("yyyy.MM.dd"), new DecimalFormat("0.00"));
            renderer.setSeriesItemLabelGenerator(0, generator);//setLabelGenerator(generator);

            renderer.setBaseItemLabelsVisible(true);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainCrosshairPaint(Color.GRAY);


            if (renderer instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer) renderer;
                rend.setBaseShapesVisible(true);
                rend.setBaseShapesFilled(true);
            }

            plot.setRenderer(renderer);
            return chart;
    }


////     if (!result.equals("")){
////                                    h.put("testName", testName);
////                                    h.put("abn",handler.getOBXAbnormalFlag(i, j));
////                                    h.put("result",result);
////                                    h.put("range",handler.getOBXReferenceRange(i, j));
////                                    h.put("units",handler.getOBXUnits(i, j));
////                                    String collDate = handler.getTimeStamp(i, j);
////                                    h.put("lab_no",lab_no);
////                                    h.put("collDate",collDate);
////                                    h.put("collDateDate",UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss"));
////                                    labList.add(h);
        JFreeChart actualLabChartRef(Integer demographicNo, String labType, String identifier,String testName, String patientName, String chartTitle) {
            org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();

            ArrayList<Map<String, Serializable>> list = CommonLabTestValues.findValuesForTest(labType, demographicNo, testName, identifier);

            String typeYAxisName = "";
            ArrayList<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();


            String typeLegendName = "Lab Value";
            typeYAxisName = "type Y";

            boolean nameSet = false;
            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (Map mdb : list) {
                if (!nameSet){
                    typeYAxisName = (String)mdb.get("units");
                    typeLegendName = (String) mdb.get("testName");
                    newSeries.setKey(typeLegendName);
                    nameSet = true;
                }
                newSeries.addOrUpdate(new Day((Date) mdb.get("collDateDate")), Double.parseDouble(""+mdb.get("result")));
                log.debug("RANGE "+mdb.get("range"));

                if (mdb.get("range") != null){
                    String range = (String) mdb.get("range");
                    if (range.indexOf("-") != -1){
                        String[] sp = range.split("-");
                        double open = Double.parseDouble(sp[0]);
                        double high = Double.parseDouble(sp[1]);
                        double low = Double.parseDouble(sp[0]);
                        double close = Double.parseDouble(sp[1]);
                        double volume = 1045;
                        dataItems.add(new OHLCDataItem(new Day((Date) mdb.get("collDateDate")).getStart(), open, high, low, close, volume));
                    }
                }

            }
            dataset.addSeries(newSeries);

            JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, "Days", typeYAxisName, dataset, true, true, true);

            XYPlot plot = chart.getXYPlot();
            plot.getDomainAxis().setAutoRange(true);


            log.debug("LEN " + plot.getDomainAxis().getLowerBound() + " ddd " + plot.getDomainAxis().getUpperMargin() + " eee " + plot.getDomainAxis().getLowerMargin());
            plot.getDomainAxis().setUpperMargin(plot.getDomainAxis().getUpperMargin()*6);
            plot.getDomainAxis().setLowerMargin(plot.getDomainAxis().getLowerMargin()*6);
            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin()*1.7);

            plot.getDomainAxis().setUpperMargin(0.9);
            plot.getDomainAxis().setLowerMargin(0.9);
            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin() * 4);

            ValueAxis va = plot.getRangeAxis();
            va.setAutoRange(true);
            XYItemRenderer renderer = plot.getRenderer(); //DateFormat.getInstance()
            XYItemLabelGenerator generator = new StandardXYItemLabelGenerator("{1} \n {2}", new SimpleDateFormat("yyyy.MM.dd"), new DecimalFormat("0.00"));
            renderer.setSeriesItemLabelGenerator(0, generator);//setLabelGenerator(generator);

            renderer.setBaseItemLabelsVisible(true);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainCrosshairPaint(Color.GRAY);

            if (renderer instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer) renderer;
                rend.setBaseShapesVisible(true);
                rend.setBaseShapesFilled(true);
            }

            plot.setRenderer(renderer);


            if (dataItems != null && dataItems.size() > 0){
                OHLCDataItem[] ohlc = dataItems.toArray(new OHLCDataItem[dataItems.size()]);
                XYDataset referenceRangeDataset = new DefaultOHLCDataset("Normal Reference Range", ohlc);
                plot.setDataset(1, referenceRangeDataset);
                plot.mapDatasetToRangeAxis(1, 0);
                plot.setRenderer(1,new HighLowRenderer());

            }

            return chart;
        }



        JFreeChart actualLabChartRefPlusMeds(Integer demographicNo, String labType, String identifier,String testName, String patientName, String chartTitle,String[] drugs) {
            org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();


            ArrayList<Map<String,Serializable>> list = null;
            MiscUtils.getLogger().debug(" lab type >"+labType+"< >"+labType.equals("loinc")+"<"+testName+" "+identifier);
            if (labType.equals("loinc")){
              try{

              Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
              list = CommonLabTestValues.findValuesByLoinc2(demographicNo.toString(), identifier, conn );
              MiscUtils.getLogger().debug("List ->"+list.size());
              conn.close();
              }catch(Exception ed){
            	  MiscUtils.getLogger().error("Error", ed);
              }
            }else{
               list = CommonLabTestValues.findValuesForTest(labType, demographicNo, testName, identifier);
            }
            String typeYAxisName = "";
            ArrayList<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();


            String typeLegendName = "Lab Value";
            typeYAxisName = "type Y";

            boolean nameSet = false;
            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (Map mdb : list) {
                if (!nameSet){
                    typeYAxisName = (String)mdb.get("units");
                    typeLegendName = (String) mdb.get("testName");
                    if (typeLegendName ==null){
                        typeLegendName = testName;
                    }

                    newSeries.setKey(typeLegendName);
                    nameSet = true;
                }
                newSeries.addOrUpdate(new Day((Date) mdb.get("collDateDate")), Double.parseDouble(""+mdb.get("result")));
                log.debug("RANGE "+mdb.get("range"));

                if (mdb.get("range") != null){
                    String range = (String) mdb.get("range");
                    if (range.indexOf("-") != -1){
                        String[] sp = range.split("-");
                        double open = Double.parseDouble(sp[0]);
                        double high = Double.parseDouble(sp[1]);
                        double low = Double.parseDouble(sp[0]);
                        double close = Double.parseDouble(sp[1]);
                        double volume = 1045;
                        dataItems.add(new OHLCDataItem(new Day((Date) mdb.get("collDateDate")).getStart(), open, high, low, close, volume));
                    }
                }

            }
            dataset.addSeries(newSeries);

            JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, "Days", typeYAxisName, dataset, true, true, true);

            XYPlot plot = chart.getXYPlot();
            plot.getDomainAxis().setAutoRange(true);


            log.debug("LEN " + plot.getDomainAxis().getLowerBound() + " ddd " + plot.getDomainAxis().getUpperMargin() + " eee " + plot.getDomainAxis().getLowerMargin());
            plot.getDomainAxis().setUpperMargin(plot.getDomainAxis().getUpperMargin()*6);
            plot.getDomainAxis().setLowerMargin(plot.getDomainAxis().getLowerMargin()*6);
            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin()*1.7);

            plot.getDomainAxis().setUpperMargin(0.9);
            plot.getDomainAxis().setLowerMargin(0.9);
            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin() * 4);

            ValueAxis va = plot.getRangeAxis();
            va.setAutoRange(true);
            XYItemRenderer renderer = plot.getRenderer(); //DateFormat.getInstance()
            XYItemLabelGenerator generator = new StandardXYItemLabelGenerator("{1} \n {2}", new SimpleDateFormat("yyyy.MM.dd"), new DecimalFormat("0.00"));
            renderer.setSeriesItemLabelGenerator(0, generator);//setLabelGenerator(generator);

            renderer.setBaseItemLabelsVisible(true);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainCrosshairPaint(Color.GRAY);

            if (renderer instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer) renderer;
                rend.setBaseShapesVisible(true);
                rend.setBaseShapesFilled(true);
            }

            plot.setRenderer(renderer);


            if (dataItems != null && dataItems.size() > 0){
                OHLCDataItem[] ohlc = dataItems.toArray(new OHLCDataItem[dataItems.size()]);
                XYDataset referenceRangeDataset = new DefaultOHLCDataset("Normal Reference Range", ohlc);
                plot.setDataset(1, referenceRangeDataset);
                plot.mapDatasetToRangeAxis(1, 0);
                plot.setRenderer(1,new HighLowRenderer());

            }

            XYTaskDataset drugDataset = getDrugDataSet( demographicNo,drugs);

            //DateAxis xAxis = new DateAxis("Date/Time");
            //DateAxis xAxis = plot.getRangeAxis();
            SymbolAxis yAxis = new SymbolAxis("Meds",  getDrugSymbol(demographicNo,drugs));
            yAxis.setGridBandsVisible(false);
            XYBarRenderer xyrenderer = new XYBarRenderer();
            xyrenderer.setUseYInterval(true);
            xyrenderer.setBarPainter(new StandardXYBarPainter());

            //XYPlot xyplot = new XYPlot(drugDataset, xAxis, yAxis, xyrenderer);
            XYPlot xyplot = new XYPlot(drugDataset, plot.getDomainAxis(), yAxis, xyrenderer);


            xyplot.getDomainAxis().setUpperMargin(0.9);
            xyplot.getDomainAxis().setLowerMargin(0.9);



            CombinedDomainXYPlot cplot = new CombinedDomainXYPlot(new DateAxis("Date/Time"));
            cplot.add(plot);
            cplot.add(xyplot);

                ///////
            chart = new JFreeChart(chartTitle,cplot);
            chart.setBackgroundPaint(Color.white);

            return chart;
        }





       JFreeChart labChartRef(Integer demographicNo, String typeIdName, String typeIdName2, String patientName, String chartTitle) {
        org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();
        ArrayList<EctMeasurementsDataBean> list = getList(demographicNo, typeIdName);
        String typeYAxisName = "";
         ArrayList<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();
        if (typeIdName.equals("BP")) {
            log.debug("Using BP LOGIC FOR type 1 ");
            EctMeasurementsDataBean sampleLine = list.get(0);
            typeYAxisName = sampleLine.getTypeDescription();
            TimeSeries systolic = new TimeSeries("Systolic", Day.class);
            TimeSeries diastolic = new TimeSeries("Diastolic", Day.class);
            for (EctMeasurementsDataBean mdb : list) { // dataVector) {
                String[] str = mdb.getDataField().split("/");

                systolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[0]));
                diastolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[1]));
            }
            dataset.addSeries(diastolic);
            dataset.addSeries(systolic);
        } else {
            log.debug("Not Using BP LOGIC FOR type 1 ");
            // get the name from the TimeSeries
            EctMeasurementsDataBean sampleLine = list.get(0);
            String typeLegendName = sampleLine.getTypeDisplayName();
            typeYAxisName = sampleLine.getTypeDescription(); // this should be the type of measurement
            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (EctMeasurementsDataBean mdb : list) { //dataVector) {
                newSeries.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(mdb.getDataField()));
                try{
                    Hashtable h = getMeasurementsExt( mdb.getId());
                    if (h != null && h.containsKey("minimum")){
                        String min = (String) h.get("minimum");
                        String max = (String) h.get("maximum");
                        double open = Double.parseDouble(min.trim());
                        double high = Double.parseDouble(max.trim());
                        double low = Double.parseDouble(min.trim());
                        double close = Double.parseDouble(max.trim());
                        double volume = 1045;
                        dataItems.add(new OHLCDataItem(mdb.getDateObservedAsDate(), open, high, low, close, volume));
                    }
                }catch(Exception et){
                	MiscUtils.getLogger().error("Error", et);
                }
            }
            dataset.addSeries(newSeries);
        }



        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, "Days", typeYAxisName, dataset, true, true, true);

            XYPlot plot = chart.getXYPlot();
            plot.getDomainAxis().setAutoRange(true);


            log.debug("LEN " + plot.getDomainAxis().getLowerBound() + " ddd " + plot.getDomainAxis().getUpperMargin() + " eee " + plot.getDomainAxis().getLowerMargin());
            plot.getDomainAxis().setUpperMargin(plot.getDomainAxis().getUpperMargin()*6);
            plot.getDomainAxis().setLowerMargin(plot.getDomainAxis().getLowerMargin()*6);
            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin()*1.7);

            plot.getDomainAxis().setUpperMargin(0.9);
            plot.getDomainAxis().setLowerMargin(0.9);
            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin() * 4);

            ValueAxis va = plot.getRangeAxis();
            va.setAutoRange(true);
            XYItemRenderer renderer = plot.getRenderer(); //DateFormat.getInstance()
            XYItemLabelGenerator generator = new StandardXYItemLabelGenerator("{1} \n {2}", new SimpleDateFormat("yyyy.MM.dd"), new DecimalFormat("0.00"));
            renderer.setSeriesItemLabelGenerator(0, generator);//setLabelGenerator(generator);

            renderer.setBaseItemLabelsVisible(true);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainCrosshairPaint(Color.GRAY);


            if (renderer instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer) renderer;
                rend.setBaseShapesVisible(true);
                rend.setBaseShapesFilled(true);
            }

            plot.setRenderer(renderer);


            if (dataItems != null && dataItems.size() > 0){
                OHLCDataItem[] ohlc = dataItems.toArray(new OHLCDataItem[dataItems.size()]);
                XYDataset referenceRangeDataset = new DefaultOHLCDataset("Reference Range", ohlc);
                plot.setRenderer(1, setAxisAndDataSet(1,plot,plot.getRangeAxis(),referenceRangeDataset,Color.GREEN,new HighLowRenderer() ));
            }






            /////


            return chart;
    }

    JFreeChart defaultChart(Integer demographicNo, String typeIdName,
            String typeIdName2, String patientName,
            String chartTitle) {
        org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();

        ArrayList<EctMeasurementsDataBean> list = getList(demographicNo, typeIdName);
        String typeYAxisName = "";

        if (typeIdName.equals("BP")) {
            log.debug("Using BP LOGIC FOR type 1 ");
            EctMeasurementsDataBean sampleLine = list.get(0);
            typeYAxisName = sampleLine.getTypeDescription();
            TimeSeries systolic = new TimeSeries("Systolic", Day.class);
            TimeSeries diastolic = new TimeSeries("Diastolic", Day.class);
            for (EctMeasurementsDataBean mdb : list) { // dataVector) {
            	if(!mdb.getDataField().equals("")){
	                String[] str = mdb.getDataField().split("/");
	
	                systolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[0]));
	                diastolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[1]));
            	}else{
            		log.debug("Error passing measurement value to chart. DataField is empty for ID:" + mdb.getId());
            	}
            }
            dataset.addSeries(diastolic);
            dataset.addSeries(systolic);

        } else {
            log.debug("Not Using BP LOGIC FOR type 1 ");
            // get the name from the TimeSeries
            EctMeasurementsDataBean sampleLine = list.get(0);
            String typeLegendName = sampleLine.getTypeDisplayName();
            typeYAxisName = sampleLine.getTypeDescription(); // this should be the type of measurement

            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (EctMeasurementsDataBean mdb : list) { //dataVector) {
            	
            	if(!mdb.getDataField().equals("")){
            		newSeries.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(mdb.getDataField()));
            	}else{
            		log.debug("Error passing measurement value to chart. DataField is empty for ID:" + mdb.getId());
            	}
            }
            dataset.addSeries(newSeries);
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, "Days", typeYAxisName, dataset, true, true, true);

        if (typeIdName2 != null) {
            log.debug("type id name 2" + typeIdName2);

            ArrayList<EctMeasurementsDataBean> list2 = getList(demographicNo, typeIdName2);
            org.jfree.data.time.TimeSeriesCollection dataset2 = new org.jfree.data.time.TimeSeriesCollection();

            log.debug("list2 " + list2);
            
            EctMeasurementsDataBean sampleLine2 = list2.get(0);
            String typeLegendName = sampleLine2.getTypeDisplayName();
            String typeYAxisName2 = sampleLine2.getTypeDescription(); // this should be the type of measurement

            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (EctMeasurementsDataBean mdb : list2) { //dataVector) {
            	if(!mdb.getDataField().equals("")){
            		newSeries.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(mdb.getDataField()));
	            }else{
	        		log.debug("Error passing measurement value to chart. DataField is empty for ID:" + mdb.getId());
	            }
            }
            dataset2.addSeries(newSeries);

            ////
            final XYPlot plot = chart.getXYPlot();
            final NumberAxis axis2 = new NumberAxis(typeYAxisName2);
            axis2.setAutoRangeIncludesZero(false);
            plot.setRangeAxis(1, axis2);
            plot.setDataset(1, dataset2);
            plot.mapDatasetToRangeAxis(1, 1);
            final XYItemRenderer renderer = plot.getRenderer();
            renderer.setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
            if (renderer instanceof StandardXYItemRenderer) {
                final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;

                rr.setBaseShapesFilled(true);
            }

            final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
            renderer2.setSeriesPaint(0, Color.black);
            renderer.setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
            plot.setRenderer(1, renderer2);

        }
        return chart;
    }



    /*
     * Just Drugs
     */
    JFreeChart ChartMeds(Integer demographicNo,String patientName, String chartTitle,String[] drugs) {
            MiscUtils.getLogger().debug("In ChartMeds");
            org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();
            JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, "Days", "MEDS", dataset, true, true, true);

            XYPlot plot = chart.getXYPlot();
//            plot.getDomainAxis().setAutoRange(true);
//            Range rang = plot.getDataRange(plot.getRangeAxis());
//
//            log.debug("LEN " + plot.getDomainAxis().getLowerBound() + " ddd " + plot.getDomainAxis().getUpperMargin() + " eee " + plot.getDomainAxis().getLowerMargin());
//            plot.getDomainAxis().setUpperMargin(plot.getDomainAxis().getUpperMargin()*6);
//            plot.getDomainAxis().setLowerMargin(plot.getDomainAxis().getLowerMargin()*6);
//            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin()*1.7);
//
//            plot.getDomainAxis().setUpperMargin(0.9);
//            plot.getDomainAxis().setLowerMargin(0.9);
//            plot.getRangeAxis().setUpperMargin(plot.getRangeAxis().getUpperMargin() * 4);


            XYTaskDataset drugDataset = getDrugDataSet( demographicNo,drugs);

            SymbolAxis yAxis = new SymbolAxis("Meds",  getDrugSymbol(demographicNo,drugs));


            yAxis.setGridBandsVisible(false);
            XYBarRenderer xyrenderer = new XYBarRenderer();
            xyrenderer.setUseYInterval(true);
            xyrenderer.setBarPainter(new StandardXYBarPainter());

            //XYPlot xyplot = new XYPlot(drugDataset, xAxis, yAxis, xyrenderer);
            XYPlot xyplot = new XYPlot(drugDataset, plot.getDomainAxis(), yAxis, xyrenderer);

            xyplot.getDomainAxis().setUpperMargin(0.9);
            xyplot.getDomainAxis().setLowerMargin(0.9);

            CombinedDomainXYPlot cplot = new CombinedDomainXYPlot(new DateAxis("Date/Time"));
            cplot.add(xyplot);

            chart = new JFreeChart(chartTitle,cplot);
            chart.setBackgroundPaint(Color.white);
            return chart;
        }



    private  Hashtable getMeasurementsExt(Integer measurementId) {
		Hashtable<String,String> hash = new Hashtable<String,String>();
		if (measurementId!=null) {
		    MeasurementsExtDao dao = SpringUtils.getBean(MeasurementsExtDao.class);
		    MeasurementsExt m = dao.find(measurementId);
		    if(m != null) {
		    	hash.put(m.getKeyVal(), m.getVal());
		    }
		}
		return hash;
    }

    private static XYItemRenderer setAxisAndDataSet(int i,XYPlot plot, ValueAxis axis,XYDataset dataset,Paint p,XYItemRenderer renderer){
        plot.setRangeAxis(i, axis);
        plot.setDataset(i, dataset);
        plot.mapDatasetToRangeAxis(i, i);


        renderer.setSeriesPaint(0,p);
        axis.setLabelPaint(p);
        axis.setTickLabelPaint(p);
        return renderer;
    }



}
