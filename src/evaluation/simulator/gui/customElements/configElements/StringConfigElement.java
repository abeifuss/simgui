package evaluation.simulator.gui.customElements.configElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class StringConfigElement extends JPanel implements ActionListener {

	StringProp property;
	JTextField textfield;
	JComboBox<String> jComboBox;
	
	SimPropRegistry simPropRegistry;
	
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
		}else{
			this.textfield = new JTextField();
			this.textfield.addActionListener(this);
			this.textfield.setToolTipText(property.getTooltip());
			this.add( textfield, "growx, push" );
		}
		
		if (!property.getInfo().equals("")){
			JTextArea textarea = new JTextArea("Info: " + property.getInfo());
			textarea.setEditable(false);
			textarea.setLineWrap(true);
			this.add( textarea, "growx, growy, push" );
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
		
}
