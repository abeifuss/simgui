package framework.core.gui.userInterfaces.pluginRegistryGui.plugIn.staticFunctionBranches;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;

import framework.core.gui.model.StaticFunctionBean;
import framework.core.gui.services.AvailableResourcesDAO;
import framework.core.gui.userInterfaces.mainGui.StatusBar;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.PopUpPrompt;

/**
 * This is a generic GUI tab for requirements or capability branches with static functions.
 * 
 * @author Marius Fink
 * 
 */
public class StaticFunctionBranchGuiService {

	private List<StaticFunctionBean> currentStaticFunctions;
	private StaticFunctionBranchGui gui;
	private AvailableResourcesDAO dao;
	public static final short REQUIREMENTS_BRANCH = 0;
	public static final short CAPABILITY_BRANCH = 1;
	private final short branchType;
	private Map<Integer, MultipleStaticFunctionBoxGuiService> services;

	/**
	 * Creates a new StaticFunction branch
	 * 
	 * @param type
	 *            th type of this branch, has to be one of the public constants of this class
	 *            <code>REQUIREMENTS_BRANCH</code> or <code>CAPABILITY_BRANCH</code>
	 */
	public StaticFunctionBranchGuiService(short type) {
		this.branchType = type;
		init();
	}

	private void init() {
		dao = new AvailableResourcesDAO();
		currentStaticFunctions = dao.getAvailableStaticFunctions();

		services = new HashMap<Integer, MultipleStaticFunctionBoxGuiService>();
		for (int i = 1; i <= 5; i++) {
			MultipleStaticFunctionBoxGuiService svc = new MultipleStaticFunctionBoxGuiService(i, filterForLayer(i));
			services.put(i, svc);
		}

		String title = "";
		switch (branchType) {
		case REQUIREMENTS_BRANCH:
			title = GUIText.getText("requirements");
			break;
		default:
			title = GUIText.getText("capabilities");
		}
		gui = new StaticFunctionBranchGui(title, services);

		gui.getRefreshButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentStaticFunctions = dao.getAvailableStaticFunctions();
				validateAndDeleteValues();
			}
		});

	}

	private void validateAndDeleteValues() {
		List<StaticFunctionBean> toBeDeleted = new LinkedList<StaticFunctionBean>();
		for (Entry<Integer, MultipleStaticFunctionBoxGuiService> entry : services.entrySet()) {
			MultipleStaticFunctionBoxGuiService guiService = entry.getValue();
			for (StaticFunctionBean bean : guiService.getStaticFunctions()) {
				if (!currentStaticFunctions.contains(bean)) {
					guiService.removeStaticFunctionFromList(bean);
					toBeDeleted.add(bean);
				}
			}
		}
		if (toBeDeleted.size() > 0) {
			PopUpPrompt.displayGenericErrorMessage(GUIText.getText("configInvalid"),
					GUIText.getText("configInvalidRemovedStaticFunctions", toBeDeleted.size() + ""));
		} else {
			StatusBar.getInstance().showStatus(GUIText.getText("configValidStaticFunction"));
		}
	}

	private List<StaticFunctionBean> filterForLayer(int layer) {
		List<StaticFunctionBean> filtered = new LinkedList<StaticFunctionBean>();
		for (StaticFunctionBean bean : currentStaticFunctions) {
			if (bean.getLayer() == layer) {
				filtered.add(bean);
			}
		}
		return filtered;
	}

	public JComponent getComponent() {
		return gui.getComponent();
	}

	public Map<Integer, MultipleStaticFunctionBoxGuiService> getBoxServices() {
		return services;
	}

	/**
	 * Displays the given beans
	 * 
	 * @param sfBeans
	 *            the beans to display
	 */
	public void addStaticFunctions(List<StaticFunctionBean> sfBeans) {
		for (StaticFunctionBean sfb : sfBeans) {
			services.get(sfb.getLayer()).addStaticFunctionToList(sfb);
		}
	}
}
