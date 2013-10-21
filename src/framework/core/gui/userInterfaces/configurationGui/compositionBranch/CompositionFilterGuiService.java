package framework.core.gui.userInterfaces.configurationGui.compositionBranch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import framework.core.gui.model.PlugInFilter;
import framework.core.gui.model.PlugInFilterModel;
import framework.core.gui.services.CompositionFilterService;
import framework.core.gui.userInterfaces.configurationGui.compositionBranch.CompositionFilterGui.FilterRow;

/**
 * The GUI service for the generic filters. Specifiy filters at:
 * {@link framework.core.gui.services.CompositionFilterService}
 * 
 * @author Marius Fink
 * 
 */
public class CompositionFilterGuiService {
	private CompositionFilterGui gui;
	private CompositionFilterService service;
	private Map<FilterRow, PlugInFilterModel> filterMapper = new HashMap<FilterRow, PlugInFilterModel>();

	/**
	 * Initialize this service with the current displayed plug-ins
	 * 
	 * @param currentDisplayedPlugIns
	 *            the plug-ins thar are currently displayed
	 */
	public CompositionFilterGuiService(CompositionFilterService filterService) {
		init();
		service = filterService;
	}

	/**
	 * Initializes the component.
	 */
	private void init() {
		gui = new CompositionFilterGui();

		for (PlugInFilterModel filter : CompositionFilterService.getFilters()) {
			filterMapper.put(gui.addFilterRow(filter.getPossibleValues(), filter.getName()), filter);
		}
		gui.drawRows();

		gui.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				filter();
			}

		});
	}
	
	/**
	 * Starts the filtering
	 */
	void filter() {
		List<PlugInFilter> filters = new LinkedList<PlugInFilter>();
		
		for (Entry<FilterRow, PlugInFilterModel> entry : filterMapper.entrySet()) {
			if (entry.getKey().isActivated()) {
				filters.add(new PlugInFilter(entry.getValue(), entry.getKey().getCurrentValue()));
			}
		}
		
		service.filter(filters);
	}

	/**
	 * @return the panel of this component
	 */
	public JPanel getComponent() {
		return gui.getPanel();
	}
}
