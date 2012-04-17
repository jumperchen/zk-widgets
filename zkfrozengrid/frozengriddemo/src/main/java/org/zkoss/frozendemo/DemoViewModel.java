package org.zkoss.frozendemo;

import java.io.InputStreamReader;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

public class DemoViewModel {

	private ListModel<SwineFluInfo> model;
	private String selectedItem;
	private String selectedItem2;

	
	@Init
	public void init() {
		
		List<SwineFluInfo> list = loadInfo();
		 model = new ListModelList<SwineFluInfo>(list);
	}

	private List<SwineFluInfo> loadInfo() {
		CSVReader reader = new CSVReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("swineFlu.csv")));
		ColumnPositionMappingStrategy<SwineFluInfo> strat = new ColumnPositionMappingStrategy<SwineFluInfo>();
		String[] columns = new String[] { "state", "cases", "deaths",
				"description", "latitude", "longitude" }; // the fields to bind
		strat.setType(SwineFluInfo.class);
		strat.setColumnMapping(columns);
		
		CsvToBean<SwineFluInfo> csv = new CsvToBean<SwineFluInfo>();
		return csv.parse(strat, reader);
	}

	
	public ListModel<SwineFluInfo> getModel() {
		return model;
	}
	
	
	
	
	
	
	
	
	
	
	
	@Command
	public void select() {
		System.out.println(selectedItem);
	}

	@Command
	public void select2() {
		System.out.println(selectedItem2);
	}

	

	public ListModel getModel2() {
		return model;
		// return new MyListModel2(model);
	}

	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	public String getSelectedItem2() {
		return selectedItem2;
	}

	public void setSelectedItem2(String selectedItem2) {
		this.selectedItem2 = selectedItem2;
	}

}
