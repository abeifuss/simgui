package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import framework.core.gui.util.GUIText;

/**
 * IntInputField is a Spinner for ints
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
public class IntInputField extends JPanel implements InputField {
	private static final long serialVersionUID = 6883530602150405287L;
	private int start;
	private int min;
	private int max;
	private JSpinner spinner;

	/**
	 * Has to be initialized with initial value, min and max.
	 * 
	 * @param start
	 *            the initial selected value (has to be between min and max)
	 * @param min
	 *            the minimum value (included)
	 * @param max
	 *            the maximum value (included)
	 */
	public IntInputField(int start, int min, int max) {
		assert start >= min && start <= max : "Precondition violated: start >= min && start <= max";
		this.start = start;
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
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.InputField#getStringRepresentation()
	 */
	@Override
	public String getStringRepresentation() {
		return spinner.getModel().getValue().toString();
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
		int valueOf;
		try {
			valueOf = Integer.valueOf(representation);
		} catch (NumberFormatException n) {
			valueOf = start;
		}
		spinner.getModel().setValue((Integer) valueOf);
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
