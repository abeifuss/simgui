package evaluation.simulator.gui.customElements.accordion;

import java.awt.Component;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.annotations.simulationProperty.StringProp;

@SuppressWarnings("serial")
public class AccordionCellEditor extends AbstractCellEditor implements
		TableCellEditor {

	private final JCheckBox checkBox = new JCheckBox();
	private Class<?> propertyClass;
	private final JComboBox<String> comboBox = new JComboBox<>();
	private final JSpinner spinner = new JSpinner();

	private final JTextField _textfield = new JTextField();
	private boolean predefined;

	@Override
	public Object getCellEditorValue() {

		if (this.propertyClass == Integer.class) {
			return this.spinner.getValue();
		} else if (this.propertyClass == Boolean.class) {
			return this.checkBox.isSelected();
		} else if (this.propertyClass == Float.class) {
			return this._textfield.getText();
		} else if (this.propertyClass == Double.class) {
			return this._textfield.getText();
		}else if (this.propertyClass == String.class) {
			if (this.predefined) {
				return this.comboBox.getSelectedItem();
			}
			return this._textfield.getText();
		}

		return "unknown";
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		List<SimProp> list = ((AccordionModel) table.getModel()).getProperties();
		
		this.propertyClass = list.get(row).getValueType();

		// TODO: extend!!!
		if (this.propertyClass == Integer.class) {
			this.spinner.setValue((int) value);
			return this.spinner;
		} else if (this.propertyClass == Float.class) {
			this._textfield.setText(value + "");
			return this._textfield;
		} else if (this.propertyClass == Double.class) {
			this._textfield.setText(value + "");
			return this._textfield;
		} else if (this.propertyClass == Boolean.class) {
			this.checkBox.setSelected((boolean) value);
			return this.checkBox;
		} else if (this.propertyClass == String.class) {
			StringProp stringProp = (StringProp) list.get(row);
			String possibleValues = stringProp.getPossibleValues();
			if (!possibleValues.equals("")) {
				this.predefined = true;
				this.comboBox.removeAllItems();
				String[] valueArray = possibleValues.split(",");
				for (String element : valueArray) {
					this.comboBox.addItem(element.replace(" ", ""));
				}
				return this.comboBox;
			}
			this.predefined = false;
		}

		this._textfield.setText((String) value);
		return this._textfield;
	}

}
