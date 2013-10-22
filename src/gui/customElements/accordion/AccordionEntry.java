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

import annotations.simulationProperty.SimProp;

@SuppressWarnings("serial")
public class AccordionEntry extends JPanel {
	
	private JButton _entryButton;
	private JTable _entryTable;
	
	public AccordionEntry(String name, List<SimProp>  listOfAllSectionsInACategory) {
		
		setLayout(new BorderLayout(0, 0));

		_entryButton = new JButton(name,  new ImageIcon("etc/img/icons/green/arrow-144-24.png"));
		_entryButton.setForeground(Color.BLACK);
		_entryButton.setHorizontalAlignment(SwingConstants.LEFT);
		_entryButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		
		
		_entryTable = new JTable(new AccordionModel(name)){
			
			// This takes care that the non-editable cells are grayed out.
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
					Component c = super.prepareRenderer(renderer, row, column);
					if (column == 1 && !isCellEditable(row, column)) {
					c.setBackground(new Color(255,200,200));
				}
				return c;
				}
		};
		
		_entryTable.addMouseMotionListener(new AccordionMouseMotionAdapter(listOfAllSectionsInACategory, _entryTable));
		_entryTable.setDefaultRenderer(Object.class, new AccordionTableCellRenderer(listOfAllSectionsInACategory));
		_entryTable.setVisible(false);

		TableColumn col = _entryTable.getColumnModel().getColumn(1);
		col.setCellEditor(new AccordionCellEditor());
		
		ActionListener al = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleVisibility(e.getSource());
			}
		};
		
		_entryButton.addActionListener(al);
		_entryButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		_entryTable.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.add(_entryButton, BorderLayout.NORTH);
		this.add(_entryTable, BorderLayout.CENTER);
	}
	
	private void toggleVisibility(Object source){
		
		JButton btn = ( JButton ) source;
		if (!_entryTable.isVisible()){
			btn.setIcon( new ImageIcon("etc/img/icons/red/arrow-144-24.png") );
		}else{
			btn.setIcon( new ImageIcon("etc/img/icons/green/arrow-144-24.png") );
		}
		_entryTable.setVisible(!_entryTable.isVisible());
		
		
	}
}
