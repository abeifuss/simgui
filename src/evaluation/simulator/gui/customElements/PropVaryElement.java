package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.LogLevel;

import evaluation.simulator.annotations.property.SimProp;
import evaluation.simulator.gui.customElements.structure.HelpPropValues;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

/**
 * @author alex
 *
 */
public class PropVaryElement extends JPanel {

	private static Logger logger = Logger.getLogger(PluginPanel.class);

	private JComboBox<String> cBox[];
	private JTextField propElement[];
	private HelpPropValues value[];
	private Class propType[];
	private final int numOfPropsToVary = 2;

	Map<String, SimProp> propMap;
	Map<JComboBox<String>, Integer> boxToIndexMap;
	Map<JTextField, Integer> propToIndexMap;

	public PropVaryElement() {
		this.setLayout(new MigLayout("", "[grow]", ""));

		loadContent();
	}

	/**
	 * Loads content
	 */
	private void loadContent() {

		this.propMap = SimPropRegistry.getInstance().getProperties();
		
		// old version
//		String propertyStrings[] = propMap.keySet().toArray(new String[1]);
		
		// added by alex		
		Vector<String> tmp = new Vector<String>();
		for (String key: propMap.keySet()){
			if (propMap.get(key).isPropertyToVary()){
				tmp.add(propMap.get(key).getName());
			}
		}
		tmp.add(0, "---");
		
		//String propertyStrings[] = tmp.toArray(new String[1]);		
		
		cBox = new JComboBox[numOfPropsToVary];
		cBox[0] = new JComboBox<String>(tmp);
		cBox[0].setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxx");
		cBox[1] = new JComboBox<String>(tmp);
		cBox[1].setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxx");
		addBoxListener(cBox[0]);
		addBoxListener(cBox[1]);

		propElement = new JTextField[numOfPropsToVary];
		propElement[0] = new JTextField();
		propElement[1] = new JTextField();
		addTextListener(propElement[0]);
		addTextListener(propElement[1]);

		value = new HelpPropValues[numOfPropsToVary];
		value[0] = null;
		value[1] = null;

		propType = new Class[numOfPropsToVary];

		this.boxToIndexMap = new HashMap<>();
		this.boxToIndexMap.put(cBox[0], 0);
		this.boxToIndexMap.put(cBox[1], 1);

		this.propToIndexMap = new HashMap<>();
		this.propToIndexMap.put(propElement[0], 0);
		this.propToIndexMap.put(propElement[1], 1);
		this.add(cBox[0], "growx, wrap");
		this.add(propElement[0], "growx, wrap");
		this.add(cBox[1], "growx, wrap");
		this.add(propElement[1], "growx");
		// this.add(wrapper1);

		// JPanel wrapper2 = new JPanel(new BorderLayout());
		// wrapper2.add(propElement[0], BorderLayout.CENTER);
		// this.add(wrapper2);
		//
		// JPanel wrapper3 = new JPanel(new BorderLayout());
		// wrapper3.add(cBox[1], BorderLayout.CENTER);
		// this.add(wrapper3);
		//
		// JPanel wrapper4 = new JPanel(new BorderLayout());
		// wrapper4.add(propElement[1], BorderLayout.CENTER);
		// this.add(wrapper4);

		comboboxChanged(cBox[0]);
		comboboxChanged(cBox[1]);

	}

	private void comboboxChanged(JComboBox<String> ComboBox) {

		int index = this.boxToIndexMap.get(ComboBox);
		JTextField currentElement = propElement[index];

		currentElement.setText("");
		value[index] = null;
		

		String currentItem = (String) ComboBox.getSelectedItem();
		if (currentItem.equals("---")) {
			propElement[index].setEnabled(false);
			propType[index] = null;
			if (index == 0) {
				cBox[1].setSelectedItem("---");
				this.cBox[1].setEnabled(false);
				this.propElement[1].setEnabled(false);
				
			}
		} else {
			propElement[index].setEnabled(true);
			
			SimProp tmp = SimPropRegistry.getInstance().getPropertiesByName(currentItem);
			propType[index] = tmp.getValueType();
			logger.log(Level.DEBUG, "Proptype is set to" + propType[index].toString());
		}

		if ((ComboBox == cBox[0]) && (!currentItem.equals("---"))) {
			this.cBox[1].setEnabled(true);			
			String prop = SimPropRegistry.getInstance().getPropertiesByName(currentItem).getPropertyID();
			logger.log(Level.DEBUG,"writing into PROPERTY_TO_VARY: " + prop);
			SimPropRegistry.getInstance().setPropertyToVaryValue("PROPERTY_TO_VARY", prop);
		}
		
		if ((ComboBox == cBox[1]) && (!currentItem.equals("---"))){
			String prop = SimPropRegistry.getInstance().getPropertiesByName(String.valueOf(cBox[1].getSelectedItem())).getPropertyID();
			logger.log(Level.DEBUG,"writing into PROPERTY_TO_VARY: " + prop);
			SimPropRegistry.getInstance().setPropertyToVaryValue("SECOND_PROPERTY_TO_VARY", prop);
		}
		
		if (String.valueOf(cBox[1].getSelectedItem()).equals("---")){
			SimPropRegistry.getInstance().setPropertyToVaryValue("USE_SECOND_PROPERTY_TO_VARY", "FALSE");
		}else{
			SimPropRegistry.getInstance().setPropertyToVaryValue("USE_SECOND_PROPERTY_TO_VARY", "TRUE");
		}

		logger.log(Level.DEBUG, currentElement.getSelectedText());
		this.repaint();
	}

	private void addBoxListener(final JComboBox<String> box) {
		ItemListener il = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					PropVaryElement.this.comboboxChanged(box);					
					
				}
			}
		};
		box.addItemListener(il);
	}

	private void addTextListener(final JTextField field) {
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {

				if (a.getActionCommand() != null) {
					int i = PropVaryElement.this.propToIndexMap.get(field);
					value[i] = new HelpPropValues(a.getActionCommand().toString(), propType[i]);
					logger.log(Level.DEBUG, "PropertyType is: " + value[i].getType().toString());
					logger.log(Level.DEBUG, "PropertyValue is: " + a.getActionCommand().toString());
					boolean validity = value[i].isValid();
					logger.log(Level.DEBUG, "Validity is: " + validity);
					
					if (validity && a.getSource() == propElement[0]){
						SimPropRegistry.getInstance().setPropertyToVaryValue("VALUES_FOR_THE_PROPERTY_TO_VARY", propElement[0].getText());
					}else if (validity && a.getSource() == propElement[1]){
						SimPropRegistry.getInstance().setPropertyToVaryValue("VALUES_FOR_THE_SECOND_PROPERTY_TO_VARY", propElement[1].getText());
					}
				}
			}

		};
		field.addActionListener(al);
	}

	/**
	 * Update
	 */
	public void update() {
		logger.log(Level.DEBUG, "PROPERTY_TO_VARY");
		String id = SimPropRegistry.getInstance().getPropertiesToVary().get("PROPERTY_TO_VARY");
		logger.log(Level.DEBUG, id);
		String name = SimPropRegistry.getInstance().getPropertieNameByID(id);
		logger.log(Level.DEBUG, name);
		cBox[0].setSelectedItem(name);
		
		logger.log(Level.DEBUG, "VALUES_FOR_THE_PROPERTY_TO_VARY");
		String values = SimPropRegistry.getInstance().getPropertiesToVary().get("VALUES_FOR_THE_PROPERTY_TO_VARY");
		logger.log(Level.DEBUG, values);
		propElement[0].setText(values);
		
		logger.log(Level.DEBUG, "USE_SECOND_PROPERTY_TO_VARY");
		logger.log(Level.DEBUG, SimPropRegistry.getInstance().getPropertiesToVary().get("USE_SECOND_PROPERTY_TO_VARY"));
		
		logger.log(Level.DEBUG, SimPropRegistry.getInstance().getPropertiesToVary().get("SECOND_PROPERTY_TO_VARY"));
		id = SimPropRegistry.getInstance().getPropertiesToVary().get("SECOND_PROPERTY_TO_VARY");
		logger.log(Level.DEBUG, id);
		name = SimPropRegistry.getInstance().getPropertieNameByID(id);
		logger.log(Level.DEBUG, name);
		cBox[1].setSelectedItem(name);
		
		logger.log(Level.DEBUG, SimPropRegistry.getInstance().getPropertiesToVary().get("VALUES_FOR_THE_SECOND_PROPERTY_TO_VARY"));
		
		logger.log(Level.DEBUG, "VALUES_FOR_THE_SECOND_PROPERTY_TO_VARY");
		values = SimPropRegistry.getInstance().getPropertiesToVary().get("VALUES_FOR_THE_SECOND_PROPERTY_TO_VARY");
		logger.log(Level.DEBUG, values);
		propElement[1].setText(values);
	}

}
