package evaluation.simulator.gui.customElements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

@SuppressWarnings("serial")
public class PlugInSelection extends JPanel {

	JComboBox<String> plugInLevel1List;
	JComboBox<String> plugInLevel2List;
	JComboBox<String> plugInLevel3List;
	JComboBox<String> plugInLevel4List;
	JComboBox<String> plugInLevel5List;
	JComboBox<String> plugInLevel6List;

	public PlugInSelection() {
		SimPropRegistry gcr = SimPropRegistry.getInstance();

		Map<String, String>[] PluginLayerMap = gcr.getPlugIns();

		String[] levelStrings[] = {
				PluginLayerMap[0].keySet().toArray(
						new String[PluginLayerMap[0].size()]),
				PluginLayerMap[1].keySet().toArray(
						new String[PluginLayerMap[1].size()]),
				PluginLayerMap[2].keySet().toArray(
						new String[PluginLayerMap[2].size()]),
				PluginLayerMap[3].keySet().toArray(
						new String[PluginLayerMap[3].size()]),
				PluginLayerMap[4].keySet().toArray(
						new String[PluginLayerMap[4].size()]),
				PluginLayerMap[5].keySet().toArray(
						new String[PluginLayerMap[5].size()]) };

		this.plugInLevel1List = new JComboBox<String>(levelStrings[0]);
		this.plugInLevel2List = new JComboBox<String>(levelStrings[1]);
		this.plugInLevel3List = new JComboBox<String>(levelStrings[2]);
		this.plugInLevel4List = new JComboBox<String>(levelStrings[3]);
		this.plugInLevel5List = new JComboBox<String>(levelStrings[4]);
		this.plugInLevel6List = new JComboBox<String>(levelStrings[5]);

		this.plugInLevel2List.setEnabled(false);
		this.plugInLevel3List.setEnabled(false);
		this.plugInLevel4List.setEnabled(false);
		this.plugInLevel5List.setEnabled(false);
		this.plugInLevel6List.setEnabled(false);

		this.plugInLevel1List.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PlugInSelection.this.selectedL1PlugIn();
			}
		});

		this.plugInLevel2List.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PlugInSelection.this.selectedL2PlugIn();
			}
		});

		this.plugInLevel3List.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PlugInSelection.this.selectedL3PlugIn();
			}
		});

		this.plugInLevel4List.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PlugInSelection.this.selectedL4PlugIn();
			}
		});

		this.plugInLevel5List.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PlugInSelection.this.selectedL5PlugIn();
			}
		});

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(this.plugInLevel1List);
		this.add(this.plugInLevel2List);
		this.add(this.plugInLevel3List);
		this.add(this.plugInLevel4List);
		this.add(this.plugInLevel5List);
		this.add(this.plugInLevel6List);
	}

	public void selectedL1PlugIn() {
		if ((String) this.plugInLevel1List.getSelectedItem() != " ") {
			this.plugInLevel2List.setEnabled(true);
		}
	}

	public void selectedL2PlugIn() {
		if ((String) this.plugInLevel1List.getSelectedItem() != " ") {
			this.plugInLevel3List.setEnabled(true);
		}
	}

	public void selectedL3PlugIn() {
		if ((String) this.plugInLevel1List.getSelectedItem() != " ") {
			this.plugInLevel4List.setEnabled(true);
		}
	}

	public void selectedL4PlugIn() {
		if ((String) this.plugInLevel1List.getSelectedItem() != " ") {
			this.plugInLevel5List.setEnabled(true);
		}
	}

	public void selectedL5PlugIn() {
		if ((String) this.plugInLevel1List.getSelectedItem() != " ") {
			this.plugInLevel6List.setEnabled(true);
		}
	}

}
