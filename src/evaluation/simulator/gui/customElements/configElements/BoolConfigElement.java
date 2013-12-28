package evaluation.simulator.gui.customElements.configElements;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import evaluation.simulator.annotations.simulationProperty.BoolProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

@SuppressWarnings("serial")
public class BoolConfigElement extends JPanel implements ItemListener, Observer {

	BoolProp property;
	JCheckBox checkbox;
	List<Component> messages;
	
	SimPropRegistry simPropRegistry;
	
	public BoolConfigElement(BoolProp s) {
		
		simPropRegistry = SimPropRegistry.getInstance();
		
		this.property = s;
		simPropRegistry.registerGuiElement(this, property.getPropertyID());
		
		checkbox = new JCheckBox("enable");
		checkbox.addItemListener(this);
		checkbox.setToolTipText(property.getTooltip());
		
		this.messages = new LinkedList<Component>();
		
		MigLayout migLayout = new MigLayout("","[grow]","");
		this.setLayout(migLayout);
		
		this.setBorder(BorderFactory.createTitledBorder(property.getName()));
		this.add( checkbox, "growx, push, wrap" );
		
		if (!property.getInfo().equals("")){
			JTextArea textarea = new JTextArea("Info: " + property.getInfo());
			textarea.setEditable(false);
			textarea.setLineWrap(true);
			textarea.setWrapStyleWord(true);
			this.add( textarea, "growx, growy, push" );
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		simPropRegistry.setValue(this.property.getPropertyID(), this.checkbox.isSelected());
	}
	
	// Called when simporp has changed
	@Override
	public void update(Observable observable, Object o) {
		
		for (Component message : this.messages){
			this.remove(message);
		}
		this.messages.clear();
		
		this.checkbox.setSelected((boolean)simPropRegistry.getValue( property.getPropertyID()).getValue());

		if (property.getWarnings() != null && property.getWarnings().size() > 0){
			JLabel warning = new JLabel(new ImageIcon("etc/img/icons/warning/warning_16.png"));
			
			this.messages.add( warning );
			for (String each : property.getWarnings()){
				this.messages.add( new JLabel(each) );
			}
		}
		
		if (property.getErrors() != null && property.getErrors().size() > 0){
			JLabel error = new JLabel(new ImageIcon("etc/img/icons/error/error_16.png"));
			this.messages.add( error );
			for (String each : property.getErrors()){
				this.messages.add( new JLabel(each) );
			}
		}
		
		for (Component message : this.messages){
			this.add(message, "wrap, push");
		}
		
		updateUI();
	}

}
