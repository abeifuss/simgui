package evaluation.simulator.gui.console;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ConsolePanel extends JTextArea {

	private static ConsolePanel instance = null;

	public static ConsolePanel getInstance() {
		if (instance == null) {
			instance = new ConsolePanel();
		}
		return instance;
	}

	private String _log;

	private ConsolePanel() {

		this.setEditable(false);

		this._log = "";
		this.initConsole();
	}

	@Override
	public void append(String msg) {
		this._log = msg + "\n" + this._log;
		this.update();
	}

	public void initConsole() {
		this.append("Wellcome to gMixSim");
		this.append("=============");
		this.update();
	}

	public void update() {
		this.setText(this._log);
		this.setCaretPosition(10);
	}
}
