package evaluation.simulator.gui.customElements.accordion;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import evaluation.simulator.annotations.SimProp;

@SuppressWarnings("serial")
public class AccordionTableCellRenderer extends DefaultTableCellRenderer {

	private final List<SimProp> _toolTipList;

	public AccordionTableCellRenderer(List<SimProp> l) {
		super();
		this._toolTipList = l;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);

		this.setToolTipText(this._toolTipList.get(row).getTooltip());

		if (column == 1) {
			this.setHorizontalAlignment(RIGHT);
		} else {
			this.setHorizontalAlignment(LEFT);
		}

		// Coloring
		this.setForeground(Color.BLACK);
		if ((row % 2) != 0) {
			this.setBackground(new Color(200, 220, 255));
		} else {
			this.setBackground(Color.WHITE);
		}

		return this;
	}
}
