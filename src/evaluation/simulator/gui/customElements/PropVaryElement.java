package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.annotations.property.BoolProp;
import evaluation.simulator.annotations.property.DoubleProp;
import evaluation.simulator.annotations.property.FloatProp;
import evaluation.simulator.annotations.property.IntProp;
import evaluation.simulator.annotations.property.SimProp;
import evaluation.simulator.annotations.property.StringProp;
import evaluation.simulator.gui.customElements.accordion.AccordionEntry;
import evaluation.simulator.gui.customElements.configElements.BoolConfigElement;
import evaluation.simulator.gui.customElements.configElements.DoubleConfigElement;
import evaluation.simulator.gui.customElements.configElements.FloatConfigElement;
import evaluation.simulator.gui.customElements.configElements.IntConfigElement;
import evaluation.simulator.gui.customElements.configElements.StringConfigElement;
import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

import net.miginfocom.swing.MigLayout;

public class PropVaryElement extends JPanel{
	
	private static Logger logger = Logger.getLogger(PluginPanel.class);
	
	JComboBox<String> cBox1;
	JComboBox<String> cBox2;
	
	JTextField propElement1;
	JTextField propElement2;
		
	Map<String, SimProp> propMap;
	Map<JComboBox<String>, JTextField> boxToElementMap;
	Map<JTextField, String> propValueMap;
	
	public PropVaryElement() {
		 
		this.setLayout(new MigLayout());
		
		loadContent();
	}
	
	private void loadContent(){		
				
		this.propMap = SimPropRegistry.getInstance().getProperties();		
		
		String propertyStrings[] = propMap.keySet().toArray(new String[1]);
		propertyStrings[0] = "---";
		this.cBox1 = new JComboBox<String>(propertyStrings);
		this.cBox2 = new JComboBox<String>(propertyStrings);		
		addBoxListener(cBox1);
		addBoxListener(cBox2);		
		
		this.propElement1 = new JTextField();		
		this.propElement2 = new JTextField();
		addTextListener(propElement1);
		addTextListener(propElement2);
		
		this.boxToElementMap = new HashMap<>();
		this.boxToElementMap.put(cBox1, propElement1);
		this.boxToElementMap.put(cBox2, propElement2);
		
		this.propValueMap = new HashMap<>();
		this.propValueMap.put(propElement1, "");
		this.propValueMap.put(propElement2, "");

					
		this.add( cBox1, "wrap");
		this.add(propElement1, "wrap, growx");		
		this.add( cBox2, "wrap");
		this.add(propElement2, "wrap, growx");
		
		comboboxChanged(cBox1);
		comboboxChanged(cBox2);	
		
	}	

	private void comboboxChanged(JComboBox<String> ComboBox){
		
		JTextField currentElement = this.boxToElementMap.get(ComboBox);
		
		propValueMap.put(currentElement, "");
		currentElement.setText("");
		
		String currentItem = (String) ComboBox.getSelectedItem();
		if(currentItem == "---"){
			currentElement.setEnabled(false);			
			if(ComboBox == cBox1){				
				this.cBox2.setEnabled(false);
				this.propElement2.setEnabled(false);
			}			
		}
		else{			
			currentElement.setEnabled(true);
		}
		
		if((ComboBox == cBox1)&&(currentItem != "---")){
			this.cBox2.setEnabled(true);
		}
		
		logger.log(Level.DEBUG, currentElement.toString());		
		this.repaint();
	}
	
	private void addBoxListener(final JComboBox<String> property) {
		ItemListener il = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED && property.isVisible()) {
					PropVaryElement.this.comboboxChanged(property);
				}
			}
		};
		property.addItemListener(il);		
	}
	
	private void addTextListener(final JTextField field) {
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {

				if (a.getActionCommand() != null ) {
					PropVaryElement.this.propValueMap.put(field, a.getActionCommand().toString());
					logger.log(Level.DEBUG, "PropertyToVary set to: " + PropVaryElement.this.propValueMap.get(field));					
				}
			}
			
		};
		field.addActionListener(al);
	}
	
}
