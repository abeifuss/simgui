package log;

import gui.console.ConsolePanel;

public class Logger {
	
	public static void Log(LogLevel level, String msg){
		
		Long msg_time = System.currentTimeMillis();
		ConsolePanel console = ConsolePanel.getInstance();
		
		switch (level) {
		case INFO:
			System.out.println("[INFO]@"+msg_time+"#: "+msg);
			console.append("[INFO]@"+msg_time+"#: "+msg);
			break;
		case DEBUG:
			System.out.println("[DEBUG]@"+msg_time+"#: "+msg);
			console.append("[DEBUG]@"+msg_time+"#: "+msg);
			break;
		case ERROR:
			System.err.println("[ERROR]@"+msg_time+"#: "+msg);
			console.append("[ERROR]@"+msg_time+"#: "+msg);
			break;

		default:
			break;
		}
		
	}
}
