package evaluation.simulator.gui.customElements.accordion;

import java.awt.Dimension;
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

import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

/**
 * @author nachkonvention
 * 
 * 
 *         DEPRECATED
 * 
 * 
 */
@SuppressWarnings("serial")
public class Accordion extends JScrollPane {

	private final JPanel jPanel;
	private final SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();

	public Accordion() {
		
		this.setMinimumSize( new Dimension(300, 600));

		// Start Layout
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		this.jPanel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraint.anchor = GridBagConstraints.NORTH;
		gridBagConstraint.weightx = 1;
		gridBagConstraint.weighty = 0;
		gridBagConstraint.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraint.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this.jPanel, gridBagConstraint);
		this.jPanel.setLayout(gridBagLayout);

		this.setViewportView(this.jPanel);
		// End Layout

		AccordionEntry acordition;

		List<SimProp> listofAllSections = this.getSections();
		Set<String> listOfAllCategories = new HashSet<String>();

		Iterator<SimProp> iter = listofAllSections.iterator();
		while (iter.hasNext()) {
			String category = iter.next().getPluginLayer();
			listOfAllCategories.add(category);
		}

		// Select the categories
		Iterator<String> categoryIterator = listOfAllCategories.iterator();
		while (categoryIterator.hasNext()) {
			String category = categoryIterator.next();
			
			// Create the list of all elements in a category
			List<SimProp> listOfAllSectionsInACategory = new LinkedList<SimProp>();
			Iterator<SimProp> sectionIterator = listofAllSections.iterator();
			while (sectionIterator.hasNext()) {
				SimProp propToCheck = sectionIterator.next();
				if (propToCheck.getPluginLayer().equals(category)) {
					listOfAllSectionsInACategory.add(propToCheck);
				}
			}
			// TODO nicht benutzen!!!!!
			acordition = new AccordionEntry(category, null);
			this.jPanel.add(acordition, gridBagConstraint);
		}

		gridBagConstraint.weighty = 1;

		// Spring element to push all other elements to the top
		// needed for alignment
		JPanel jp = new JPanel();
		this.jPanel.add(jp, gridBagConstraint);

		this.setVisible(true);
	}

	// Select the sections
	private List<SimProp> getSections() {
		List<SimProp> list = new LinkedList<SimProp>();

		Set<Entry<String, SimProp>> e = this.simPropRegistry.getAllSimProps();

		Iterator<Entry<String, SimProp>> iter = e.iterator();
		while (iter.hasNext()) {
			Entry<?, ?> entry = iter.next();
			list.add((SimProp) entry.getValue());
		}

		return list;
	}

}
