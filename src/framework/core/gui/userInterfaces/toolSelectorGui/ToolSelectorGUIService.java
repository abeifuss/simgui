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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JToggleButton;

import framework.core.gui.model.XMLResource;
import framework.core.gui.services.CompositionDependencyService;
import framework.core.gui.userInterfaces.toolSelectorGui.keyValuePopUp.KeyValueInputDialogue;
import framework.core.gui.userInterfaces.toolSelectorGui.keyValuePopUp.KeyValuePair;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.ConsoleGUI;
import framework.core.gui.util.components.PopUpPrompt;
import framework.core.launcher.ToolName;


/**
 * This class displays a menu to choose some tool of the framework (r.g. the simulator, the load
 * generator, the info service, a mix, a client etc.)
 * 
 * @author Marius Fink
 * @version 06/01/12
 */
public class ToolSelectorGUIService {
	private static ToolSelectorGUI gui;
	private static final String INFO_PLACEHOLDER = GUIText.getText("infoPlaceHolder");
	private ConsoleGUI console;
	private List<JToggleButton> buttons = new LinkedList<JToggleButton>();
	private String selectedTool = "";
	private List<KeyValuePair<String, String>> overwrite = new LinkedList<KeyValuePair<String, String>>();

	/**
	 * Starts the GUI with given params
	 * 
	 * @param params
	 *            the params to give to the GUI
	 */
	public ToolSelectorGUIService() {

		gui = new ToolSelectorGUI();
		console = ConsoleGUI.getInstance();

		createStartButtons();
		gui.setInfoText(INFO_PLACEHOLDER);
		gui.getConsoleBtn().addActionListener(createConsoleButtonActionListener());

		registerParameterDescriptions();

		gui.getStartButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// START!
				startTool(ToolName.getToolByIdentifier(selectedTool));
			}
		});
		
		gui.getOverwriteButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				overwrite = KeyValueInputDialogue.editKeyValueOptions(GUIText.getText("overwrite"), overwrite);
				gui.getOverwriteChangesLabel().setText(GUIText.getText("changesCount", overwrite.size() + ""));
			}
		});

		handleButtonState();
	}

	/**
	 * Creates the action listener for the console button.
	 */
	private ActionListener createConsoleButtonActionListener() {
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ConsoleGUI c = ConsoleGUI.getInstance();
				if (c.isHidden()) {
					c.showConsole();
				}
			}
		};
		return al;
	}

	/**
	 * Enables or disables the Start Button
	 */
	private void handleButtonState() {
		gui.getStartButton().setEnabled(!selectedTool.matches(" *"));
	}

	/**
	 * Creates the actions that display the tooltip text of the command-line-labels in the main
	 * area.
	 */
	private void registerParameterDescriptions() {
		gui.getGlobalConfigFileTextField().setText(GUIText.getText("globalConfigStandardFile"));
		gui.getGlobalConfigLabel().addMouseListener(createHoverTextMouseListener(GUIText.getText("globalConfig")));
		gui.getOverwriteLabel().addMouseListener(createHoverTextMouseListener(GUIText.getText("overwriteParameters")));
	}

	/**
	 * Creates a MouseListener that displays the given text on hover and shows the placeholder on
	 * mouse out
	 * 
	 * @param show
	 *            the text to show
	 * @return the MouseListener
	 */
	private MouseListener createHoverTextMouseListener(final String show) {
		return new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				gui.setInfoText(show);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				gui.setInfoText(INFO_PLACEHOLDER);
			}
		};
	}

	/**
	 * Registers a listener to the given button, starting the specified Tool on click.
	 * 
	 * @param component
	 *            the JButton to register this listener to
	 * @param toolname
	 *            the tool to start on click
	 */
	private void registerListener(final JToggleButton btn, final ToolName toolname) {
		if (btn != null) {
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// startTool(toolname);
					for (JToggleButton btn2 : getButtons()) {
						if (!btn2.equals(btn)) {
							btn2.setSelected(false);
						}
					}

					updateSelectedTool(toolname.identifiers[0]);
				}
			});
			btn.addMouseListener(createHoverTextMouseListener(GUIText.getText(toolname.identifiers[0] + "Description")));
		}
	}

	/**
	 * Updates the currently selected tool
	 * 
	 * @param toolname
	 *            the toolname to set to if one button is selected
	 */
	protected void updateSelectedTool(String toolname) {
		selectedTool = "";
		for (JToggleButton btn2 : getButtons()) {
			if (btn2.isSelected()) {
				selectedTool = toolname;
			}
		}
		handleButtonState();
	}

	/**
	 * @return the start buttons
	 */
	protected List<JToggleButton> getButtons() {
		return buttons;
	}

	/**
	 * Creates all Buttons
	 */
	private void createStartButtons() {
		int i = 1;
		for (ToolName t : ToolName.values()) {
			if (t.identifiers.length > 0) {
				JToggleButton jb = gui.addToolStartButton(i + ") " + t.name());
				buttons.add(jb);
				registerListener(jb, t);
				i++;
			}
		}
	}

	/**
	 * Starts the given Tool (in a GUI separated thread)
	 * 
	 * @param toolname
	 *            the tool to start
	 */
	protected void startTool(ToolName toolname) {
		console.showConsole();

		String globalConfigFilePath = gui.getGlobalConfigFileTextField().getText();
		if (!globalConfigFilePath.matches(" *")) {
			final XMLResource config = new XMLResource(globalConfigFilePath, overwrite);
			// 3 Validate
			if (CompositionDependencyService.isConfigValid(config)) {

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						ToolName.getToolByIdentifier(selectedTool).execute(config);
					}
				});
				 t.start();
			} else {
				PopUpPrompt.displayGenericErrorMessage(GUIText.getText("configInvalid"),
						GUIText.getText("configInvalidDependencies"));
			}
		} else {
			PopUpPrompt.displayGenericErrorMessage(GUIText.getText("configInvalid"),
					GUIText.getText("noConfig"));
		}
	}

	/**
	 * @return the Main Panel of the Gui
	 */
	public Component createPanel() {
		return gui.getContentPane();
	}

}
