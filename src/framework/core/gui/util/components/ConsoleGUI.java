package framework.core.gui.util.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import framework.core.gui.userInterfaces.mainGui.GMixTitlePanel;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * The GUI console as a replacement for the system console implemented as Singleton
 * 
 * @author Marius Fink
 * @version 01.06.2012
 */
public class ConsoleGUI extends WindowAdapter {
	private JFrame frame;
	private JTextPane textPane;
	private boolean quit;

	private static ConsoleGUI instance;
	private JScrollPane scrollPane;
	private JTextField textfield;

	/**
	 * @return gets you the only instance of this console
	 */
	public static ConsoleGUI getInstance() {
		if (instance == null) {
			instance = new ConsoleGUI();
		}

		return instance;
	}

	/**
	 * Writes a colored line.
	 * 
	 * @param out
	 *            the text to write
	 * @param color
	 *            the color for this text
	 */
	public static void out(String s, Color c) {
		StyledDocument doc = ConsoleGUI.getInstance().textPane.getStyledDocument();

		Style style = ConsoleGUI.getInstance().textPane.addStyle("color_" + c.toString(), null);
		StyleConstants.setForeground(style, c);

		try {
			int len = doc.getLength();
			doc.insertString(len, s + "\n", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		ConsoleGUI.getInstance().scrollToBottom();
	}

	/**
	 * Writes a black line.
	 * 
	 * @param out
	 *            the text to write
	 */
	public static void out(String out) {
		out(out, Color.BLACK);
	}

	/**
	 * Construktor for the console starting the reading threads
	 */
	private ConsoleGUI() {
		frame = new JFrame();

		textPane = new JTextPane();
		textPane.setEditable(false);

		textfield = new JTextField();
		TextFieldStreamer ts = new TextFieldStreamer(textfield);
		textfield.addActionListener(ts);
		System.setIn(ts);

		quit = false; // signals the Threads that they should exit

		scrollPane = new JScrollPane(textPane);
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				scrollToBottom();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				scrollToBottom();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				scrollToBottom();
			}
		});

		initialize();
	}

	/**
	 * Scrolls the ScrollPane to the bottom.
	 */
	private void scrollToBottom() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textPane.setCaretPosition(textPane.getDocument().getLength());
			}
		});
	}

	/**
	 * Initializes the GUI
	 */
	private void initialize() {
		frame.setSize(new Dimension(640, 480));
		frame.setTitle(GUIText.getText("consoleTitle"));
		frame.setIconImages(ImageLoader.generateWindowIcons());
		frame.setPreferredSize(new Dimension(640, 480));
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setMaximumSize(new Dimension(640, 480));

		JButton button = new JButton(GUIText.getText("clearConsole"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clear();
			}
		});

		JPanel contentPane = new JPanel();
		frame.setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 0));
		contentPane.add(new GMixTitlePanel("console"), BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(textfield, BorderLayout.CENTER);
		buttonPanel.add(button, BorderLayout.EAST);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent we) {
				hideConsole();
			}
		});
		frame.setLocationByPlatform(true);
	}

	/**
	 * Displays the console - if the console is already visible it moves it to the foreground
	 */
	public void showConsole() {
		frame.setVisible(true);
		if (!isHidden()) {
			frame.toFront();
		}
	}

	/**
	 * Hides the Window
	 */
	public void hideConsole() {
		frame.setVisible(false);
	}

	/**
	 * Detects whether the console is hidden or not
	 * 
	 * @return true if the console is hidden, false, else
	 */
	public boolean isHidden() {
		return !frame.isVisible();
	}

	/**
	 * Clears the console
	 */
	public synchronized void clear() {
		textPane.setText("");
	}

	/**
	 * Reads a line
	 * 
	 * @param in
	 *            the input stream
	 * @return the string from in
	 * @throws IOException
	 */
	public synchronized String readLine(PipedInputStream in) throws IOException {
		String input = "";
		do {
			int available = in.available();
			if (available == 0)
				break;
			byte b[] = new byte[available];
			in.read(b);
			input = input + new String(b, 0, b.length);
		} while (!input.endsWith("\n") && !input.endsWith("\r\n") && !quit);
		return input;
	}

	/**
	 * Textfield to Systemin via TexfFieldStreamer
	 * 
	 * @version 30.06.2012
	 */
	private class TextFieldStreamer extends InputStream implements ActionListener {

		private JTextField tf;
		private String str = null;
		private int pos = 0;

		public TextFieldStreamer(JTextField jtf) {
			tf = jtf;
		}

		// gets triggered everytime that "Enter" is pressed on the textfield
		@Override
		public void actionPerformed(ActionEvent e) {
			str = tf.getText() + "\n";
			pos = 0;
			tf.setText("");
			synchronized (this) {
				System.out.print(str);
				// maybe this should only notify() as multiple threads may
				// be waiting for input and they would now race for input
				this.notifyAll();
			}
		}

		@Override
		public int read() {
			// test if the available input has reached its end
			// and the EOS should be returned
			if (str != null && pos == str.length()) {
				str = null;
				// this is supposed to return -1 on "end of stream"
				// but I'm having a hard time locating the constant
				return java.io.StreamTokenizer.TT_EOF;
			}
			// no input available, block until more is available because that's
			// the behavior specified in the Javadocs
			while (str == null || pos >= str.length()) {
				try {
					// according to the docs read() should block until new input
					// is available
					synchronized (this) {
						this.wait();
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			// read an additional character, return it and increment the index
			return str.charAt(pos++);
		}
	}

}