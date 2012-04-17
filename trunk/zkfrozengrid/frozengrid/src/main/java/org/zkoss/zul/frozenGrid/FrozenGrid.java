/* FrozenGrid.java

	Purpose:
		
	Description:
		
	History:
		Wed Apr 11 16:37:50     2012, Created by jimmyshiau

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul.frozenGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.impl.BindRowRenderer;
import org.zkoss.bind.xel.zel.BindELContext;
import org.zkoss.lang.Strings;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.ForEachStatus;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Groupfoot;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.event.ListDataListener;
import org.zkoss.zul.event.ZulEvents;

/**
 * A frozen grid component to represent a frozen column or row in grid, like MS Excel. 
 * @author jimmyshiau
 * @since 6.0.0
 */
public class FrozenGrid extends Div implements IdSpace, AfterCompose {
	
	private static final String ATTR_ON_INIT_RENDER_POSTED =
		"org.zkoss.zul.frozenGrid.FrozenGrid.onInitLaterPosted";
	
	private int _columns = 0;
	private int _rows = 0;
	private String _toolbarStyle;
	
	private transient ListModel<?> _model;
	private transient RowRenderer<?> _renderer;
	private transient ListDataListener _dataListener;
	
	@Wire
	private Grid tlGrid;
	@Wire
	private Grid trGrid;
	@Wire
	private Grid blGrid;
	@Wire
	private Grid brGrid;
	@Wire
	private Div toolbar;

		
	private ListModelList<Object> topModel;
	private ListModelList<Object> bottomModel;

	public FrozenGrid() {
		Executions.createComponents("~./zul/html/frozenGrid.zul", this, null);
		Selectors.wireComponents(this, this, false);

		topModel = new ListModelList<Object>();
		bottomModel = new ListModelList<Object>();

		RendererCallback leftRowCallback = new RendererCallback() {
			@Override
			public void afterRender(int index, List<Component> children,
					Map<Integer, List<Component>> cellCache) {
				storeRightCells(index, children, cellCache);
			}
		};

		RendererCallback rightRowCallback = new RendererCallback() {
			@Override
			public void afterRender(int index, List<Component> children,
					Map<Integer, List<Component>> cellCache) {
				storeLeftCells(index, children, cellCache);
			}
		};

		HashMap<Integer, List<Component>> topCellCache = 
			new HashMap<Integer, List<Component>>();
		HashMap<Integer, List<Component>> bottomCellCache = 
			new HashMap<Integer, List<Component>>();

		tlGrid.setRowRenderer(
				new FrozenRowRenderer<Object>(topCellCache, leftRowCallback));
		trGrid.setRowRenderer(
				new FrozenRowRenderer<Object>(topCellCache, rightRowCallback));
		blGrid.setRowRenderer(
				new FrozenRowRenderer<Object>(bottomCellCache, leftRowCallback));
		brGrid.setRowRenderer(
				new FrozenRowRenderer<Object>(bottomCellCache, rightRowCallback));

	}
	
	public void afterCompose() {
		 
		if (!Strings.isBlank(_toolbarStyle))
			toolbar.setStyle(_toolbarStyle);
		
		
		Template template = getTemplate("toolbar");
		
		if (template != null)
			template.create(toolbar, null, null, null);
		
		
		createColumns(getTemplate("columns"));
		
		if (_model != null) return;
		createRows(getTemplate("rows"));
		tlGrid.invalidate();
		trGrid.invalidate();
		blGrid.invalidate();
		brGrid.invalidate();
	}

	private void createColumns(Template template) {
		if (template == null) return;
		
		Columns cols = (Columns) template.create(tlGrid, null, null, null)[0];
		
		if (cols == null) return;
		
		List<Component> children = new ArrayList<Component>(cols.getChildren());
		
		cols.getChildren().clear();
		Columns trCols = (Columns)cols.clone();
		Columns blCols = new Columns();
		Columns brCols = new Columns();
		
		trGrid.appendChild(trCols);
		blGrid.appendChild(blCols);
		brGrid.appendChild(brCols);
		
		int i = 0;
		for (Component component : children) {
			Column col = (Column) component;
			Column bCol =  new Column();
			bCol.setWidth(col.getWidth());
			bCol.setHflex(col.getHflex());
			
			if (i++ < _columns) {
				cols.appendChild(col);
				blCols.appendChild(bCol);
			} else {
				trCols.appendChild(col);
				brCols.appendChild(bCol);
			}
		}
		
	}

	private void createRows(Template template) {
		if (template == null) return;
		reAttachRows((Rows) template.create(brGrid, null, null, null)[0]);
	}

	private void reAttachRows(Rows rows) {
		if (rows == null) return;
		List<Component> children = new ArrayList<Component>(rows.getChildren());
//		rows.getChildren().clear();
		
		Rows blRows = new Rows();
		Rows brRows = new Rows();
		Rows tlRows = new Rows();
		Rows trRows = new Rows();
		
		brGrid.removeChild(rows);
		blGrid.appendChild(blRows);
		brGrid.appendChild(brRows);
		tlGrid.appendChild(tlRows);
		trGrid.appendChild(trRows);
		
		int rowIndex = 0;
		Map<Integer, Integer> rowSpanInfo = new HashMap<Integer, Integer>();
		for (Component row : children) {
			
			Integer rowOffset = rowSpanInfo.get(rowIndex);
			if (rowOffset == null)
				rowOffset = 0;
			int colOffset = rowOffset;
			
			if (row instanceof Row) {
				List<Component> cells = new ArrayList<Component>(row.getChildren());
				row.getChildren().clear();
				
				Row rRow = (Row) row.clone();
				if (rowIndex < _rows) {
					tlRows.appendChild(row);
					trRows.appendChild(rRow);
				} else {
					blRows.appendChild(row);
					brRows.appendChild(rRow);
				}
			
				for (Component comp : cells) {
					((colOffset++ < _columns ? row: rRow)).appendChild(comp);
					
					if (comp instanceof Cell) {
						Cell cell = (Cell)comp;
						colOffset += cell.getColspan() - 1;
						
						int rowSpan = cell.getRowspan() - 1;
						
						updateOffsetInfo(rowIndex + 1, rowSpan, rowSpanInfo);
					}
				}
			} else if (row instanceof Groupfoot) {
				
			} else if (row instanceof Group) {}
			rowIndex++;
		}
	}
	
	private void updateOffsetInfo(int rowIndex, int rowSpan,
			Map<Integer, Integer> spanInfo) {
		while (rowSpan > 0) {
			Integer rowOffset = spanInfo.get(rowIndex);
			if (rowOffset == null)
				rowOffset = 0;
			spanInfo.put(rowIndex, ++rowOffset);
			rowIndex++;
			rowSpan--;
		}
	}

	/**
	 * Sets the number of columns to freeze.(from left to right)
	 * @param columns positive only
	 */
	public void setColumns(int columns) {
		if (columns < 0)
			throw new WrongValueException("Positive only");
		if (_columns != columns) {
			_columns = columns;
//			smartUpdate("columns", _columns);
		}
	}

	/**
	 * Returns the number of columns to freeze.
	 * <p>Default: 0
	 */
	public int getColumns() {
		return _columns;
	}

	/**
	 * Sets the number of rows to freeze.(from top to bottom)
	 * 
	 * @param rows positive only
	 */
	public void setRows(int rows) {
		if (rows < 0)
			throw new WrongValueException("Positive only");
		if (_rows != rows) {
			_rows = rows;
//			smartUpdate("rows", _rows);
		}
	}

	/**
	 * Returns the number of rows to freeze.
	 * <p>Default: 0
	 */
	public int getRows() {
		return _rows;
	}

	public void setToolbarStyle(String toolbarStyle) {
		this._toolbarStyle = toolbarStyle;
		if (toolbar != null)
			toolbar.setStyle(toolbarStyle);
	}
	
	public String getToolbarStyle() {
		return _toolbarStyle;
	}
	
	private void initDataListener() {
		if (_dataListener == null)
			_dataListener = new ListDataListener() {
				public void onChange(ListDataEvent event) {
					postOnInitRender();
				}
			};
			
		_model.addListDataListener(_dataListener);
	}
	
	public void setModel(ListModel<?> model) {
		if (model != null) {
			if (_model != model) {
				if (_model != null) {
					_model.removeListDataListener(_dataListener);
				}
				_model = model;
				initDataListener();
			}
			postOnInitRender();
		} else if (_model != null) {
			_model.removeListDataListener(_dataListener);
			_model = null;
		}
	}
	
	private void postOnInitRender() {
		//20080724, Henri Chen: optimize to avoid postOnInitRender twice
		if (getAttribute(ATTR_ON_INIT_RENDER_POSTED) == null) {
			setAttribute(ATTR_ON_INIT_RENDER_POSTED, Boolean.TRUE);
			Events.postEvent("onInitRender", this, null);
		}
	}
	
	public void onInitRender() {
		removeAttribute(ATTR_ON_INIT_RENDER_POSTED);
		doInitRenderer();
	}
	
	private void doInitRenderer() {
		splitModel(_model);
		tlGrid.setModel(topModel);
		trGrid.setModel(topModel);
		blGrid.setModel(bottomModel);
		brGrid.setModel(bottomModel);
		Events.postEvent(ZulEvents.ON_AFTER_RENDER, this, null);// notify the grid when all of the row have been rendered.
		
		if (getRowRenderer() instanceof BindRowRenderer) {
//			BindELContext.addModel(trGrid, topModel);
//			BindELContext.addModel(trGrid, topModel);
//			BindELContext.addModel(blGrid, bottomModel);
//			BindELContext.addModel(brGrid, bottomModel);
		}
	}
	
	private void splitModel(ListModel<?> model) {
		topModel.clear();
		bottomModel.clear();
		
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			Object o = (Object) model.getElementAt(i);
			if (i <_rows)
				topModel.add(o);
			else
				bottomModel.add(o);
		}
	}

	public ListModel<?> getModel() {
		return _model;
	}
	
	public void setRowRenderer(RowRenderer<?> renderer) {
		if (_renderer != renderer) {
			this._renderer = renderer;
			if (_model != null)
				postOnInitRender();
		}
	}
	
	public RowRenderer<?> getRowRenderer() {
		return _renderer;
	}
	
	private void storeLeftCells(
			int index, List<Component> children, Map<Integer, List<Component>> cellCache) {
		if (children.isEmpty()) return;
		if (_columns > 0) {
			List<Component> list = children.subList(0, _columns + 1);
			if (!list.isEmpty())
				cellCache.put(index, new ArrayList<Component>(list));
			list.clear();
		}
	}
	
	private void storeRightCells(int index, List<Component> children,
			Map<Integer, List<Component>> cellCache) {
		
		if (children.isEmpty()) return;
		
		int size = children.size();
		List<Component> list = children.subList(_columns, size);
		if (!list.isEmpty())
			cellCache.put(index, new ArrayList<Component>(list));
		list.clear();
	}
	
	private abstract class RendererCallback {
		abstract void afterRender(int index, List<Component> children, Map<Integer, List<Component>> cellCache);
	}
	
	private class FrozenRowRenderer<T> implements RowRenderer<T> {

		private RendererCallback callback;
		private Map<Integer, List<Component>> cellCache;

		public FrozenRowRenderer(Map<Integer, List<Component>> cellCache,
				RendererCallback callback) {
			super();
			this.cellCache = cellCache;
			this.callback = callback;
		}
		
		private List<Component> renderTemplate(Row row, final Object data, final int index, Template template) {
			final Rows rows = (Rows)row.getParent();
			final Grid grid = (Grid)rows.getParent();
			
			final Component[] items = 
				template.create(rows, row, new VariableResolver() {
					public Object resolveVariable(String name) {
						if ("each".equals(name)) {
							return data;
						} else if ("forEachStatus".equals(name)) {
							return new ForEachStatus() {
								@Override
								public ForEachStatus getPrevious() {
									return null;
								}
								@Override
								public Object getEach() {
									return data;
								}
								@Override
								public int getIndex() {
									return index;
								}
								@Override
								public Integer getBegin() {
									return 0;
								}
								@Override
								public Integer getEnd() {
									return grid.getModel().getSize();
								}
							};
						} 
//						else if ("groupingInfo".equals(name)) {
//							return groupingInfo;
//						} 
						else {
							return null;
						}
					}
				}, null);
			final Row nr = (Row)items[0];
			if (nr.getValue() == null) //template might set it
				nr.setValue(data);
			row.setAttribute("org.zkoss.zul.model.renderAs", nr);
				//indicate a new row is created to replace the existent one
			row.detach();
			return nr.getChildren();
		}
		
		@Override
		public void render(Row row, Object data, int index) throws Exception {
			List<Component> list = cellCache.get(index);
			
			if (list != null) {
				for (Component comp : list) {
					row.appendChild(comp);
				}
				cellCache.remove(index);
			} else {
				RowRenderer<Object> renderer = (RowRenderer<Object>) _renderer;
				Template template = getTemplate("model");
				List<Component> children = null;
				if (renderer != null) {
					renderer.render(row, data, index);
					Row nr = (Row) row.getAttribute("org.zkoss.zul.model.renderAs");
					if (nr != null)
						children = nr.getChildren();
					else
						children = row.getChildren();
				} else if (template != null) {
					children = renderTemplate(row, data, index, template);
				}
				callback.afterRender(index, children, cellCache);
				if (renderer instanceof BindRowRenderer) 
					cellCache.remove(index); //don't use row cache
			}
		}
		
	}
	
	@Override
	public String getZclass() {
		return _zclass == null ? "z-frozenGrid" : _zclass;
	}
}

