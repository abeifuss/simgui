package evaluation.simulator.gui.customElements.accordion;

import java.awt.Component;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

@SuppressWarnings("serial")
public class AccordionCellEditor extends AbstractCellEditor implements
		TableCellEditor {

	private final JCheckBox _cbox = new JCheckBox();
	private Class<?> _class;
	private final JSpinner _spinner = new JSpinner();
	private final JTextField _textfield = new JTextField();

	@Override
	public Object getCellEditorValue() {

		if (this._class == Integer.class) {
			return this._spinner.getValue();
		} else if (this._class == Boolean.class) {
			return this._cbox.isSelected();
		} else if (this._class == Float.class) {
			return this._textfield.getText();
		} else if (this._class == String.class) {
			return this._textfield.getText();
		}

		return "unknown";
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		SimPropRegistry spr = SimPropRegistry.getInstance();
		List<Entry<String, String>> list = ((AccordionModel) table.getModel())
				.getProperties();
		this._class = spr.getValue(list.get(row).getKey()).getValueType();

		// TODO: extend!!!
		if (this._class == Integer.class) {
			this._spinner.setValue((int) value);
			return this._spinner;
		} else if (this._class == Float.class) {
			this._textfield.setText(value + "");
			return this._textfield;
		} else if (this._class == Boolean.class) {
			this._cbox.setSelected((boolean) value);
			return this._cbox;
		}

		this._textfield.setText((String) value);
		return this._textfield;
	}

}
