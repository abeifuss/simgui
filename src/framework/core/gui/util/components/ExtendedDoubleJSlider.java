package framework.core.gui.util.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import framework.core.gui.util.visualUtilities.GUIColors;

/**
 * This class is an input component for numeric values with a slider and a textfield
 * 
 * @author Marius Fink
 * @version 22.02.2011
 */
public class ExtendedDoubleJSlider extends JPanel {
	private static final long serialVersionUID = 1L;
	private double currentValue;
	private double min;
	private double max;
	private double start;
	private int intFactor;
	protected JSlider slider;
	private List<ChangeListener> listener;
	private int majorTicks;
	private int minorTicks;
	private JLabel label;
	private boolean textfieldActive;
	private JPanel textfieldPanel;
	protected JTextField textField;

	/**
	 * Constructor of ExtendedDoubleJSlider
	 * 
	 * @param start
	 *            the initial value
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @param precision
	 *            the precision (the count of decimal places, max 8)
	 */
	public ExtendedDoubleJSlider(double start, double min, double max, int precision) {
		assert min <= start && max >= start : "Precondition violated: min <= start && max >= start";
		assert max > min : "Precondition violated: max > min";
		assert precision < 8 : "Precondition violated: precision < 8";

		this.intFactor = (int) Math.pow(10, precision);
		this.currentValue = start;
		this.min = min;
		this.max = max;
		this.majorTicks = (int) (((max - min) * (double) intFactor) / 5);
		this.minorTicks = (int) (((max - min) * (double) intFactor) / 10);
		this.start = start;
		this.listener = new ArrayList<ChangeListener>();
		this.textfieldActive = false;

		slider = createSlider();
		createTextfieldPanelAndLabel();

		redraw();
	}

	/**
	 * Redraws the component
	 */
	private void redraw() {
		removeAll();

		setLayout(new BorderLayout());

		add(slider, BorderLayout.CENTER);

		JPanel east = new JPanel(new FlowLayout(FlowLayout.LEFT));
		east.add(textfieldPanel);
		add(east, BorderLayout.EAST);

		updateUI();
	}

	/**
	 * Creates the textfield panel and label
	 */
	private void createTextfieldPanelAndLabel() {
		textfieldPanel = new JPanel(new BorderLayout());
		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textfieldActive = true;
				activateTextfieldInput();
			}

		});

		textField = new NumberTextfield();
		textField.setHorizontalAlignment(SwingConstants.CENTER);

		renewLabelWidth();

		updateDisplayedValues();
		textfieldPanel.add(label, BorderLayout.CENTER);
	}

	/**
	 * Changes the view by removing the value label and displaying the textfield
	 */
	private void activateTextfieldInput() {
		textfieldPanel.removeAll();
		if (textfieldActive) {
			textfieldPanel.add(textField);
			textField.requestFocus();
		} else {
			textfieldPanel.add(label);
		}
		updateUI();
	}

	/**
	 * Updates all displayed data
	 */
	private void updateDisplayedValues() {
		label.setText(slider.getValue() / (double) intFactor + "");
		textField.setText(slider.getValue() / (double) intFactor + "");
		slider.setValue((int) (currentValue * (double) intFactor));
	}

	/**
	 * @return the slider
	 */
	private JSlider createSlider() {
		majorTicks = (int) (((max - min) * (double) intFactor) / 5);
		minorTicks = (int) (((max - min) * (double) intFactor) / 10);

		final JSlider slider = new JSlider((int) (min * (double) intFactor), (int) (max * (double) intFactor),
				(int) (start * (double) intFactor));
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(slider.getMinimum()), new JLabel(min + ""));
		labelTable.put(new Integer(slider.getMaximum()), new JLabel(max + ""));
		slider.setLabelTable(labelTable);

		slider.setMajorTickSpacing(majorTicks);
		slider.setMinorTickSpacing(minorTicks);
		slider.setPaintTicks(true);

		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				currentValue = slider.getValue() / (double) intFactor;
				updateDisplayedValues();
				notifyListener();
			}
		});
		return slider;
	}

	/**
	 * @param cl
	 *            adds the given change listener
	 */
	public void addChangeListener(ChangeListener cl) {
		listener.add(cl);
	}

	/**
	 * @param cl
	 *            removes the given change listener
	 */
	public void removeChangeListener(ChangeListener cl) {
		listener.remove(cl);
	}

	/**
	 * @return the current value
	 */
	public double getValue() {
		return currentValue;
	}

	/**
	 * @param value
	 *            sets the current value, all displayed data will be updated
	 */
	public void setValue(double value) {
		currentValue = value;
		updateDisplayedValues();
	}

	/**
	 * Informs the listener
	 */
	private void notifyListener() {
		for (ChangeListener cl : listener) {
			cl.stateChanged(new ChangeEvent(ExtendedDoubleJSlider.class.getSimpleName() + " changed its value."));
		}
	}

	/**
	 * Subclass NumberTextfield is a textfield that only accepts numbers.
	 * 
	 * @author Marius Fink
	 * @version 2011/03/06
	 */
	private class NumberTextfield extends JTextField {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor of NumberTextfield
		 */
		public NumberTextfield() {
			addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent arg0) {
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					validateAndInform();
				}

				@Override
				public void keyPressed(KeyEvent arg0) {
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						if (isValidNumber(getText())) {
							currentValue = Double.parseDouble(getText());
						}
						updateDisplayedValues();
						textfieldActive = false;
						activateTextfieldInput();
					}
				}
			});

			addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					textfieldActive = false;
					activateTextfieldInput();
				}

				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}

		/**
		 * Validates and marks the entry if invalid
		 */
		protected void validateAndInform() {
			if (isValidNumber(getText())) {
				setForeground(Color.BLACK);
			} else {
				setForeground(GUIColors.getColorByName("critical"));
			}
		}

		@Override
		public void setText(String newText) {
			if (isValidNumber(newText)) {
				super.setText(newText);
			}
		}

		/**
		 * @param zahl
		 *            Zahlenstring
		 * @return true, wenn gültig
		 */
		private boolean isValidNumber(String zahl) {
			try {
				Double.parseDouble(zahl);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}

	/**
	 * Sets the count pf decimal places
	 * 
	 * @param n
	 *            decimal places, max 8
	 */
	public void setDecimalPlaces(int n) {
		if (n > 8) {
			n = 8;
		}
		intFactor = (int) Math.pow(10, n);
		slider = createSlider();
		renewLabelWidth();
		redraw();
	}

	/**
	 * renews the width of the label
	 */
	private void renewLabelWidth() {
		FontMetrics fm = label.getFontMetrics(label.getFont());// .getStringBounds(_intfaktor + ",",
																// 0, (_intfaktor + ",").length(),
																// null);
		String string = ((max * intFactor) + ",").replaceAll("1", "5");
		int w = SwingUtilities.computeStringWidth(fm, string);
		label.setPreferredSize(new Dimension(w, 25));
		textField.setPreferredSize(new Dimension(w, 25));
	}

}