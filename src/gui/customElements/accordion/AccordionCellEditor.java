package gui.customElements.accordion;

import gui.pluginRegistry.SimPropRegistry;

import java.awt.Component;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("serial")
public class AccordionCellEditor extends AbstractCellEditor implements TableCellEditor{

	// TODO: final Component instead of several concrete Elements?!
	final JSpinner spinner = new JSpinner();
//	final JSpinner spinnerFloat = new JSpinner();
	final JCheckBox cbox = new JCheckBox();
	final JTextField textfield = new JTextField();
	Class<?> c;

	@Override
	public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int column){
		
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		List<Entry<String, String>> list = ((AccordionModel) table.getModel()).getProperties();
		c = gcr.getValue(list.get(row).getKey()).getValueType();
		
		// TODO: extend!!!
		if (c == Integer.class){
			spinner.setValue((int)value);
			return spinner;
		}else if (c == Float.class){
			textfield.setText(value+"");
			return textfield;
		}else if (c == Boolean.class){
			cbox.setSelected((boolean)value);
			return cbox;
		}
		
		textfield.setText((String)value);
		return textfield;
	}

	@Override
	public Object getCellEditorValue() {
		
		if (c == Integer.class){
			return spinner.getValue();
		}else if (c == Boolean.class){
			return cbox.isSelected();
		}else if (c == Float.class){
			return textfield.getText();
		}else if (c == String.class){
			return textfield.getText();
		}
		
		return "unknown";
	}
	
}
