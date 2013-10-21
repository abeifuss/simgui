package framework.core.gui.userInterfaces.genericXMLGeneratedGUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import framework.core.gui.util.components.ScrollablePanel;

/**
 * This GenericConfigurationGUI is a simple Container for Configurations - it has the
 * ability to add "main branches", "SettingsGroups", and
 * "Single/Multiple Settings"-Elements.
 * 
 * @author Marius Fink
 * @version 08.08.2012
 */
class GenericXMLGUI {

    private JTabbedPane tabbedPane;
    private JPanel contentPane;
    private JPanel saveButtonPanel;

    /**
     * Creates a new Generic Config GUI
     */
    public GenericXMLGUI() {
	init();
    }

    /**
     * Initializes this window.
     */
    private void init() {
	contentPane = new JPanel(new BorderLayout(10, 0));

	tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

	contentPane.add(tabbedPane, BorderLayout.CENTER);
	contentPane.add(generateSavePanel(), BorderLayout.SOUTH);
    }

    /**
     * @return the Panel with the saving option
     */
    private Component generateSavePanel() {
	JPanel savingPanel = new JPanel(new BorderLayout(10, 0));
	saveButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	savingPanel.add(saveButtonPanel, BorderLayout.EAST);
	return savingPanel;
    }

    /**
     * Adds a tab
     * 
     * @param title
     *            the title of this tab
     * @param nextTabPanel
     *            the Panel
     */
    void addTab(String title, JComponent nextTabPanel) {
	ScrollablePanel panel = new ScrollablePanel(new BorderLayout());
	panel.setScrollableWidth( ScrollablePanel.ScrollableSizeHint.FIT );
	panel.setScrollableBlockIncrement(
	    ScrollablePanel.VERTICAL, ScrollablePanel.IncrementType.PERCENT, 200);
	
	nextTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	panel.add(nextTabPanel, BorderLayout.CENTER);
	JScrollPane scrollPane = new JScrollPane(panel);
	scrollPane.setBorder(null);
	
	tabbedPane.addTab(title, scrollPane);
    }

    /**
     * @return the main pane
     */
    public Component getContentPane() {
	return contentPane;
    }

    /**
     * @return the saveButtonPanel
     */
    public JPanel getSaveButtonPanel() {
	return saveButtonPanel;
    }
}