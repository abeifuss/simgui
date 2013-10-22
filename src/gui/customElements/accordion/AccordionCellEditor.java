package gui.customElements.accordion;

import gui.pluginRegistry.SimPropRegistry;

import java.awt.Component;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import annotations.StringProp;

@SuppressWarnings("serial")
public class AccordionCellEditor extends AbstractCellEditor implements TableCellEditor{

	private final JSpinner _spinner = new JSpinner();
	private final JCheckBox _cbox = new JCheckBox();
	private final JTextField _textfield = new JTextField();
	private final JComboBox<String> _combobox = new JComboBox<>();
	
	private Class<?> _class;
	private boolean predefined;

	@Override
	public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int column){
		
		SimPropRegistry spr = SimPropRegistry.getInstance();
		List<Entry<String, String>> list = ((AccordionModel) table.getModel()).getProperties();
		_class = spr.getValue(list.get(row).getKey()).getValueType();
		
		// TODO: extend!!!
		if (_class == Integer.class){
			_spinner.setValue((int)value);
			return _spinner;
		}else if (_class == Float.class){
			_textfield.setText(value+"");
			return _textfield;
		}else if (_class == Boolean.class){
			_cbox.setSelected((boolean)value);
			return _cbox;
		}else if (_class == String.class ){
			String possibleValues = ((StringProp)(spr.getValue(list.get(row).getKey()))).getPossibleValues();
			if ( !possibleValues.equals("") ){
				predefined = true;
				_combobox.removeAllItems();
				String[] valueArray = possibleValues.split(",");
				for (int x=0; x<valueArray.length; x++){
					 _combobox.addItem(valueArray[x].replace(" ", ""));
				}
				 return _combobox;
			}
			predefined = false;
		}
		
		_textfield.setText((String)value);
		return _textfield;
	}

	@Override
	public Object getCellEditorValue() {
		
		if (_class == Integer.class){
			return _spinner.getValue();
		}else if (_class == Boolean.class){
			return _cbox.isSelected();
		}else if (_class == Float.class){
			return _textfield.getText();
		}else if (_class == String.class){
			if (predefined){
				 return _combobox.getSelectedItem();
			}
			return _textfield.getText();
		}
		
		return "unknown";
	}
	
}
