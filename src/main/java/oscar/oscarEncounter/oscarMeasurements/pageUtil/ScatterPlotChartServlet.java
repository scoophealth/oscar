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
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jCharts.axisChart.AxisChart;
import org.jCharts.axisChart.ScatterPlotAxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.DataSeries;
import org.jCharts.chartData.ScatterPlotDataSeries;
import org.jCharts.chartData.ScatterPlotDataSet;
import org.jCharts.encoders.ServletEncoderHelper;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.properties.ScatterPlotProperties;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.types.ChartType;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.ValidationsDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.Validations;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.util.ConversionUtils;

public class ScatterPlotChartServlet extends HttpServlet {
	//---all of my charts serviced by this Servlet will have the same properties.
	protected LegendProperties legendProperties;
	protected AxisProperties axisProperties;
	protected ChartProperties chartProperties;

	protected int width = 550;
	protected int height = 360;

	/**********************************************************************************************
	 *
	 **********************************************************************************************/
	public void init() {
		this.legendProperties = new LegendProperties();
		this.chartProperties = new ChartProperties();
		this.axisProperties = new AxisProperties(true);
		ChartFont axisScaleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 13), Color.black);
		axisProperties.getXAxisProperties().setScaleChartFont(axisScaleFont);
		axisProperties.getYAxisProperties().setScaleChartFont(axisScaleFont);

		ChartFont axisTitleFont = new ChartFont(new Font("Arial Narrow", Font.PLAIN, 14), Color.black);
		axisProperties.getXAxisProperties().setTitleChartFont(axisTitleFont);
		axisProperties.getYAxisProperties().setTitleChartFont(axisTitleFont);

		ChartFont titleFont = new ChartFont(new Font("Georgia Negreta cursiva", Font.PLAIN, 14), Color.black);
		this.chartProperties.setTitleFont(titleFont);
	}

	/******************************************************************************************
	 *
	 *
	 ******************************************************************************************/
	private ScatterPlotProperties createScatterPlotProperties() {
		Stroke[] strokes = new Stroke[] { LineChartProperties.DEFAULT_LINE_STROKE };
		Shape[] shapes = new Shape[] { PointChartProperties.SHAPE_CIRCLE };

		return new ScatterPlotProperties(strokes, shapes);
	}

	/******************************************************************************************
	*
	*
	******************************************************************************************/
	private LineChartProperties createLineChartProperties() {
		Stroke[] strokes = { LineChartProperties.DEFAULT_LINE_STROKE, LineChartProperties.DEFAULT_LINE_STROKE };
		Shape[] shapes = { PointChartProperties.SHAPE_TRIANGLE, PointChartProperties.SHAPE_CIRCLE };

		return new LineChartProperties(strokes, shapes);
	}

	/*****************************************************************************************
	 * Generates dataset
	 *
	 * @return scatterPlotDataSeries
	 ******************************************************************************************/
	private ScatterPlotDataSeries createScatterPlotDataSeries(String demo, String type, String mInstrc) {
		long[][] results = generateResult(demo, type, mInstrc);
		String chartTitle = type + "-" + mInstrc;
		ScatterPlotDataSet scatterPlotDataSet = new ScatterPlotDataSet(this.createScatterPlotProperties());
		ScatterPlotDataSeries scatterPlotDataSeries = null;

		if (results != null) {

			if (type.compareTo("BP") != 0) {
				Point2D.Double[] points = new Point2D.Double[results[0].length];
				for (int x = 0; x < results[0].length; x++) {
					points[x] = ScatterPlotDataSet.createPoint2DDouble();
					points[x].setLocation(results[0][x] - results[0][0], results[1][x]);
				}

				scatterPlotDataSet.addDataPoints(points, Color.red, chartTitle);
				scatterPlotDataSeries = new ScatterPlotDataSeries(scatterPlotDataSet, "Day (note: only the last data on the same observation date is plotted)", "Test Results", chartTitle);
			}
		}

		return scatterPlotDataSeries;
	}

	/*****************************************************************************************
	 * Generates Blood pressure dataset
	 *
	 * @return DataSeries
	 ******************************************************************************************/
	private DataSeries createBloodPressureDataSeries(String demo, String type, String mInstrc) {

		long[][] results = generateResult(demo, type, mInstrc);
		DataSeries dataSeries = null;
		String chartTitle = type + "-" + mInstrc;
		String xAxisTitle = "Tests (note: only the last data on the same observation date is plotted)";
		String yAxisTitle = "Hgmm";

		if (results != null) {

			if (type.compareTo("BP") == 0) {
				double[][] points = new double[2][results[1].length / 2];
				String[] legendLabels = { "Systolic", "Diastolic" };
				Paint[] paints = { Color.red, Color.blue };
				LineChartProperties lineChartProperties = this.createLineChartProperties();
				int offset = results[1].length / 2;
				String[] xAxisLabels = new String[offset];

				for (int x = 0; x < results[1].length / 2; x++) {
					MiscUtils.getLogger().debug("systolic" + x + " " + results[1][x]);
					points[0][x] = results[1][x];
					int testNum = x + 1;
					String xAxisLabel = "test" + testNum;
					xAxisLabels[x] = xAxisLabel;
					MiscUtils.getLogger().debug("xAxisLabel is " + xAxisLabels[x]);
				}

				for (int x = 0; x < results[1].length / 2; x++) {
					MiscUtils.getLogger().debug("Diastolic" + x + results[1][x + offset]);
					points[1][x] = results[1][x + offset];
				}

				try {
					AxisChartDataSet acds = new AxisChartDataSet(points, legendLabels, paints, ChartType.LINE, lineChartProperties);
					dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, chartTitle);
					dataSeries.addIAxisPlotDataSet(acds);
					MiscUtils.getLogger().debug("the diastolic data has been added successfully");
				} catch (Exception e) {
					MiscUtils.getLogger().debug("debug", e);
				}

			}

		}
		return dataSeries;
	}

	/*****************************************************************************************
	* Generates generate result from the database
	*
	* @return DataSeries
	******************************************************************************************/
	private long[][] generateResult(String demo, String type, String mInstrc) {
		//plot only last data of the day?!

		long[][] points = null;

		MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
		if (isNumeric(type, mInstrc)) {
			List<Object> dates = dao.findObservationDatesByDemographicNoTypeAndMeasuringInstruction(ConversionUtils.fromIntString(demo), type, mInstrc);
			int nbData = dates.size();
			points = new long[2][nbData];
			for (int i = 0; i < nbData; i++) {
				Measurement m = dao.findByDemographicNoTypeAndDate(ConversionUtils.fromIntString(demo), type, (java.util.Date) dates.get(i));

				if (m != null) {
					java.util.Date dateObserved = m.getDateObserved();
					points[0][i] = dateObserved.getTime() / 1000 / 60 / 60 / 24;
					points[1][i] = ConversionUtils.fromLongString(m.getDataField());
					MiscUtils.getLogger().debug("Date: " + points[0][i] + " Value: " + points[1][i]);
				}
			}
		} else if (type.compareTo("BP") == 0) {
			List<Date> measurements = dao.findByDemographicNoTypeAndMeasuringInstruction(ConversionUtils.fromIntString(demo), type, mInstrc);
			int nbPatient = measurements.size();
			points = new long[2][nbPatient * 2];
			for (int i = 0; i < nbPatient; i++) {
				//Measurement m = measurements.get(i);

				Measurement mm = dao.findByDemographicNoTypeAndDate(ConversionUtils.fromIntString(demo), type, measurements.get(i));
				if (mm != null) {
					String bloodPressure = mm.getDataField();
					MiscUtils.getLogger().debug("bloodPressure: " + bloodPressure);
					int slashIndex = bloodPressure.indexOf("/");
					if (slashIndex >= 0) {
						String systolic = bloodPressure.substring(0, slashIndex);
						java.util.Date dateObserved = mm.getDateObserved();
						points[0][i] = dateObserved.getTime() / 1000 / 60 / 60 / 24;
						points[1][i] = Long.parseLong(systolic);
						MiscUtils.getLogger().debug("systolic: " + i + " " + systolic);

						String diastolic = bloodPressure.substring(slashIndex + 1);
						points[0][i + nbPatient] = dateObserved.getTime() / 1000 / 60 / 60 / 24;
						points[1][i + nbPatient] = Long.parseLong(diastolic);
						MiscUtils.getLogger().debug("diastolic: " + points[1][i + nbPatient]);
					}
				}

			}

			MiscUtils.getLogger().debug("Store blood pressure data to a new array successfully");
		}

		return points;
	}

	private boolean isNumeric(String type, String mInstrc) {
		boolean result=false;
		MeasurementTypeDao dao = SpringUtils.getBean(MeasurementTypeDao.class);
		List<MeasurementType> measurementTypes = dao.findByTypeAndMeasuringInstruction(type, mInstrc);

		if (!measurementTypes.isEmpty()) {
			String validation = measurementTypes.get(0).getValidation();

			ValidationsDao valDao = SpringUtils.getBean(ValidationsDao.class);
			Validations v = valDao.find(Integer.parseInt(validation));
			if (v != null && v.isNumeric() != null && v.isNumeric()) {
				result=true;
			}
		}

		return result;
	}

	/**********************************************************************************************
	 *
	 **********************************************************************************************/

	public void service(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		String type = request.getParameter("type");
		String mInstrc = request.getParameter("mInstrc");
		EctSessionBean bean = (EctSessionBean) request.getSession().getAttribute("EctSessionBean");
		String demographicNo = null;

		 if(request.getParameter("demographicNo") != null) {
         	demographicNo = request.getParameter("demographicNo");
         }
         
         if (demographicNo == null &&  bean != null){
             demographicNo = bean.getDemographicNo();                    
         }
		
		
		try {
			//addIAxisPlotDataSet(IAxisPlotDataSet 
			DataAxisProperties xAxisProperties = new DataAxisProperties();
			DataAxisProperties yAxisProperties = new DataAxisProperties();

			ChartProperties chartProperties = new ChartProperties();
			LegendProperties legendProperties = new LegendProperties();


			if (type.compareTo("BP") == 0) {
				AxisProperties axisProperties = new AxisProperties(false);
				DataSeries dataSeries = this.createBloodPressureDataSeries(demographicNo, type, mInstrc);
				AxisChart axisChart = new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 550, 360);
				ServletEncoderHelper.encodeJPEG13(axisChart, 1.0f, httpServletResponse);
			} else {
				AxisProperties axisProperties = new AxisProperties(xAxisProperties, yAxisProperties);
				ScatterPlotDataSeries scatterPlotDataSeries = this.createScatterPlotDataSeries(demographicNo, type, mInstrc);
				if (scatterPlotDataSeries != null) {
					ScatterPlotAxisChart scatterPlotAxisChart = new ScatterPlotAxisChart(scatterPlotDataSeries, chartProperties, axisProperties, legendProperties, 500, 400);

					ServletEncoderHelper.encodeJPEG13(scatterPlotAxisChart, 1.0f, httpServletResponse);
				}
			}
		} catch (Exception t) {
			MiscUtils.getLogger().error("Error", t);
		}

	}
}
