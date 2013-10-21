package framework.core.gui.userInterfaces.configurationGui.compositionBranch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.ScrollablePanel;
import framework.core.gui.util.visualUtilities.GUIColors;
import framework.core.gui.util.visualUtilities.ImageLoader;
import framework.core.gui.util.visualUtilities.SpringLayoutUtilities;

/**
 * This tab is the gui for orchestrating the current plug ins to a valid configuration.
 * 
 * @author Marius Fink
 * 
 */
public class CompositionBranchGui {
	private JPanel mainPanel;
	private int numberOfRows = 0;
	private JPanel layerRowPanel;
	private JPanel innerRowPanel;
	private JButton refreshButton;

	/**
	 * Create a new instance
	 */
	public CompositionBranchGui(JPanel filterPanel) {
		init(filterPanel);
	}

	/**
	 * Initializes this component
	 */
	private void init(JPanel filterPanel) {
		JPanel compositionPanel = new ScrollablePanel(new BorderLayout());

		layerRowPanel = new JPanel(new BorderLayout());
		innerRowPanel = new JPanel(new SpringLayout());

		layerRowPanel.add(innerRowPanel, BorderLayout.CENTER);
		// will be filled afterwards
		compositionPanel.add(layerRowPanel, BorderLayout.NORTH);

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(filterPanel, BorderLayout.NORTH);
		mainPanel.add(addRefreshButton(compositionPanel), BorderLayout.CENTER);
	}

	private Component addRefreshButton(JPanel compositionPanel) {
		refreshButton = new JButton(ImageLoader.loadIcon("refresh"));
		refreshButton.setMargin(new Insets(0, 0, 0, 0));
		
		JPanel topBar = new JPanel(new BorderLayout(5, 1));
		topBar.add(refreshButton, BorderLayout.EAST);
		JLabel titleLabel = new JLabel(GUIText.getText("composition"));
		titleLabel.setFont(titleLabel.getFont().deriveFont(20f));
		topBar.add(titleLabel, BorderLayout.WEST);
		topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GUIColors.getColorByName("metagray")));
		
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.add(topBar, BorderLayout.NORTH);
		wrapper.add(compositionPanel, BorderLayout.CENTER);
		
		return wrapper;
	}

	/**
	 * Adds a row with 2 drop down services. Call <code>drawRows()</code> afterwards.
	 * 
	 * @param mixDropDownServicePanel
	 *            the mic drop down service
	 * @param clientDropDownServicePanel
	 *            the client drop down service
	 */
	public void addRow(JPanel mixDropDownServicePanel, JPanel clientDropDownServicePanel) {
		numberOfRows++;

		JLabel number = new JLabel(numberOfRows + "");
		JLabel text = new JLabel(GUIText.getText("layer" + numberOfRows));

		innerRowPanel.add(number);
		innerRowPanel.add(text);
		innerRowPanel.add(mixDropDownServicePanel);
		innerRowPanel.add(clientDropDownServicePanel);
	}

	/**
	 * Draws all current rows. Has to be called after all rows were added.
	 */
	public void drawRows() {

		SpringLayoutUtilities.makeCompactGrid(innerRowPanel, // parent
				numberOfRows, 4, // rows, cols
				0, 0, // initX, initY
				30, 3); // xPad, yPad

		layerRowPanel.updateUI();
		innerRowPanel.updateUI();
		mainPanel.updateUI();
	}

	public JComponent getJPanel() {
		return mainPanel;
	}

	/**
	 * @return the refreshButton
	 */
	public JButton getRefreshButton() {
		return refreshButton;
	}

	/**
	 * Removes all rows
	 */
	public void removeAllRows() {
		numberOfRows = 0;
		innerRowPanel.removeAll();
		innerRowPanel.updateUI();
	}
}
