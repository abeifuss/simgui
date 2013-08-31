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

	JComboBox<String> l1List;
	JComboBox<String> l2List;
	JComboBox<String> l3List;
	JComboBox<String> l4List;
	JComboBox<String> l5List;
				
	public PlugInSelection() {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		
		Map<String,String>[] PluginLayerMap = gcr.getPlugIns();		
		
		String[] levelStrings[]= {	PluginLayerMap[0].keySet().toArray(new String[PluginLayerMap[0].size()]),
									PluginLayerMap[1].keySet().toArray(new String[PluginLayerMap[1].size()]),
									PluginLayerMap[2].keySet().toArray(new String[PluginLayerMap[2].size()]),
									PluginLayerMap[3].keySet().toArray(new String[PluginLayerMap[3].size()]),
									PluginLayerMap[4].keySet().toArray(new String[PluginLayerMap[4].size()])
								 };		
				
		l1List = new JComboBox<String>(levelStrings[0]);		
		l2List = new JComboBox<String>(levelStrings[1]);
		l3List = new JComboBox<String>(levelStrings[2]);
		l4List = new JComboBox<String>(levelStrings[3]);
		l5List = new JComboBox<String>(levelStrings[4]);
		
		l2List.setEnabled(false);
		l3List.setEnabled(false);
		l4List.setEnabled(false);
		l5List.setEnabled(false);
		
		l1List.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedL1PlugIn();
			}
		});
		
		l2List.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedL2PlugIn();
			}
		});
		
		l3List.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedL3PlugIn();
			}
		});
		
		l4List.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedL4PlugIn();
			}
		});
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(l1List);
		this.add(l2List);
		this.add(l3List);
		this.add(l4List);
		this.add(l5List);
	}
	
	public void selectedL1PlugIn(){
		if ((String)l1List.getSelectedItem() != " ") {
			l2List.setEnabled(true);
		}
	}
	
	public void selectedL2PlugIn(){
		if ((String)l1List.getSelectedItem() != " ") {
			l3List.setEnabled(true);
		}
	}
	
	public void selectedL3PlugIn(){
		if ((String)l1List.getSelectedItem() != " ") {
			l4List.setEnabled(true);
		}
	}
	
	public void selectedL4PlugIn(){
		if ((String)l1List.getSelectedItem() != " ") {
			l5List.setEnabled(true);
		}
	}
	
}
