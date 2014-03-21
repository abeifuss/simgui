package evaluation.simulator.gui.console;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import evaluation.simulator.gui.customElements.ConsolePanel;

/**
 * implements a new Appender for parsing log4j information into the
 * {@link JTextArea} of the {@link ConsolePanel}.
 * 
 * @author nachkonvention
 */
public class TextAreaConsoleAppender extends WriterAppender {

	static Logger logger = Logger.getLogger(TextAreaConsoleAppender.class);
	private static javax.swing.JTextPane logTextPane = null;

	/**
	 * @param logTextPane
	 *            the TextArea for parsing log4j information to
	 */
	public void setTextArea(javax.swing.JTextPane logTextPane) {
		TextAreaConsoleAppender.logTextPane = logTextPane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.log4j.WriterAppender#append(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	public void append(LoggingEvent loggingEvent) {

		final String message = this.layout.format(loggingEvent);
		final LoggingEvent currentEvent = loggingEvent;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				StyledDocument styledDocMainLowerText;
				try {
					styledDocMainLowerText = logTextPane.getStyledDocument();
					Style style = styledDocMainLowerText.addStyle("StyledDocument", null);

					StyleConstants.setFontFamily(style, "Cursive");
					StyleConstants.setFontSize(style, 12);

					if (currentEvent.getLevel().toString() == "FATAL") {
						StyleConstants.setForeground(style, Color.red);
					} else {
						StyleConstants.setForeground(style, Color.blue);
					}

					try {
						styledDocMainLowerText.insertString(styledDocMainLowerText.getLength(), message, style);
					} catch (BadLocationException e) {
						logger.fatal(e);
					}
				} catch (Exception e) {

				}

			}
		});
	}
}
