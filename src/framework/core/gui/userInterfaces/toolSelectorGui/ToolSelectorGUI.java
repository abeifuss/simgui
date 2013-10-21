/*
 * gMix open source project - https://svs.informatik.uni-hamburg.de/gmix/
 * Copyright (C) 2011  Karl-Peter Fuchs
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package framework.core.gui.userInterfaces.toolSelectorGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.JTextFieldContextMenu;
import framework.core.gui.util.visualUtilities.ImageLoader;
import framework.core.gui.util.visualUtilities.SpringLayoutUtilities;

/**
 * The concrete GUI class without logic
 */
public class ToolSelectorGUI {

	private List<JToggleButton> buttons = new LinkedList<JToggleButton>();
	private JTextPane infoTextPane;
	private JButton overwriteButton;
	private JTextFieldContextMenu globalConfigPathTextField;
	private JButton startButton;
	private JButton consoleBtn;
	private JLabel globalConfigLabel, overwriteLabel;
	private Component contentPane;
	private JPanel buttonPanel;
	private JLabel overwriteChangesLabel;

	/**
	 * Constructor of class ToolSelectorGUI opens up the starter GUI
	 * 
	 * @param params
	 *            the params
	 */
	public ToolSelectorGUI() {
		infoTextPane = generateTextPane();

		consoleBtn = new JButton(ImageLoader.loadIcon("console"));

		init();
	}

	/**
	 * Creates the textpane
	 * 
	 * @return the info text area as JTextPane
	 */
	private JTextPane generateTextPane() {
		JTextPane jtp = new JTextPane();
		jtp.setBackground(Color.WHITE);
		jtp.setEditable(false);
		jtp.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 20));
		jtp.setFont(new Font("Monospaced", Font.PLAIN, 15));

		return jtp;
	}

	/**
	 * Shows the GUI
	 */
	public void init() {
		contentPane = generateMainContentPanel();
	}

	/**
	 * Builds the Main Content panel with the Buttons and the description area
	 * 
	 * @return the main content panel
	 */
	private Component generateMainContentPanel() {
		buttonPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(0, 1);
		gridLayout.setVgap(5);
		buttonPanel.setLayout(gridLayout);
		for (JToggleButton btn : buttons) {
			buttonPanel.add(btn);
		}

		JPanel infoIconPanel = new JPanel(new BorderLayout());
		infoIconPanel.add(new JLabel(ImageLoader.loadIcon("info")), BorderLayout.NORTH);
		infoIconPanel.setBackground(Color.WHITE);

		consoleBtn.setPreferredSize(new Dimension(25, 25));
		consoleBtn.setBackground(Color.WHITE);
		consoleBtn.setToolTipText(GUIText.getText("consoleTooltip"));
		infoIconPanel.add(consoleBtn, BorderLayout.SOUTH);

		JScrollPane jScrollPane = new JScrollPane(infoTextPane);
		jScrollPane.setBorder(null);

		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		infoPanel.add(infoIconPanel, BorderLayout.WEST);
		infoPanel.add(jScrollPane, BorderLayout.CENTER);

		JPanel rightMainPanel = new JPanel(new BorderLayout());
		rightMainPanel.add(infoPanel, BorderLayout.CENTER);
		rightMainPanel.add(generateCommandLineParametersPanel(), BorderLayout.SOUTH);

		JPanel mainContentPanel = new JPanel(new BorderLayout(5, 5));
		mainContentPanel.add(buttonPanel, BorderLayout.WEST);
		mainContentPanel.add(rightMainPanel, BorderLayout.CENTER);

		return mainContentPanel;
	}

	/**
	 * @return the panel for the command line param input
	 */
	private Component generateCommandLineParametersPanel() {
		JPanel panel = new JPanel(new SpringLayout());

		globalConfigLabel = new JLabel(GUIText.getText("config"));
		panel.add(globalConfigLabel);
		JPanel fileChooserPanel = new JPanel(new BorderLayout());
		globalConfigPathTextField = new JTextFieldContextMenu();
		globalConfigPathTextField.setEnabled(false);
		JButton globalConfigFileChooserBtn = new JButton(ImageLoader.loadIcon("open"));
		globalConfigFileChooserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(false);
				if (fc.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					globalConfigPathTextField.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		globalConfigFileChooserBtn.setMargin(new Insets(0, 0, 0, 0));
		globalConfigFileChooserBtn.setSize(new Dimension(globalConfigPathTextField.getSize().height,
				globalConfigPathTextField.getSize().height));
		fileChooserPanel.add(globalConfigFileChooserBtn, BorderLayout.EAST);
		fileChooserPanel.add(globalConfigPathTextField, BorderLayout.CENTER);
		panel.add(fileChooserPanel);

		overwriteLabel = new JLabel(GUIText.getText("tempChanges"));
		panel.add(overwriteLabel);
		JPanel overwriteWrapper = new JPanel(new BorderLayout(15, 0));
		overwriteButton = new JButton(GUIText.getText("edit"), ImageLoader.loadIcon("edit", 15));
		overwriteWrapper.add(overwriteButton, BorderLayout.CENTER);
		overwriteChangesLabel = new JLabel(GUIText.getText("changesCount", "0"));
		overwriteWrapper.add(overwriteChangesLabel, BorderLayout.EAST);
		panel.add(overwriteWrapper);

		panel.add(new JPanel());
		startButton = new JButton(GUIText.getText("startTool"), ImageLoader.loadIcon("start"));
		startButton.setFont(startButton.getFont().deriveFont(15f));
		panel.add(startButton);

		SpringLayoutUtilities.makeCompactGrid(panel, // parent
				3, 2, // rows, cols
				3, 3, // initX, initY
				3, 3); // xPad, yPad

		panel.setBorder(BorderFactory.createTitledBorder(GUIText.getText("commandLineSettingsTitle")));
		return panel;
	}

	/**
	 * Adds a StartButton and returns it for eg. ActionListener registration
	 * 
	 * @param title
	 *            the title of the button
	 * @return the created button
	 */
	public JToggleButton addToolStartButton(String title) {
		assert title != null : "Precondition transgressed: title != null";
		JToggleButton jb = new JToggleButton(title);
		buttons.add(jb);
		buttonPanel.add(jb);
		return jb;
	}

	/**
	 * @param infoText
	 *            the new infoText of the Action
	 */
	public void setInfoText(String infoText) {
		infoTextPane.setText(infoText);
	}

	/**
	 * @return the path to the config file, entered by the user
	 */
	public JTextField getGlobalConfigFileTextField() {
		return globalConfigPathTextField;
	}

	/**
	 * @return the "Start"-Button of the Tool
	 */
	public JButton getStartButton() {
		return startButton;
	}

	/**
	 * @return the consoleBtn
	 */
	public JButton getConsoleBtn() {
		return consoleBtn;
	}

	/**
	 * @return the globalConfigLabel
	 */
	public JLabel getGlobalConfigLabel() {
		return globalConfigLabel;
	}

	/**
	 * @return the overwriteLabel
	 */
	public JLabel getOverwriteLabel() {
		return overwriteLabel;
	}

	/**
	 * @return the main Panel
	 */
	public Component getContentPane() {
		return contentPane;
	}

	public JButton getOverwriteButton() {
		return overwriteButton;
	}

	/**
	 * @return the overwriteChangesLabel
	 */
	public JLabel getOverwriteChangesLabel() {
		return overwriteChangesLabel;
	}

}