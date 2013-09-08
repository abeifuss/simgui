package gui.customElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import gui.pluginRegistry.SimPropRegistry;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PlugInSelection extends JPanel{

	JComboBox<String> plugInLevel1List;
	JComboBox<String> plugInLevel2List;
	JComboBox<String> plugInLevel3List;
	JComboBox<String> plugInLevel4List;
	JComboBox<String> plugInLevel5List;
				
	public PlugInSelection() {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		
		Map<String,String>[] PluginLayerMap = gcr.getPlugIns();		
		
		String[] levelStrings[]= {	PluginLayerMap[0].keySet().toArray(new String[PluginLayerMap[0].size()]),
									PluginLayerMap[1].keySet().toArray(new String[PluginLayerMap[1].size()]),
									PluginLayerMap[2].keySet().toArray(new String[PluginLayerMap[2].size()]),
									PluginLayerMap[3].keySet().toArray(new String[PluginLayerMap[3].size()]),
									PluginLayerMap[4].keySet().toArray(new String[PluginLayerMap[4].size()])
								 };		
				
		plugInLevel1List = new JComboBox<String>(levelStrings[0]);		
		plugInLevel2List = new JComboBox<String>(levelStrings[1]);
		plugInLevel3List = new JComboBox<String>(levelStrings[2]);
		plugInLevel4List = new JComboBox<String>(levelStrings[3]);
		plugInLevel5List = new JComboBox<String>(levelStrings[4]);
		
		plugInLevel2List.setEnabled(false);
		plugInLevel3List.setEnabled(false);
		plugInLevel4List.setEnabled(false);
		plugInLevel5List.setEnabled(false);
		
		plugInLevel1List.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedL1PlugIn();
			}
		});
		
		plugInLevel2List.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedL2PlugIn();
			}
		});
		
		plugInLevel3List.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedL3PlugIn();
			}
		});
		
		plugInLevel4List.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedL4PlugIn();
			}
		});
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(plugInLevel1List);
		this.add(plugInLevel2List);
		this.add(plugInLevel3List);
		this.add(plugInLevel4List);
		this.add(plugInLevel5List);
	}
	
	public void selectedL1PlugIn(){
		if ((String)plugInLevel1List.getSelectedItem() != " ") {
			plugInLevel2List.setEnabled(true);
		}
	}
	
	public void selectedL2PlugIn(){
		if ((String)plugInLevel1List.getSelectedItem() != " ") {
			plugInLevel3List.setEnabled(true);
		}
	}
	
	public void selectedL3PlugIn(){
		if ((String)plugInLevel1List.getSelectedItem() != " ") {
			plugInLevel4List.setEnabled(true);
		}
	}
	
	public void selectedL4PlugIn(){
		if ((String)plugInLevel1List.getSelectedItem() != " ") {
			plugInLevel5List.setEnabled(true);
		}
	}
	
}
