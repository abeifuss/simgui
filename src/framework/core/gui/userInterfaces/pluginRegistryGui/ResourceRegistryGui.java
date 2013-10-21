package framework.core.gui.userInterfaces.pluginRegistryGui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch.DescriptionPanel;
import framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.RepertoryPanelGui;
import framework.core.gui.util.GUIText;

/**
 * PluginRegistryGui is the Gui for managing the existing plugins and registering new ones
 * 
 * @author Marius Fink
 * @version 14.09.2012
 */
public class ResourceRegistryGui {
    private JPanel leftSide;
    private JPanel mainPanel;
    private JSplitPane splitPane;

    public ResourceRegistryGui() {
	init();
    }

    /**
     * Initializes the gui
     */
    private void init() {
	mainPanel = new JPanel(new BorderLayout());
	

	GridLayout gridLayout = new GridLayout(0, 1);
	gridLayout.setVgap(5);
	leftSide = new JPanel(gridLayout);

	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		leftSide, mainPanel);
	splitPane.setOneTouchExpandable(true);
	splitPane.setDividerLocation(200);
	splitPane.setDividerSize(8);
    }

    public void addRepertoryPanel(RepertoryPanelGui choosingPanel) {
	leftSide.add(choosingPanel.getComponent());
	leftSide.updateUI();
    }
    
    /**
     * @return the mainPanel
     */
    public Component getComponent() {
	return splitPane;
    }

    /**
     * Sets the right hand content (updates the UI afterwards)
     * 
     * @param rightHandPanel
     *            the Panel to add on the right
     */
    public void setMainContent(Component rightHandPanel) {
	mainPanel.removeAll();
	mainPanel.add(rightHandPanel, BorderLayout.CENTER);
	mainPanel.updateUI();
    }
    
    /**
     * Removes the main content and displays an info instead.
     */
    public void removeMainContent() {
	mainPanel.removeAll();
	mainPanel.add(new DescriptionPanel(GUIText.getText("nothingSelected")));
	mainPanel.updateUI();
    }
}
