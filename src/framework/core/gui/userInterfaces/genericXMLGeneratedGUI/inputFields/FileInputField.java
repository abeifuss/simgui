package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.JTextFieldContextMenu;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * FileInputFiled is a Panel with an open file button
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
public class FileInputField extends JPanel implements InputField {

    private static final long serialVersionUID = 5235854150690467794L;
    private String filetype;
    private JTextFieldContextMenu textField;
    private JButton fileChoosingBtn;

    /**
     * Creates a new FileInputField with optional file filter
     * 
     * @param filetype
     *            the filetype filter - can be null for no filter
     */
    public FileInputField(String filetype) {
	this.filetype = filetype;
	init();
    }

    /**
     * Initializes this component.
     */
    private void init() {
	final JFileChooser fc = new JFileChooser();
	if (filetype != null) {
	    fc.setFileFilter(new FileFilter() {
		@Override
		public String getDescription() {
		    return GUIText.getText("accept", filetype);
		}

		@Override
		public boolean accept(File arg0) {
		    return arg0.getName().endsWith(filetype)
			    || arg0.isDirectory();
		}
	    });
	}

	textField = new JTextFieldContextMenu();
	textField.setEditable(false);
	fileChoosingBtn = new JButton(ImageLoader.loadIcon("open"));
	fileChoosingBtn.setOpaque(false);
	fileChoosingBtn.setMargin(new Insets(0, 0, 0, 0));
	fileChoosingBtn.setSize(textField.getSize().height, textField.getSize().height);
	fileChoosingBtn.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    textField.setText(fc.getSelectedFile().getAbsoluteFile()
			    .getAbsolutePath());
		}
	    }
	});

	setLayout(new BorderLayout());
	add(textField, BorderLayout.CENTER);
	add(fileChoosingBtn, BorderLayout.EAST);
    }

    /**
     * (non-Javadoc)
     * 
     * @see framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.InputField#getStringRepresentation()
     */
    @Override
    public String getStringRepresentation() {
	return textField.getText();
    }
    
    @Override
    public void addActionListener(ActionListener al) {
	final ActionListener alCopy = al;
	textField.getDocument().addDocumentListener(new DocumentListener() {
	    @Override
	    public void removeUpdate(DocumentEvent arg0) {
		alCopy.actionPerformed(new ActionEvent(arg0, 0, "Document state changed."));
	    }
	    
	    @Override
	    public void insertUpdate(DocumentEvent arg0) {
		alCopy.actionPerformed(new ActionEvent(arg0, 0, "Document state changed."));
	    }
	    
	    @Override
	    public void changedUpdate(DocumentEvent arg0) {
		alCopy.actionPerformed(new ActionEvent(arg0, 0, "Document state changed."));
	    }
	});
    }

    @Override
    public void setValue(String representation) {
	textField.setText(representation);
    }

    @Override
    public void empty() {
	setValue("");
    }

    @Override
    public void disableInput() {
	textField.setEnabled(false);
	fileChoosingBtn.setEnabled(false);
    }

}
