package gui.customElements.accordion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import annotations.SimProp;

@SuppressWarnings("serial")
public class AccordionEntry extends JPanel {
	
	JButton btn;
	JTable tab;
	
	public AccordionEntry(String name, List<SimProp>  listOfAllSectionsInACategory) {
		
		setLayout(new BorderLayout(0, 0));

		btn = new JButton(name,  new ImageIcon("etc/img/icons/green/arrow-144-24.png"));
		btn.setForeground(Color.BLACK);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setHorizontalTextPosition(SwingConstants.RIGHT);
		
		
		tab = new JTable(new AccordionModel(name)){
			
			// This takes care that the non-editable cells are grayed out.
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
					Component c = super.prepareRenderer(renderer, row, column);
					if (column == 1 && !isCellEditable(row, column)) {
					c.setBackground(new Color(255,200,200));
				}
				return c;
				}
		};
		
		tab.addMouseMotionListener(new AccordionMouseMotionAdapter(listOfAllSectionsInACategory, tab));
		tab.setDefaultRenderer(Object.class, new AccordionTableCellRenderer(listOfAllSectionsInACategory));
		tab.setVisible(false);

		TableColumn col = tab.getColumnModel().getColumn(1);
		col.setCellEditor(new AccordionCellEditor());
		
		ActionListener al = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleVisibility(e.getSource());
			}
		};
		
		btn.addActionListener(al);
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		tab.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.add(btn, BorderLayout.NORTH);
		this.add(tab, BorderLayout.CENTER);
	}
	
	private void toggleVisibility(Object source){
		
		JButton btn = ( JButton ) source;
		if (!tab.isVisible()){
			btn.setIcon( new ImageIcon("etc/img/icons/red/arrow-144-24.png") );
		}else{
			btn.setIcon( new ImageIcon("etc/img/icons/green/arrow-144-24.png") );
		}
		tab.setVisible(!tab.isVisible());
		
		
	}
}
