package gui.customElements.accordion;

import gui.pluginRegistry.SimPropRegistry;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import annotations.SimProp;

@SuppressWarnings("serial")
public class Accordion extends JScrollPane{

	private JPanel _panel;
	private SimPropRegistry _spr = SimPropRegistry.getInstance();
	
	public Accordion() {

		// Start Layout
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		_panel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
	    gbc.weightx = 1;
	    gbc.weighty = 0;
	    gbc.gridx = GridBagConstraints.RELATIVE;
	    gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(_panel, gbc);
		_panel.setLayout(gbl);
		
		setViewportView(_panel);
		// End Layout
		
		AccordionEntry acordition;
		
		List<SimProp> listofAllSections = getSections();
		Set<String> listOfAllCategories = new HashSet<String>();
		
		Iterator<SimProp> iter = listofAllSections.iterator();
		while(iter.hasNext()){
			String category = (String) iter.next().getCategory();
			listOfAllCategories.add(category);
		}
		
		// Select the categories
		Iterator<String> iter2 = listOfAllCategories.iterator();
		while(iter2.hasNext()){
			String category = (String) iter2.next();
			
			// Create the list of all elements in a category
			List<SimProp> listOfAllSectionsInACategory = new LinkedList<SimProp>();
			Iterator<SimProp> iter1 = listofAllSections.iterator();
			while(iter1.hasNext()){
				SimProp propToCheck = iter1.next();
				if (propToCheck.getCategory().equals(category))
					listOfAllSectionsInACategory.add(propToCheck);
			}
			
			acordition = new AccordionEntry(category, listOfAllSectionsInACategory);
			_panel.add( acordition, gbc );
		}
		
		gbc.weighty = 1;
		
		// Spring element to push all other elements to the top
		// needed for alignment
		JPanel jp = new JPanel();
		_panel.add ( jp, gbc );
		
		setVisible(true);
	}

	// Select the sections
	private List<SimProp> getSections() {
		List<SimProp> list = new LinkedList<SimProp>();
		
		Set<Entry<String, SimProp>> e = _spr.getAllSimProps();
		
		Iterator<Entry<String, SimProp>> iter = e.iterator();
	    while (iter.hasNext()) {
	    	Entry<?, ?> entry = (Entry<?, ?>) iter.next();
	    	list.add((SimProp) entry.getValue());
	    }
	    
	    return list;
	}
	
}
