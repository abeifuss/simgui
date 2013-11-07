package evaluation.simulator.gui.layout.frames;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.customElements.SimHelpContentPanel;
import evaluation.simulator.gui.customElements.SimHelpMenuPanel;
import evaluation.simulator.gui.service.GuiService;
// import log.LogLevel;
// import log.Logger;

@SuppressWarnings("serial")
public class HelpFrame extends JFrame {

	private static HelpFrame _instance = null;

	public static HelpFrame getInstance() {
		if (_instance == null) {
			_instance = new HelpFrame();
		}
		return _instance;
	}

	private int _helpFrameHeight;
	private int _helpFrameWidth;
	private int _helpFrameXPos;

	private int _helpFrameYPos;

	private JPanel panel;

	private HelpFrame() {

		this.getContentPane().setLayout(new BorderLayout());

		this.init();

		this.setTitle("Help Tool");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(
				"etc/img/icons/icon128.png"));

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(false);

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				HelpFrame.this.safeProperties();
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				HelpFrame.this.safeProperties();
				GuiService.getInstance().toogleHelpTools();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				HelpFrame.this.safeProperties();
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}
		});
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void init() {
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		JSplitPane splitPlane = new JSplitPane();
		// splitPlane.setLayout(new BorderLayout());

		SimHelpContentPanel content = SimHelpContentPanel.getInstance();
		SimHelpMenuPanel navigation = SimHelpMenuPanel.getInstance();

		splitPlane.setLeftComponent(navigation);
		splitPlane.setRightComponent(content);

		this.panel.add(splitPlane);
		this.add(this.panel);

		this.pack();

		try {
			this._helpFrameXPos = UserConfigService.getInstance().getInteger(
					"HELPFRAME_XPOS");
		} catch (Exception e) {
			this._helpFrameXPos = 600;
		}

		try {
			this._helpFrameYPos = UserConfigService.getInstance().getInteger(
					"HELPFRAME_YPOS");
		} catch (Exception e) {
			this._helpFrameYPos = 100;
		}

		try {
			this._helpFrameWidth = UserConfigService.getInstance().getInteger(
					"HELPFRAME_WIDTH");
		} catch (Exception e) {
			this._helpFrameWidth = 700;
		}

		try {
			this._helpFrameHeight = UserConfigService.getInstance().getInteger(
					"HELPFRAME_HEIGTH");
		} catch (Exception e) {
			this._helpFrameHeight = 750;
		}

		this.setBounds(this._helpFrameXPos, this._helpFrameYPos,
				this._helpFrameWidth, this._helpFrameHeight);
	}

	private void safeProperties() {
		UserConfigService.getInstance().setInteger("HELPFRAME_XPOS",
				this.getX());
		UserConfigService.getInstance().setInteger("HELPFRAME_YPOS",
				this.getY());
		UserConfigService.getInstance().setInteger("HELPFRAME_WIDTH",
				this.getWidth());
		UserConfigService.getInstance().setInteger("HELPFRAME_HEIGTH",
				this.getHeight());
	}
}
