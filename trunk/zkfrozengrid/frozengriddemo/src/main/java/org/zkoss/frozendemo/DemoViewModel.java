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

	private ListModel<Score> model;
	private String selectedItem;
	private String selectedItem2;
	
	@Init
	public void init() {
		
		List<Score> list = loadInfo();
		 model = new ListModelList<Score>(list);
	}

	private List<Score> loadInfo() {
		CSVReader reader = new CSVReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("ScoreSheet.csv")));
		ColumnPositionMappingStrategy<Score> strat = new ColumnPositionMappingStrategy<Score>();
		String[] columns = new String[] {"Player", "No.", "Date", "Time", "Location", "Opponent", 
				 "AB", "R", "H", "1B", "2B", "3B", "HR", "RBI", "SO", "BB", "SAC", "HP", "PO", "A", "E"}; // the fields to bind
		strat.setType(Score.class);
		strat.setColumnMapping(columns);
		
		CsvToBean<Score> csv = new CsvToBean<Score>();
		return csv.parse(strat, reader);
	}

	
	public ListModel<Score> getModel() {
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
