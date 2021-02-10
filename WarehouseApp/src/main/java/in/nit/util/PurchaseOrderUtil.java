package in.nit.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderUtil {
	
	public void generatePieChart(String location,
								List<Object[]> list) {
		
		DefaultPieDataset dataset=null;
		JFreeChart chart=null;
		
		//a.Create DataSet and read List<Object[]> into DataSet values
		dataset=new DefaultPieDataset();
		
		for(Object[] ob:list) {
			//Key -OrderCode ,value-count value
			dataset.setValue(ob[0].toString(),Double.valueOf(ob[1].toString()));
		}
		
		//b. Convert Dataset data into JFreeChart object using ChartFactory class
		chart=ChartFactory.createPieChart("PURCHASE ORDER CODE", dataset);
		
		//C.Convert JFreeChart object into Image using ChartUtil class
		try {
			ChartUtils.saveChartAsJPEG(new File(location+"/purchaseorderA.jpg"), chart,400 , 300);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void generateBarChart(String location,
								List<Object[]> list) {
		
		DefaultCategoryDataset dataset=null;
		JFreeChart chart=null;
		
		//a.Create DataSet and read List<Object[]> into DataSet values
		dataset=new DefaultCategoryDataset();
		for(Object[] ob:list) {
			//Value ,key,display label
			dataset.setValue(Double.valueOf(ob[1].toString()),ob[0].toString(),"");
		}
		//b. Convert Dataset data into JFreeChart object using ChartFactory class
		chart=ChartFactory.createBarChart("PURCHASE ORDER CODE","PURCHASE ORDER TYPES", "COUNT", dataset);
		
		//C.Convert JFreeChart object into Image using ChartUtil class
		
		try {
			ChartUtils.saveChartAsJPEG(new File(location+"/purchaseorderB.jpg"),chart, 500,350);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


