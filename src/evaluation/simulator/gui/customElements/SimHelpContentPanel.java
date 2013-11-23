package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;

import evaluation.simulator.conf.service.SimulationConfigService;

@SuppressWarnings("serial")
public class SimHelpContentPanel extends JPanel {
	
	private static Logger logger = Logger.getLogger(SimHelpContentPanel.class);

	private static SimHelpContentPanel _instance = null;

	public static SimHelpContentPanel getInstance() {
		if (_instance == null) {
			_instance = new SimHelpContentPanel();
		}
		return _instance;
	}

	XHTMLPanel _htmlContent;

	private SimHelpContentPanel() {
		this.init();
	}

	private void init() {

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

	public void loadURL(String url) {
		logger.log(Level.DEBUG, "Loading help-page: " + url);
		try {
			this._htmlContent.setDocument(new File(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
