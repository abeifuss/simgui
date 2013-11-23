package evaluation.simulator.gui.customElements.accordion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.conf.service.SimulationConfigService;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class AccordionEntry extends JPanel {
	
	private static Logger logger = Logger.getLogger(AccordionEntry.class);
	
	private final JComboBox<String> comboBox;
	private final JButton entryButton;
	private JTable entryTable = null;
	private final String localName;

	public AccordionEntry(String name, final JComboBox<String> jComboBox) {
		this.localName = name;
		
		if ( jComboBox == null ){
			logger.log(Level.ERROR, "jComboBox == null");
		}
		this.comboBox = jComboBox;

		this.setLayout(new BorderLayout(0, 0));
		this.entryButton = new JButton(this.localName, new ImageIcon(
				"etc/img/icons/green/arrow-144-24.png"));
		this.entryButton.setForeground(Color.BLACK);
		this.entryButton.setHorizontalAlignment(SwingConstants.LEFT);
		this.entryButton.setHorizontalTextPosition(SwingConstants.RIGHT);

		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AccordionEntry.this.toggleVisibility(e.getSource(),AccordionEntry.this.comboBox);
			}
		};

		this.entryButton.addActionListener(actionListener);
		this.entryButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.add(this.entryButton, BorderLayout.NORTH);
		this.comboBox.setVisible(false);
		this.comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {
					AccordionEntry.this.comboBoxChanged(AccordionEntry.this.comboBox);
				}

			}
		});
		this.comboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); 
		
		if ( this.comboBox.getModel().getSize() > 1 ){
			this.add(this.comboBox, BorderLayout.CENTER);
		}
		
		setDefaultTable();
		
	}

	private void comboBoxChanged(JComboBox<String> jComboBox) {
		if (jComboBox.getSelectedIndex() != 0) {

			if (this.entryTable != null) {
				this.setDefaultTable();
			}

			logger.log(Level.DEBUG, "Reload table");
			SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();

			String pluginLevel = this.localName;
			String pluginName = (String) jComboBox.getSelectedItem();

			logger.log( Level.DEBUG, "Set plugin-level " + pluginLevel + " to " + pluginName);
			simPropRegistry.setActivePlugins(pluginLevel, pluginName); // GGF Mapped
			
			// Select all SimProps which either match the plugin or are global in the plugin layer
			List<SimProp> tmpListOfAllSimPropertiesForPlugin = simPropRegistry.getSimPropertiesByPluginOrPluginLayer(pluginName, pluginLevel);
			
			for (SimProp s : tmpListOfAllSimPropertiesForPlugin ){
				logger.log( Level.DEBUG, "Load: " +s.getPluginID() + "::" + s.getName());

			}

			this.entryTable = new JTable(new AccordionModel(
					tmpListOfAllSimPropertiesForPlugin)) {

				// This takes care that the non-editable cells are grayed out.
				@Override
				public Component prepareRenderer(TableCellRenderer renderer,
						int row, int column) {
					Component c = super.prepareRenderer(renderer, row, column);
					if ((column == 1) && !this.isCellEditable(row, column)) {
						c.setBackground(new Color(255, 200, 200));
					}
					return c;
				}
			};

			this.entryTable
					.addMouseMotionListener(new AccordionMouseMotionAdapter(
							tmpListOfAllSimPropertiesForPlugin,
							this.entryTable));

			this.entryTable.setDefaultRenderer(Object.class,
					new AccordionTableCellRenderer(
							tmpListOfAllSimPropertiesForPlugin));

			this.entryTable.setVisible(true);

			TableColumn col = this.entryTable.getColumnModel().getColumn(1);
			col.setCellEditor(new AccordionCellEditor());
			this.entryTable.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(this.entryTable, BorderLayout.SOUTH);
			this.entryTable.setVisible(true);
			this.updateUI();
		} else {
			this.remove(this.entryTable);
			this.updateUI();
		}
	}

	private void setDefaultTable() {
		if (this.entryTable != null) {
			this.remove(this.entryTable);
		}
		
		// Select all global SimProps for the given plugin layer
		SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
		String pluginLayer = this.localName;
		List<SimProp> tmpListOfAllVisibleSimProperties = simPropRegistry.getGlobalSimPropertiesByPluginLayer(pluginLayer);
	
		for (SimProp s : tmpListOfAllVisibleSimProperties ){
			logger.log( Level.DEBUG, "Load " +s.getPluginID() + " " + s.getName());
		}
		
		this.entryTable = new JTable(new AccordionModel(
				tmpListOfAllVisibleSimProperties)) {

			// This takes care that the non-editable cells are grayed out.
			@Override
			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if ((column == 1) && !this.isCellEditable(row, column)) {
					c.setBackground(new Color(255, 200, 200));
				}
				return c;
			}
		};

		this.entryTable
				.addMouseMotionListener(new AccordionMouseMotionAdapter(
						tmpListOfAllVisibleSimProperties,
						this.entryTable));

		this.entryTable.setDefaultRenderer(Object.class,
				new AccordionTableCellRenderer(
						tmpListOfAllVisibleSimProperties));

		this.entryTable.setVisible(true);

		TableColumn col = this.entryTable.getColumnModel().getColumn(1);
		col.setCellEditor(new AccordionCellEditor());
		this.entryTable.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(this.entryTable, BorderLayout.SOUTH);
		this.entryTable.setVisible(false);
		this.updateUI();
		
	}

	public void setVibility(boolean b) {

		this.comboBox.setVisible(b);
		if (this.entryTable != null) {
			this.entryTable.setVisible(b);
		}
		this.entryButton.setIcon(new ImageIcon(
				"etc/img/icons/red/arrow-144-24.png"));
	}

	private void toggleVisibility(Object source, JComboBox<String> jComboBox) {

		JButton btn = (JButton) source;
		if (this.entryTable == null) {
			if (!jComboBox.isVisible()) {
				btn.setIcon(new ImageIcon("etc/img/icons/red/arrow-144-24.png"));
			} else {
				btn.setIcon(new ImageIcon(
						"etc/img/icons/green/arrow-144-24.png"));
			}
			jComboBox.setVisible(!jComboBox.isVisible());
		} else {
			if (!this.entryTable.isVisible()) {
				btn.setIcon(new ImageIcon("etc/img/icons/red/arrow-144-24.png"));
			} else {
				btn.setIcon(new ImageIcon(
						"etc/img/icons/green/arrow-144-24.png"));
			}
			this.entryTable.setVisible(!this.entryTable.isVisible());
			jComboBox.setVisible(!jComboBox.isVisible());
		}
		this.repaint();
	}
}
