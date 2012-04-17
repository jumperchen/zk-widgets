package org.zkoss.frozendemo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;


public class TestComposer4 extends GenericForwardComposer {
	 private Random random = new Random();
	 private Calendar cal = Calendar.getInstance();

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
		
		List list = new ArrayList();
		for (int i = 0; i < 10; i++) {
			String[] data = new String[10];
			for (int j = 0; j < 10; j++) {
				data[j] = "item " + i + j;
			}
			list.add(data);
		}
		comp.setAttribute("model", new ListModelList(list));
		
		
		comp.setAttribute("renderer", new RowRenderer<String[]>() {

			public void render(Row row, String[] data, int index) throws Exception {
				for (int i = 0; i < data.length; i++) {
					row.appendChild(new Label(data[i]));
				}
			}
		});
		
	}
	


	
	

	
}
