package evaluation.simulator.gui.customElements.configElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import evaluation.simulator.annotations.simulationProperty.FloatProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

@SuppressWarnings("serial")
public class FloatConfigElement extends JPanel implements ChangeListener, ActionListener, ItemListener {

	FloatProp property;
	JCheckBox auto;
	JCheckBox unlimited;
	JSpinner spinner;
	
	SimPropRegistry simPropRegistry;
	
	public FloatConfigElement(FloatProp simProp) {
		
		simPropRegistry = SimPropRegistry.getInstance();
		
		this.property = simProp;
		
		this.auto = new JCheckBox("auto");
//		this.auto.addActionListener( this );
		this.auto.addItemListener( this );
//		this.auto.addChangeListener( this );
		
		this.unlimited = new JCheckBox("unlimited");
//		this.unlimited.addActionListener( this );
		this.unlimited.addItemListener( this );
//		this.unlimited.addChangeListener( this );
		
		this.spinner = new JSpinner();
		this.spinner.setModel( new SpinnerNumberModel( (float) simProp.getValue(),
				simProp.getMinValue(),
				simProp.getMaxValue(),
				simProp.getStepSize()) );
		this.spinner.addChangeListener( this );
		this.spinner.setToolTipText(property.getTooltip());
		
		MigLayout migLayout = new MigLayout("","[grow]","");
		this.setLayout(migLayout);
		
		this.setBorder(BorderFactory.createTitledBorder(property.getName()));
//		this.add( new JLabel(simProp.getName()), "wrap" );
		this.add( this.spinner, "growx, push, wrap" );
		
		if (simProp.getEnableAuto()){
			this.add(auto, "wrap");
		}
		
		if (simProp.getEnableUnlimited()){
			this.add(unlimited, "push");;
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		if ( event.getSource() == this.spinner ){
			simPropRegistry.setValue(this.property.getPropertyID(), this.spinner.getValue());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {

	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if ( this.auto.isSelected() ){
			this.unlimited.setEnabled(false);
			this.spinner.setEnabled(false);
		}else{
			this.unlimited.setEnabled(true);
			this.spinner.setEnabled(true);
		}
		
		if ( this.unlimited.isSelected() ){
			this.auto.setEnabled(false);
			this.spinner.setEnabled(false);
		}else{
			this.auto.setEnabled(true);
			this.spinner.setEnabled(true);
		}
	}
}
