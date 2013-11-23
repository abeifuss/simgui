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
		this.setVisible(false);

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

		this.confToolFrameXPos  = UserConfigService.getCONFTOOLFRAME_XPOS();
		this.confToolFrameYPos  = UserConfigService.getCONFTOOLFRAME_YPOS();
		this.confToolFrameWidth = UserConfigService.getCONFTOOLFRAME_WIDTH();
		this.confToolFrameHeight = UserConfigService.getCONFTOOLFRAME_HEIGHT();

		this.setBounds(this.confToolFrameXPos, this.confToolFrameYPos,
				this.confToolFrameWidth, this.confToolFrameHeight);

	}

	private void safeProperties() {
		
		UserConfigService.setCONFTOOLFRAME_HEIGHT(this.getHeight());
		UserConfigService.setCONFTOOLFRAME_WIDTH(this.getWidth());
		UserConfigService.setCONFTOOLFRAME_XPOS(this.getX());
		UserConfigService.setCONFTOOLFRAME_YPOS(this.getY());
	}
}
