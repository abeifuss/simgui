package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 * Creates the {@link JPanel} which offers the representation of a website
 * 
 * @author nachkonvention
 * 
 */
@SuppressWarnings("serial")
public class SimHelpContentPanel extends JPanel {

	private static Logger logger = Logger.getLogger(SimHelpContentPanel.class);

	private static SimHelpContentPanel instance = null;

	/**
	 * Singleton
	 * 
	 * @return an instance of {@link SimHelpContentPanel}
	 */
	public static SimHelpContentPanel getInstance() {
		if (instance == null) {
			instance = new SimHelpContentPanel();
		}
		return instance;
	}

	XHTMLPanel _htmlContent;

	private SimHelpContentPanel() {
		this.initialize();
	}

	private void initialize() {

		try {
			this._htmlContent = new XHTMLPanel();
			this.setLayout(new BorderLayout());
			this.setBackground(Color.WHITE);
			this._htmlContent.setDocument(new File("etc/html/index.html"));

			this.add(new FSScrollPane(this._htmlContent));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Some problem has occured" + ex.getMessage());
		}

	}

	/**
	 * @param url
	 *            the URL to load represented as {@link String}
	 */

	public void loadURL(String url) {
		logger.log(Level.DEBUG, "Loading help-page: " + url);
		try {
			this._htmlContent.setDocument(new File(url));
		} catch (Exception e) {
			logger.error("Probably no stylesheet defined... processing.");
		}
	}
}
