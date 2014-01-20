package evaluation.simulator.gui.customElements.accordion;

import java.awt.Component;
import java.util.List;
import java.util.Collections;

import javax.swing.JPanel;


import net.miginfocom.swing.MigLayout;

import evaluation.simulator.annotations.property.BoolProp;
import evaluation.simulator.annotations.property.DoubleProp;
import evaluation.simulator.annotations.property.FloatProp;
import evaluation.simulator.annotations.property.IntProp;
import evaluation.simulator.annotations.property.SimProp;
import evaluation.simulator.annotations.property.StringProp;
import evaluation.simulator.gui.customElements.configElements.BoolConfigElement;
import evaluation.simulator.gui.customElements.configElements.DoubleConfigElement;
import evaluation.simulator.gui.customElements.configElements.FloatConfigElement;
import evaluation.simulator.gui.customElements.configElements.IntConfigElement;
import evaluation.simulator.gui.customElements.configElements.StringConfigElement;
import evaluation.simulator.gui.helper.SimpropComparator;
import evaluation.simulator.gui.helper.ValueComparator;
import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

@SuppressWarnings("serial")
public class PropertyPanel extends JPanel {

	String localName;
	
	public PropertyPanel( String name ) {
		super();
		this.localName = name;
		
		MigLayout migLayout = new MigLayout("","[grow]","[grow]");
		this.setLayout(migLayout);
		
		loadContent();
	}
	
	private void loadContent(){
		
		// Select all global SimProps for the given plugin layer
		SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
		String pluginLayer = this.localName;
		List<SimProp> tmpListOfAllVisibleSimProperties = simPropRegistry.getGlobalSimPropertiesByPluginLayer(pluginLayer);
		Collections.sort(tmpListOfAllVisibleSimProperties,new SimpropComparator());
		
		
		// add content
		for (SimProp simProp : tmpListOfAllVisibleSimProperties ){
			if ( simProp.getValueType() == Integer.class ){
				this.add( new IntConfigElement((IntProp) simProp), "growx, wrap");
			}
			if ( simProp.getValueType() == Float.class ){
				this.add( new FloatConfigElement((FloatProp) simProp), "growx, wrap");
			}
			if ( simProp.getValueType() == Double.class ){
				this.add( new DoubleConfigElement((DoubleProp) simProp), "growx, wrap");
			}
			if ( simProp.getValueType() == Boolean.class ){
				this.add( new BoolConfigElement((BoolProp) simProp), "growx, wrap");
			}
			if ( simProp.getValueType() == String.class ){
				this.add( new StringConfigElement((StringProp) simProp), "growx, wrap");
			}
		}
		DependencyChecker.checkAll(simPropRegistry);
	}
	
	public void realoadContent(String pluginName){
		for ( Component c : this.getComponents()){
			this.remove(c);
		}
		
		SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
		List<SimProp> tmpListOfAllVisibleSimProperties = simPropRegistry.getSimPropertiesByPluginOrPluginLayer(pluginName, this.localName);
		Collections.sort(tmpListOfAllVisibleSimProperties,new SimpropComparator());
		
		for (SimProp simProp : tmpListOfAllVisibleSimProperties ){
			if ( simProp.getValueType() == Integer.class ){
				this.add( new IntConfigElement((IntProp) simProp), "growx, wrap");
			}
			if ( simProp.getValueType() == Float.class ){
				this.add( new FloatConfigElement((FloatProp) simProp), "growx, wrap");
			}
			if ( simProp.getValueType() == Double.class ){
				this.add( new DoubleConfigElement((DoubleProp) simProp), "growx, wrap");
			}
			if ( simProp.getValueType() == Boolean.class ){
				this.add( new BoolConfigElement((BoolProp) simProp), "growx, wrap");
			}
			if ( simProp.getValueType() == String.class ){
				this.add( new StringConfigElement((StringProp) simProp), "growx, wrap");
			}
		}
		
//		loadContent();
	}

}
