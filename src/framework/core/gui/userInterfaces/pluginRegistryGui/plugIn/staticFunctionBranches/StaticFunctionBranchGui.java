package framework.core.gui.userInterfaces.pluginRegistryGui.plugIn.staticFunctionBranches;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.GUIColors;
import framework.core.gui.util.visualUtilities.ImageLoader;

public class StaticFunctionBranchGui {

	private JPanel mainPanel;
	private JButton refreshButton;
	private Map<Integer, MultipleStaticFunctionBoxGuiService> boxServices;
	private String title;

	public StaticFunctionBranchGui(String title, Map<Integer, MultipleStaticFunctionBoxGuiService> boxServices) {
		this.boxServices = boxServices;
		this.title = title;
		refreshUI();
	}

	private void refreshUI() {
		refreshButton = new JButton(ImageLoader.loadIcon("refresh"));
		refreshButton.setToolTipText(GUIText.getText("reloadCurrentAvailableStaticFunction"));
		refreshButton.setMargin(new Insets(0, 0, 0, 0));
		
		JPanel topBar = new JPanel(new BorderLayout(5, 1));
		topBar.add(refreshButton, BorderLayout.EAST);
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(titleLabel.getFont().deriveFont(20f));
		topBar.add(titleLabel, BorderLayout.WEST);
		topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GUIColors.getColorByName("metagray")));

		mainPanel = new JPanel(new BorderLayout(5, 3));
		mainPanel.add(topBar, BorderLayout.NORTH);

		JPanel content = new JPanel(new GridLayout(2, 3, 10, 10));

		for (int i = 1; i <= 5; i++) {
			content.add(boxServices.get(i).getComponent());
		}

		mainPanel.add(content, BorderLayout.CENTER);
	}
	
	public JComponent getComponent() {
		return mainPanel;
	}
	
	public JButton getRefreshButton() {
		return refreshButton;
	}
}
