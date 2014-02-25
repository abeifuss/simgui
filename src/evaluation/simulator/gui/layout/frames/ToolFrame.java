package evaluation.simulator.gui.layout.frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.customElements.SimConfigPanel;
import evaluation.simulator.gui.service.GuiService;

/**
 * Window containing the SimProp Configuration when being seperated
 * 
 * @author nachkonvention
 * 
 */
@SuppressWarnings("serial")
public class ToolFrame extends JFrame {

	private static ToolFrame instance = null;
	private SimConfigPanel panel;

	/**
	 * Singleton
	 * 
	 * @return an instance of {@link ToolFrame}
	 */
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

		this.initialize();

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

	/**
	 * initialize the window
	 */
	public void initialize() {
		this.loadProperties();
		this.setPanel(SimConfigPanel.getInstance());
		this.add(this.getPanel());

		this.setTitle("Configuration Tool");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage("etc/img/icons/icon128.png"));

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(this.confToolFrameWidth, this.confToolFrameHeight));

		this.pack();
		this.setVisible(false);

	}

	private void safeProperties() {
		UserConfigService.setCONFTOOLFRAME_HEIGHT(this.getHeight());
		UserConfigService.setCONFTOOLFRAME_WIDTH(this.getWidth());
		UserConfigService.setCONFTOOLFRAME_XPOS(this.getX());
		UserConfigService.setCONFTOOLFRAME_YPOS(this.getY());
	}

	private void loadProperties() {
		this.confToolFrameXPos = UserConfigService.getCONFTOOLFRAME_XPOS();
		this.confToolFrameYPos = UserConfigService.getCONFTOOLFRAME_YPOS();
		this.confToolFrameWidth = UserConfigService.getCONFTOOLFRAME_WIDTH();
		this.confToolFrameHeight = UserConfigService.getCONFTOOLFRAME_HEIGHT();
	}

	/**
	 * @return the panel
	 */
	public SimConfigPanel getPanel() {
		return this.panel;
	}

	/**
	 * @param panel
	 *            the panel
	 */
	public void setPanel(SimConfigPanel panel) {
		this.panel = panel;
	}

}
