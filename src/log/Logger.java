package log;

import gui.console.ConsolePanel;

public class Logger {
	
	public static void Log(LogLevel level, String msg){
		
		Long t = System.currentTimeMillis();
		ConsolePanel c = ConsolePanel.getInstance();
		
		switch (level) {
		case INFO:
			System.out.println("[INFO]@"+t+"#: "+msg);
			c.append("[INFO]@"+t+"#: "+msg);
			break;
		case DEBUG:
			System.out.println("[DEBUG]@"+t+"#: "+msg);
			c.append("[DEBUG]@"+t+"#: "+msg);
			break;
		case ERROR:
			System.err.println("[ERROR]@"+t+"#: "+msg);
			c.append("[ERROR]@"+t+"#: "+msg);
			break;

		default:
			break;
		}
		
	}
}
