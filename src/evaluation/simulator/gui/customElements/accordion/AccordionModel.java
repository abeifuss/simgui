package evaluation.simulator.gui.customElements.accordion;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.gui.customElements.SimConfigPanel;
import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class AccordionModel implements TableModel {

	private final List<SimProp> properties;
	private final SimPropRegistry simPropRegistry;

	public AccordionModel(List<SimProp> tmpListOfAllSimPropertiesInANamespace) {
		this.simPropRegistry = SimPropRegistry.getInstance();
		this.properties = tmpListOfAllSimPropertiesInANamespace;
		
		for ( SimProp property : properties ){
			Logger.Log(LogLevel.DEBUG, "Show: " + property.getPropertyID());
		}
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

	public List<SimProp> getProperties() {
		return this.properties;
	}

	@Override
	public int getRowCount() {
		return this.properties.size();
	}

	@Override
	public Object getValueAt(int row, int col) {

		if (col == 0) {
			return this.properties.get(row).getName();
		}

		return this.properties.get(row).getValue();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if ( (col == 1) && this.properties.get(row).getEnable() ) {
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

		String id = this.properties.get(arg1).getPropertyID();
		Logger.Log(LogLevel.DEBUG, "Changed " + id);
		this.simPropRegistry.setValue(id, arg0);

		DependencyChecker.checkAll(this.simPropRegistry);
		SimConfigPanel.setStatusofSaveButton(!DependencyChecker.errorsInConfig);
	}

}
