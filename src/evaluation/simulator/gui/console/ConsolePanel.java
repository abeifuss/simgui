package evaluation.simulator.gui.console;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
@Deprecated
public class ConsolePanel extends JPanel {

	private static ConsolePanel instance = null;

	public static ConsolePanel getInstance() {
		if (instance == null) {
			instance = new ConsolePanel();
		}
		return instance;
	}

	private String _log;
	private final JTextArea textArea;
	private final JScrollPane scroll;

	private ConsolePanel() {

		this.textArea = new JTextArea();
		this.scroll = new JScrollPane(this.textArea);

		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this, gridBagConstraints);

		this.setLayout( gridBagLayout );
		this.textArea.setEditable(false);

		this.add(this.scroll, gridBagConstraints);
		this.textArea.setVisible(true);

		this.scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		this.scroll.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

		this._log = "";
		this.initConsole();
	}

	public void append(String msg) {
		this._log = this._log + "\n" + msg;
		this.update();
	}

	public void initConsole() {
		this.append("=============");
		this.append("Wellcome to gMixSim");
	}

	public void update() {
		this.textArea.setText(this._log);
		//		this.textArea.setCaretPosition(0);
		this.scroll.getVerticalScrollBar().setValue( this.scroll.getVerticalScrollBar().getMaximum() );
		this.scroll.repaint();
	}
}
