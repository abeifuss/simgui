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
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

@SuppressWarnings("serial")
public class AccordionEntry extends JPanel {
	private final JButton entryButton;
	private JTable entryTable = null;
	private final String localName;

	public AccordionEntry(String name, final JComboBox<String> jComboBox) {
		this.localName = name;

		this.setLayout(new BorderLayout(0, 0));
		this.entryButton = new JButton(this.localName, new ImageIcon(
				"etc/img/icons/green/arrow-144-24.png"));
		this.entryButton.setForeground(Color.BLACK);
		this.entryButton.setHorizontalAlignment(SwingConstants.LEFT);
		this.entryButton.setHorizontalTextPosition(SwingConstants.RIGHT);

		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AccordionEntry.this.toggleVisibility(e.getSource(), jComboBox);
			}
		};

		this.entryButton.addActionListener(actionListener);
		this.entryButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.add(this.entryButton, BorderLayout.NORTH);
		jComboBox.setVisible(false);
		jComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if ( e.getStateChange() == ItemEvent.SELECTED ){
					AccordionEntry.this.comboBoxChanged(jComboBox);
				}

			}
		});
		jComboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); // for
																// automatically
																// resizing the
																// JComboBox
		this.add(jComboBox, BorderLayout.CENTER);
	}

	private void comboBoxChanged(JComboBox<String> jComboBox) {
		if (jComboBox.getSelectedIndex() != 0) {
			
			if ( entryTable != null ){
				remove(entryTable);
			}
			
			Logger.Log( LogLevel.DEBUG, "Reload table");
			SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
			List<SimProp> tmpListOfAllSimPropertiesInANamespace = simPropRegistry.getSimPropertiesByNamespace((String) jComboBox.getSelectedItem());
			
			for (SimProp s : tmpListOfAllSimPropertiesInANamespace ){
				Logger.Log( LogLevel.DEBUG, "Load: " +s.getNamespace() + "::" + s.getName());
			}
			
			this.entryTable = new JTable(new AccordionModel( tmpListOfAllSimPropertiesInANamespace )) {

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

			this.entryTable.addMouseMotionListener(new AccordionMouseMotionAdapter(
							tmpListOfAllSimPropertiesInANamespace, this.entryTable));
			
			this.entryTable.setDefaultRenderer(Object.class, new AccordionTableCellRenderer(
							tmpListOfAllSimPropertiesInANamespace));
			
			this.entryTable.setVisible(true);

			TableColumn col = this.entryTable.getColumnModel().getColumn(1);
			col.setCellEditor(new AccordionCellEditor());
			this.entryTable.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(this.entryTable, BorderLayout.SOUTH);
			this.entryTable.setVisible(true);
			this.updateUI();
		} else {
			// this.remove(this.entryTable);
			this.updateUI();
		}
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
