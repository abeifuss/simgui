package evaluation.simulator.gui.layout.frames;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.customElements.SimConfigPanel;
import evaluation.simulator.gui.service.GuiService;

@SuppressWarnings("serial")
public class ToolFrame extends JFrame {

	private static ToolFrame instance = null;

	public static ToolFrame getInstance() {
		if (instance == null) {
			instance = new ToolFrame();
		}
		return instance;
	}

	private int confToolFrameHeight;
	private int confToolFrameWidth;
	private int confToolFrameXPos;

	private int confToolFrameYPos;

	private ToolFrame() {

		this.getContentPane().setLayout(new BorderLayout());
		this.init();
		this.setTitle("Configuration Tool");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(
				"etc/img/icons/icon128.png"));

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				ToolFrame.this.safeProperties();
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				ToolFrame.this.safeProperties();
				GuiService.getInstance().toogleConfTools();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				ToolFrame.this.safeProperties();
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

	public void init() {
		SimConfigPanel panel = SimConfigPanel.getInstance();
		this.add(panel);
		this.pack();

		try {
			this.confToolFrameXPos = UserConfigService.getInstance()
					.getInteger("CONFTOOLFRAME_XPOS");
		} catch (Exception e) {
			this.confToolFrameXPos = 100;
		}

		try {
			this.confToolFrameYPos = UserConfigService.getInstance()
					.getInteger("CONFTOOLFRAME_YPOS");
		} catch (Exception e) {
			this.confToolFrameYPos = 100;
		}

		try {
			this.confToolFrameWidth = UserConfigService.getInstance()
					.getInteger("CONFTOOLFRAME_WIDTH");
		} catch (Exception e) {
			this.confToolFrameWidth = 500;
		}

		try {
			this.confToolFrameHeight = UserConfigService.getInstance()
					.getInteger("CONFTOOLFRAME_HEIGTH");
		} catch (Exception e) {
			this.confToolFrameHeight = 750;
		}

		this.setBounds(this.confToolFrameXPos, this.confToolFrameYPos,
				this.confToolFrameWidth, this.confToolFrameHeight);

	}

	private void safeProperties() {
		UserConfigService.getInstance().setInteger("CONFTOOLFRAME_XPOS",
				this.getX());
		UserConfigService.getInstance().setInteger("CONFTOOLFRAME_YPOS",
				this.getY());
		UserConfigService.getInstance().setInteger("CONFTOOLFRAME_WIDTH",
				this.getWidth());
		UserConfigService.getInstance().setInteger("CONFTOOLFRAME_HEIGTH",
				this.getHeight());
	}
}
