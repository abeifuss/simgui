package framework.core.gui.userInterfaces.pluginRegistryGui.plugIn.staticFunctionBranches;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import framework.core.gui.model.StaticFunctionBean;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.ImageLoader;

public class MultipleStaticFunctionBoxGuiService {

	private MultipleStaticFunctionBoxGui gui;
	private int layer;
	private List<StaticFunctionBean> availableStaticFunctions;
	private List<StaticFunctionBean> addedStaticFunctions;

	public MultipleStaticFunctionBoxGuiService(int layer, List<StaticFunctionBean> availableStaticFunctions) {
		this.layer = layer;
		this.availableStaticFunctions = availableStaticFunctions;
		this.addedStaticFunctions = new LinkedList<StaticFunctionBean>();
		init();
	}

	private void init() {
		gui = new MultipleStaticFunctionBoxGui(GUIText.getText("layer" + layer));

		handleButtonStates();

		gui.getAddButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				StaticFunctionDropDownMenu menu = new StaticFunctionDropDownMenu();

				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		gui.getRemoveButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedIndex = gui.getList().getSelectedIndex();
				if (selectedIndex != -1) {
					removeStaticFunctionFromList(selectedIndex);
				}
			}
		});
		
		gui.getList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				handleButtonStates();
			}
		});
	}

	/**
	 * Enables or disables the add and remove button based on the current states.
	 */
	private void handleButtonStates() {
		if (gui.getList().getSelectedIndex() == -1) {
			gui.getRemoveButton().setEnabled(false);
		} else {
			gui.getRemoveButton().setEnabled(true);
		}

		if (availableStaticFunctions.size() - addedStaticFunctions.size() == 0) {
			gui.getAddButton().setEnabled(false);
		} else {
			gui.getAddButton().setEnabled(true);
		}
	}

	public JComponent getComponent() {
		return gui.getComponent();
	}

	public void removeStaticFunctionFromList(int index) {
		gui.getListModel().remove(index);
		addedStaticFunctions.remove(index);

		handleButtonStates();
	}
	
	public void removeStaticFunctionFromList(StaticFunctionBean bean) {
		gui.getListModel().removeElement(bean);
		addedStaticFunctions.remove(bean);
		handleButtonStates();
	}

	public void addStaticFunctionToList(StaticFunctionBean bean) {
		gui.getListModel().addElement(bean);
		addedStaticFunctions.add(bean);

		handleButtonStates();
	}

	public List<StaticFunctionBean> getStaticFunctions() {
		return new LinkedList<StaticFunctionBean>(addedStaticFunctions);
	}

	private class StaticFunctionDropDownMenu extends JPopupMenu {
		private static final long serialVersionUID = -5158585882823373483L;

		public StaticFunctionDropDownMenu() {
			List<StaticFunctionBean> nonDisplayedStaticFunctions = new LinkedList<StaticFunctionBean>(
					availableStaticFunctions);
			nonDisplayedStaticFunctions.removeAll(addedStaticFunctions);
			for (final StaticFunctionBean bean : nonDisplayedStaticFunctions) {
				JMenuItem anItem = new JMenuItem(bean.getName(), ImageLoader.loadIcon("function", 10));
				anItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						addStaticFunctionToList(bean);
					}
				});
				add(anItem);
			}
		}
	}
}
