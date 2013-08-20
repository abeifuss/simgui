package gui.customElements.accordion;

import gui.pluginRegistry.DependencyChecker;
import gui.pluginRegistry.SimPropRegistry;

import java.util.List;
import java.util.Map.Entry;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import log.LogLevel;
import log.Logger;

public class AccordionModel implements TableModel {

	SimPropRegistry gcr;
	List<Entry<String, String>> propertiesInThisCategory;

	public AccordionModel(String category) {

		gcr = SimPropRegistry.getInstance();
		propertiesInThisCategory = gcr.getCategoryItems(category);
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public Class<?> getColumnClass(int arg0) {

		if (arg0 == 1) {
			// String, Integer, Float, Bool or some exotic stuff
			return Object.class;
		} else if (arg0 == 0) {
			// First row is always a string (property name)
			return String.class;
		}
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int arg0) {
		return "col " + arg0;
	}

	@Override
	public int getRowCount() {
		return propertiesInThisCategory.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		
		if (arg1 == 0){
			return propertiesInThisCategory.get(arg0).getKey();
		}
		
		return gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getValue();
		
		// request the value from the guiConfigReqistry
//		if (gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getValueType() == Integer.class ){
//			return gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getValue();
//		}else if (gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getValueType() == Boolean.class ){
//			return gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getValue();
//		}else if (gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getValueType() == Float.class ){
//			return gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getValue();
//		}else if (gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getValueType() == String.class ){
//			return gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getValue();
//		}else {
//			return new String("");
//		}
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		if (arg1 == 1 && gcr.getValue(propertiesInThisCategory.get(arg0).getKey()).getEnable() ){
			return true;
		}
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		
		String id = propertiesInThisCategory.get(arg1).getKey();
		Logger.Log(LogLevel.DEBUG, "Changed " + id);
		gcr.setValue(id, arg0);
		
		DependencyChecker.checkAll(gcr);
	}

	public List<Entry<String, String>> getProperties() {
		return propertiesInThisCategory;
	}

}
