package gui.customElements.accordion;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import annotations.simulationProperty.SimProp;

@SuppressWarnings("serial")
public class AccordionTableCellRenderer extends DefaultTableCellRenderer {

	private List<SimProp> _toolTipList;

	public AccordionTableCellRenderer(List<SimProp> l) {
		super();
		_toolTipList = l;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		setToolTipText(_toolTipList.get(row).getTooltip());

		if (column == 1) {
			setHorizontalAlignment(RIGHT);
		} else {
			setHorizontalAlignment(LEFT);
		}

		// Coloring
		setForeground(Color.BLACK);
		if (row % 2 != 0) {
			setBackground(new Color(200, 220, 255));
		} else {
			setBackground(Color.WHITE);
		}
		
		return this;
	}
}
