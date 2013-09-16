package gui.customElements.accordion;

import gui.service.DescriptionService;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JTable;

import annotations.SimProp;

public class AccordionMouseMotionAdapter implements MouseMotionListener {

	private JTable _table;
	private List<SimProp>  _listOfCategoryEntries;
	
	public AccordionMouseMotionAdapter(List<SimProp>  l, JTable t) {
		_table = t;
		_listOfCategoryEntries = l;
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent event) {
            Point point = event.getPoint();
			int row = _table.rowAtPoint(point);
            DescriptionService ds = DescriptionService.getInstance();
			ds.setDescription(_listOfCategoryEntries.get(row).getDescription());
    }

}
