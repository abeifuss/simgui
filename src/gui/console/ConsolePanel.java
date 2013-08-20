package gui.console;

import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ConsolePanel extends JTextArea{

	JTextField console;
	String log;
	JScrollBar vbar;
	
	private static ConsolePanel instance = null;

	private ConsolePanel() {
		
		this.setEditable(false);
		
		log = "";
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
		this.setText(log);
		this.setCaretPosition(10);
	}
	
	public void append(String msg) {
		log = msg + "\n" + log;
		this.update();
	}
}
