package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import evaluation.simulator.gui.console.TextAreaConsoleAppender;

/**
 * {@link JPanel} containing the console
 * 
 * @author nachkonvention
 * 
 */
@SuppressWarnings("serial")
public class ConsolePanel extends JPanel {

	private static ConsolePanel instance = null;

	/**
	 * Singleton
	 * 
	 * @return instance of {@link ConsolePanel}
	 */
	public static ConsolePanel getInstance() {
		if (instance == null) {
			instance = new ConsolePanel();
		}
		return instance;
	}

	private final JTextPane textArea;
	private final JScrollPane scroll;

	private ConsolePanel() {

		this.textArea = new JTextPane();
		this.scroll = new JScrollPane(this.textArea);

		this.setLayout(new BorderLayout());
		this.textArea.setEditable(false);

		this.add(this.scroll, BorderLayout.CENTER);
		this.textArea.setVisible(true);

		this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				TextAreaConsoleAppender textAreaConsoleAppender = new TextAreaConsoleAppender();
				textAreaConsoleAppender.setTextArea(ConsolePanel.getInstance().textArea);
			}
		});
	}

}
