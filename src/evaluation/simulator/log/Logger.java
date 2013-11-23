package evaluation.simulator.log;

import org.apache.log4j.Level;

import evaluation.simulator.gui.layout.frames.ConsoleFrame;

public class Logger {

	public static void log(Level level, String msg) {

		Long msg_time = System.currentTimeMillis();
		ConsoleFrame console = ConsoleFrame.getInstance();
//
//		switch (level) {
//		case INFO:
//			System.out.println("[INFO]@" + msg_time + "#: " + msg);
//			console.append("[INFO]@" + msg_time + "#: " + msg);
//			break;
//		case DEBUG:
//			System.out.println("[DEBUG]@" + msg_time + "#: " + msg);
//			console.append("[DEBUG]@" + msg_time + "#: " + msg);
//			break;
//		case ERROR:
//			System.err.println("[ERROR]@" + msg_time + "#: " + msg);
//			console.append("[ERROR]@" + msg_time + "#: " + msg);
//			break;
//
//		default:
//			break;
//		}

	}
}
