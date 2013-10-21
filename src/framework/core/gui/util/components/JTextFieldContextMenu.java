package framework.core.gui.util.components;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import framework.core.gui.util.GUIText;

/**
 * A JTextfield with a contextMenu for cut/copy/paste
 * 
 * @author Marius Fink
 * @date 04/21/2006
 */
public class JTextFieldContextMenu extends JTextField implements ActionListener, MouseListener, CaretListener {
	private static final long serialVersionUID = 1L;
	private JPopupMenu popup;
	private JMenuItem cut;
	private JMenuItem copy;
	private JMenuItem paste;
	private Clipboard systemClipboard;
	private Object content;
	private StringSelection selection;
	private Transferable transferData;
	private DataFlavor dataFlavor;

	/**
	 * Creates a well known JTextfield with the context menu ability cut/copy/paste
	 */
	public JTextFieldContextMenu() {
		super();
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		popup = new JPopupMenu();
		cut = new JMenuItem(GUIText.getText("cut"));
		copy = new JMenuItem(GUIText.getText("copy"));
		paste = new JMenuItem(GUIText.getText("paste"));

		popup.add(new JSeparator());
		popup.add(cut);
		popup.add(copy);
		popup.add(paste);
		popup.add(new JSeparator());

		cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
		cut.addActionListener(this);
		copy.addActionListener(this);
		paste.addActionListener(this);

		copy.setEnabled(false);
		cut.setEnabled(false);
		paste.setEnabled(false);

		addMouseListener(this);
		addCaretListener(this);

		addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				selectAll();

			}
		});
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cut) {
			cut();
		} else if (e.getSource() == copy) {
			kopieren();
		} else if (e.getSource() == paste) {
			paste();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		// do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(this, e.getX(), e.getY());
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// do nothing
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
	 */
	public void caretUpdate(CaretEvent e) {
		if (e.getMark() != e.getDot()) {
			copy.setEnabled(true);
			cut.setEnabled(true);
		} else {
			copy.setEnabled(false);
			cut.setEnabled(false);
		}
	}

	/**
	 * Copies the selected text into the systems clipboard and deletes the String from the Textfield
	 */
	public void cut() {
		selection = new StringSelection(getSelectedText());
		systemClipboard.setContents(selection, selection);
		deleteMarked();

		paste.setEnabled(true);
	}

	/**
	 * Copies the selected text into the systems clipboard
	 */
	public void kopieren() {
		selection = new StringSelection(getSelectedText());
		systemClipboard.setContents(selection, selection);
		paste.setEnabled(true);
	}

	/**
	 * Pastes a String from the systems clipboard
	 */
	public void paste() {
		transferData = systemClipboard.getContents(null);
		dataFlavor = DataFlavor.stringFlavor;

		try {
			content = transferData.getTransferData(dataFlavor);
		} catch (UnsupportedFlavorException e) {
		} catch (IOException e) {
		}

		if (content != null) {
			setText(content.toString());
		}
	}

	/**
	 * Deletes the marked area
	 */
	public void deleteMarked() {
		String text = "";
		text += getText().substring(0, getSelectionStart());
		text += getText().substring(getSelectionEnd(), getText().length());
		setText(text);
	}

	/**
	 * Initializes the buttons
	 */
	public void initialize() {
		copy.setEnabled(false);
		cut.setEnabled(false);
		paste.setEnabled(false);
	}
}