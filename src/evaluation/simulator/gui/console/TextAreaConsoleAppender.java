package evaluation.simulator.gui.console;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaConsoleAppender extends WriterAppender {

	static Logger logger = Logger.getLogger(TextAreaConsoleAppender.class);
	private static javax.swing.JTextPane logTextPane = null;


	/** Set the target JTextPane for the logging information to appear. */
	public void setTextArea(javax.swing.JTextPane logTextPane) {
		TextAreaConsoleAppender.logTextPane = logTextPane;
	}


	@Override
	public void append(LoggingEvent loggingEvent) {

		final String message = this.layout.format(loggingEvent);
		final LoggingEvent currentEvent = loggingEvent;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				StyledDocument styledDocMainLowerText = logTextPane.getStyledDocument();
				Style style = styledDocMainLowerText.addStyle("StyledDocument", null);

				StyleConstants.setFontFamily(style, "Cursive");
				StyleConstants.setFontSize(style, 12);

				if (currentEvent.getLevel().toString() == "FATAL") {
					StyleConstants.setForeground(style, Color.red);
				} else {
					StyleConstants.setForeground(style, Color.blue);
				}

				try {
					styledDocMainLowerText.insertString(styledDocMainLowerText.getLength(),
							message, style);
				} catch (BadLocationException e) {
					logger.fatal(e);
				}
			}
		});
	}
}