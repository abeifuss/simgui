package evaluation.simulator.gui.customElements.configElements;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import evaluation.simulator.annotations.simulationProperty.BoolProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

@SuppressWarnings("serial")
public class BoolConfigElement extends JPanel implements ItemListener {

	BoolProp property;
	JCheckBox checkbox;
	
	SimPropRegistry simPropRegistry;
	
	public BoolConfigElement(BoolProp s) {
		
		simPropRegistry = SimPropRegistry.getInstance();
		
		this.property = s;
		
		checkbox = new JCheckBox("enable");
		checkbox.addItemListener(this);
		checkbox.setToolTipText(property.getTooltip());
		
		MigLayout migLayout = new MigLayout("","[grow]","");
		this.setLayout(migLayout);
		
		this.setBorder(BorderFactory.createTitledBorder(property.getName()));
//		this.add( new JLabel(s.getName()), "wrap" );
		this.add( checkbox, "growx, push, wrap" );
		
		if (!property.getInfo().equals("")){
			JTextArea textarea = new JTextArea("Info: " + property.getInfo());
			textarea.setEditable(false);
			textarea.setLineWrap(true);
			this.add( textarea, "growx, growy" );
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		simPropRegistry.setValue(this.property.getPropertyID(), this.checkbox.isSelected());
	}

}
