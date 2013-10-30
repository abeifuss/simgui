package evaluation.simulator.gui.customElements.accordion;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JTable;

import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.gui.service.DescriptionService;

public class AccordionMouseMotionAdapter implements MouseMotionListener {

	private final List<SimProp> simulationProperties;
	private final JTable table;

	public AccordionMouseMotionAdapter(List<SimProp> l, JTable t) {
		this.table = t;
		this.simulationProperties = l;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent event) {
		Point point = event.getPoint();
		int row = this.table.rowAtPoint(point);
		DescriptionService ds = DescriptionService.getInstance();
		ds.setDescription(this.simulationProperties.get(row).getDescription());
	}

}
