package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.JTextFieldContextMenu;
import framework.core.gui.util.visualUtilities.GUIColors;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * StringInputField is an extended textfield with description of restricitions
 * if given
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
public class StringInputField extends JPanel implements InputField {

	private final JTextFieldContextMenu textField = new JTextFieldContextMenu();
	private static final long serialVersionUID = -5649072025346879865L;
	private String pattern;
	private String preset;
	private boolean isValid;
	private String lastValidInput;
	private JPanel iconPanel;
	private static final ImageIcon VALID_ICON = ImageLoader.loadIcon("valid");
	private static final ImageIcon INVALID_ICON = ImageLoader
			.loadIcon("invalid");

	/**
	 * Constuctor of StringInputField with preset and optional pattern
	 * 
	 * @param preset
	 *            the preset (has to match the pattern
	 * @param pattern
	 *            the pattern for validation - this can be null
	 */
	public StringInputField(String preset, String pattern) {
		assert preset.matches(pattern) : "Precondition violated: preset.matches(pattern)";

		this.pattern = pattern;
		this.preset = preset;
		this.lastValidInput = preset;
		init();
		validateInput();
	}

	/**
	 * Initializes the String input panel
	 */
	private void init() {
		setLayout(new BorderLayout());
		setOpaque(false);
		add(textField, BorderLayout.CENTER);

		iconPanel = new JPanel(new BorderLayout());
		iconPanel.setOpaque(false);
		add(iconPanel, BorderLayout.EAST);

		if (preset != null) {
			textField.setText(preset);
		}

		if (pattern != null) {
			textField.setToolTipText(GUIText.getText("stringPattern", pattern));

			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					validateInput();
				}
			});
		}
	}

	/**
	 * Validates the Input
	 */
	private void validateInput() {
		if (pattern == null) {
			isValid = true;
			lastValidInput = textField.getText();
		} else {
			isValid = textField.getText().matches(pattern);
			if (isValid) {
				lastValidInput = textField.getText();
				textField.setForeground(GUIColors
						.getColorByName("standardForeground"));
				setIcon(VALID_ICON);
			} else {
				textField.setForeground(GUIColors.getColorByName("critical"));
				setIcon(INVALID_ICON);
			}
		}
	}

	/**
	 * Sets the validation icon of this textfield
	 * 
	 * @param icon
	 *            the icon to show
	 */
	private void setIcon(ImageIcon icon) {
		iconPanel.removeAll();
		JLabel jLabel = new JLabel(icon);
		jLabel.setToolTipText(GUIText.getText("stringPattern", pattern));
		iconPanel.add(jLabel);
		iconPanel.updateUI();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.InputField#getStringRepresentation()
	 */
	@Override
	public String getStringRepresentation() {
		validateInput();
		return lastValidInput;
	}

	@Override
	public void addActionListener(ActionListener al) {
		final ActionListener alCopy = al;
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				alCopy.actionPerformed(new ActionEvent(arg0, 0,
						"Document state changed."));
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				alCopy.actionPerformed(new ActionEvent(arg0, 0,
						"Document state changed."));
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				alCopy.actionPerformed(new ActionEvent(arg0, 0,
						"Document state changed."));
			}
		});
	}

	@Override
	public void setValue(String representation) {
		textField.setText(representation);
		validateInput();
	}

	@Override
	public void empty() {
		setValue("");
	}

	@Override
	public void disableInput() {
		textField.setEnabled(false);
	}
}
