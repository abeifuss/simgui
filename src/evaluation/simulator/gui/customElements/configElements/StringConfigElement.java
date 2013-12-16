package evaluation.simulator.gui.customElements.configElements;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import evaluation.simulator.annotations.simulationProperty.StringProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

@SuppressWarnings("serial")
public class StringConfigElement extends JPanel implements ActionListener, Observer {

	StringProp property;
	JTextField textfield;
	JComboBox<String> jComboBox;
	
	SimPropRegistry simPropRegistry;
	private Component component;
	
	public StringConfigElement(StringProp s) {
		
		simPropRegistry = SimPropRegistry.getInstance();
		
		this.property = s;
		
		MigLayout migLayout = new MigLayout("","[grow]","");
		this.setLayout(migLayout);
		
		this.setBorder(BorderFactory.createTitledBorder(property.getName()));
		
		if ( !this.property.getPossibleValues().equals("") ){
			this.jComboBox = new JComboBox<String>();
			StringTokenizer st = new StringTokenizer(this.property.getPossibleValues(), ",");
			while (st.hasMoreTokens()) {
				jComboBox.addItem(st.nextToken());
			}
			this.jComboBox.addActionListener(this);
			this.jComboBox.setToolTipText(property.getTooltip());
			this.add( jComboBox, "growx, push" );
			this.component = this.jComboBox;
		}else{
			this.textfield = new JTextField();
			this.textfield.addActionListener(this);
			this.textfield.setToolTipText(property.getTooltip());
			this.add( textfield, "growx, push" );
			this.component = this.textfield;
		}
		
		if (!property.getInfo().equals("")){
			JTextArea textarea = new JTextArea("Info: " + property.getInfo());
			textarea.setEditable(false);
			textarea.setLineWrap(true);
			this.add( textarea, "growx, growy, push" );
		}
		
		if (!property.getInfo().equals("")){
			JTextArea textarea = new JTextArea("Info: " + property.getInfo());
			textarea.setEditable(false);
			textarea.setLineWrap(true);
			textarea.setWrapStyleWord(true);
			this.add( textarea, "growx, growy" );
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if ( event.getSource() == this.textfield ){
			simPropRegistry.setValue(this.property.getPropertyID(), this.textfield.getText());
		}
		if ( event.getSource() == this.jComboBox ){
			simPropRegistry.setValue(this.property.getPropertyID(), (String) this.jComboBox.getSelectedItem());
		}
	}
	
	// calles when the simproperty changed
	// e.g. when the dependecy checker disables a property
	@Override
	public void update(Observable observable, Object o) {
		
		if ( (boolean)o ){
			this.component.setEnabled(true);
		} else {
			this.component.setEnabled(false);
		}
		
		updateUI();
	}
		
}
