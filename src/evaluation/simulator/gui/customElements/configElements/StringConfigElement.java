package evaluation.simulator.gui.customElements.configElements;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
	List<Component> messages;
	
	public StringConfigElement(StringProp s) {
		
		simPropRegistry = SimPropRegistry.getInstance();
		
		this.property = s;
		simPropRegistry.registerGuiElement(this, property.getPropertyID());
		
		this.messages = new LinkedList<Component>();
		
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
	
	// Called when simporp has changed
	@Override
	public void update(Observable observable, Object o) {
		
		for (Component message : this.messages){
			this.remove(message);
		}
		this.messages.clear();
		
		if ( !this.property.getPossibleValues().equals("") ) {
			this.jComboBox.setSelectedItem((String) simPropRegistry.getValue( property.getPropertyID()).getValue());
		}else{
			this.textfield.setText((String) simPropRegistry.getValue( property.getPropertyID()).getValue());
		}
		
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
