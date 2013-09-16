package gui.console;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ConsolePanel extends JTextArea{

	private String _log;
	private static ConsolePanel instance = null;

	private ConsolePanel() {
		
		this.setEditable(false);
		
		_log = "";
		initConsole();
	}
	public static ConsolePanel getInstance() {
		if (instance == null) {
			instance = new ConsolePanel();
		}
		return instance;
	}
	
	public void initConsole() {
		append("Wellcome to gMixSim");
		append("=============");
		this.update();
	}
	
	public void update() {
		this.setText(_log);
		this.setCaretPosition(10);
	}
	
	public void append(String msg) {
		_log = msg + "\n" + _log;
		this.update();
	}
}
