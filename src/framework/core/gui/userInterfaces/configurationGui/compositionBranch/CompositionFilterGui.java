package framework.core.gui.userInterfaces.configurationGui.compositionBranch;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.apache.log4j.Logger;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.SpringLayoutUtilities;

/**
 * A generic filter gui that is able to create so called filter rows
 * 
 * @author Marius Fink
 */
public class CompositionFilterGui {
	private JPanel mainPanel;
	private int numberOfRows;
	private JPanel innerRowPanel;
	private static final Logger LOGGER = Logger.getLogger(CompositionFilterGui.class);
	private List<ActionListener> actionListeners = new ArrayList<ActionListener>(10);

	/**
	 * The only way to create it.
	 */
	public CompositionFilterGui() {
		init();
	}

	/**
	 * Initializes the component
	 */
	private void init() {
		mainPanel = new JPanel(new BorderLayout());
		innerRowPanel = new JPanel(new SpringLayout());

		mainPanel.add(innerRowPanel, BorderLayout.NORTH);
		mainPanel.setBorder(BorderFactory.createTitledBorder(GUIText.getText("compositionFilter")));
	}

	/**
	 * Adds a row with a drop down. Heads up! Call <code>drawRows()</code> afterwards.
	 * 
	 * @param possValues
	 *            all possible values
	 * @param description
	 *            the name of this field
	 */
	public FilterRow addFilterRow(String[] possValues, String description) {
		if (possValues.length > 0) {
			numberOfRows++;

			final FilterRow row = new FilterRow();
			row.setActivated(true);
			row.setCurrentValue(possValues[0]);

			final JCheckBox checkbox = new JCheckBox();
			checkbox.setSelected(true);
			checkbox.setToolTipText(GUIText.getText("enableFilter"));
			checkbox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					row.setActivated(checkbox.isSelected());
					notifyListeners();
				}
			});

			final JComboBox<String> comboBox = new JComboBox<String>(possValues);
			comboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					row.setCurrentValue(comboBox.getSelectedItem().toString());
					if (checkbox.isSelected()) {
						notifyListeners();
					}
				}
			});

			innerRowPanel.add(checkbox);
			innerRowPanel.add(new JLabel(description));
			innerRowPanel.add(comboBox);

			return row;
		} else {
			LOGGER.error("Could not add the row: " + description + ". You gave me no possible values.");
			return null;
		}
	}

	/**
	 * Draws all current rows. Has to be called after all rows were added.
	 */
	public void drawRows() {

		SpringLayoutUtilities.makeCompactGrid(innerRowPanel, // parent
				numberOfRows, 3, // rows, cols
				0, 0, // initX, initY
				10, 3); // xPad, yPad

		mainPanel.updateUI();
		innerRowPanel.updateUI();
		mainPanel.updateUI();
	}

	public JPanel getPanel() {
		return mainPanel;
	}


	/**
	 * Adds an ActionListener to this component.
	 * 
	 * @param al
	 *            the new ActionListener
	 */
	public void addActionListener(ActionListener al) {
		actionListeners.add(al);
	}

	/**
	 * Notifies all currently registered ActionListeners
	 */
	private void notifyListeners() {
		for (ActionListener al : actionListeners) {
			al.actionPerformed(new ActionEvent(this, 0, "State changed"));
		}
	}

	/**
	 * A simple container for a filtering row that can be enabled and disabled and has a current
	 * choosen value.
	 * 
	 * @author Marius Fink
	 */
	public class FilterRow {
		private boolean activated;
		private String currentValue;

		/**
		 * @return the activated
		 */
		public boolean isActivated() {
			return activated;
		}

		/**
		 * @param activated
		 *            the activated to set
		 */
		public void setActivated(boolean activated) {
			this.activated = activated;
		}

		/**
		 * @return the currentValue
		 */
		public String getCurrentValue() {
			return currentValue;
		}

		/**
		 * @param currentValue
		 *            the currentValue to set
		 */
		public void setCurrentValue(String currentValue) {
			this.currentValue = currentValue;
		}

	}
}
