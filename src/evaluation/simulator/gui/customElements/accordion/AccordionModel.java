package evaluation.simulator.gui.customElements.accordion;

import java.util.List;
import java.util.Map.Entry;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import evaluation.simulator.gui.customElements.SimConfigPanel;
import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class AccordionModel implements TableModel {

	private final List<Entry<String, String>> _propertiesInThisCategory;
	private final SimPropRegistry _spr;

	public AccordionModel(String category) {

		this._spr = SimPropRegistry.getInstance();
		this._propertiesInThisCategory = this._spr.getCategoryItems(category);
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public Class<?> getColumnClass(int arg0) {

		if (arg0 == 1) {
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

	public List<Entry<String, String>> getProperties() {
		return this._propertiesInThisCategory;
	}

	@Override
	public int getRowCount() {
		return this._propertiesInThisCategory.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {

		if (arg1 == 0) {
			return this._propertiesInThisCategory.get(arg0).getKey();
		}

		return this._spr.getValue(
				this._propertiesInThisCategory.get(arg0).getKey()).getValue();
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		if ((arg1 == 1)
				&& this._spr.getValue(
						this._propertiesInThisCategory.get(arg0).getKey())
						.getEnable()) {
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

		String id = this._propertiesInThisCategory.get(arg1).getKey();
		Logger.Log(LogLevel.DEBUG, "Changed " + id);
		this._spr.setValue(id, arg0);

		DependencyChecker.checkAll(this._spr);
		SimConfigPanel.setStatusofSaveButton(!DependencyChecker.errorsInConfig);
	}

}
