package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import framework.core.gui.util.GUIText;

/**
 * IntInputField is a Spinner for ints with an "auto" checkbox
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
public class IntAutoInputField extends JPanel implements InputField {
	private static final long serialVersionUID = 6883530602150405287L;
	private int start;
	private int min;
	private int max;
	private JSpinner spinner;
	private String currentValue;
	private boolean auto;
	private JCheckBox autoCheckbox;
	
	/**
	 * Has to be initialized with initial value, min and max.
	 * 
	 * @param start
	 *            the initial selected value (has to be between min and max or "auto")
	 * @param min
	 *            the minimum value (included)
	 * @param max
	 *            the maximum value (included)
	 */
	public IntAutoInputField(String preset, int min, int max) {
		
		currentValue = preset;
		try {
			start = Integer.valueOf(preset);
		} catch (NumberFormatException nfe) {
			start = min;
			auto = true;
		}
		
		assert (start >= min && start <= max) || (auto) : "Precondition violated: start >= min && start <= max";
		
		this.min = min;
		this.max = max;

		init();
	}

	/**
	 * Initializes this spinner.
	 */
	private void init() {
		setLayout(new BorderLayout(10, 0));
		spinner = new JSpinner(new SpinnerNumberModel(start, min, max, 1));

		setOpaque(false);
		spinner.setOpaque(false);

		add(spinner, BorderLayout.CENTER);
		JLabel jLabel = new JLabel(GUIText.getText("rangeRestriction", "" + min, "" + max));
		jLabel.setOpaque(false);

		add(jLabel, BorderLayout.EAST);
		
		autoCheckbox = new JCheckBox("auto");
		autoCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setAutoEnabled(autoCheckbox.isSelected());
			}
		});
		//initial values
		autoCheckbox.setSelected(auto);
		spinner.setEnabled(!auto);
		
		JPanel autoPanel = new JPanel(new BorderLayout());
		autoPanel.add(autoCheckbox, BorderLayout.WEST);
		//autoPanel.add(new JLabel(GUIText.getText("auto")));
		
		add(autoPanel, BorderLayout.WEST);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.InputField#getStringRepresentation()
	 */
	@Override
	public String getStringRepresentation() {
		return currentValue;
	}

	@Override
	public void addActionListener(ActionListener al) {
		final ActionListener alCopy = al;
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				alCopy.actionPerformed(new ActionEvent(arg0, 0, "Spinner state changed."));
			}
		});
	}

	@Override
	public void setValue(String representation) {
		if (representation.trim().toLowerCase().equals("auto")) {
			setAutoEnabled(true);
		}
		int valueOf;
		try {
			valueOf = Integer.valueOf(representation);
		} catch (NumberFormatException n) {
			valueOf = start;
		}
		spinner.getModel().setValue((Integer) valueOf);
	}

	private void setAutoEnabled(boolean b) {
		if (b) {
			spinner.setEnabled(false);
			currentValue = "auto";
		} else {
			spinner.setEnabled(true);
			currentValue = spinner.getModel().getValue().toString();
		}
		
	}

	@Override
	public void empty() {
		setValue("0");
	}

	@Override
	public void disableInput() {
		spinner.setEnabled(false);
	}
}
