package gui.customElements.accordion;

import gui.service.DescriptionService;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JTable;

import annotations.SimProp;

public class AccordionMouseMotionAdapter implements MouseMotionListener {

	JTable table;
	List<SimProp>  list;
	
	public AccordionMouseMotionAdapter(List<SimProp>  l, JTable tab) {
		table = tab;
		list = l;
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent evt) {
            Point p = evt.getPoint();
			int y = table.rowAtPoint(p);
            DescriptionService ds = DescriptionService.getInstance();
			ds.setDescription(list.get(y).getDescription());
    }

}
