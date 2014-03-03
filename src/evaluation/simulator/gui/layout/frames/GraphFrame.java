package evaluation.simulator.gui.layout.frames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import org.apache.batik.swing.JSVGCanvas;

/**
 * Window for dispatching generated graphs (plotted Simulator output)
 * 
 * @author nachkonvention
 * 
 */
public class GraphFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static GraphFrame _instance = null;

	/**
	 * @param uri
	 * @param filename
	 */
	public GraphFrame(String uri, String filename) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagLayout.setConstraints(this, gridBagConstraints);
		this.setLayout(gridBagLayout);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		JSVGCanvas svgCanvas = new JSVGCanvas();
		this.add(svgCanvas, gridBagConstraints);
		svgCanvas.setURI(uri);
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle(filename);
		this.setVisible(true);
	}

	/**
	 * No Singleton. Kind of factory.
	 * 
	 * @param uri
	 * @param filename
	 * @return
	 */
	public static GraphFrame getInstance(String uri, String filename) {
		_instance = new GraphFrame(uri, filename);
		return _instance;
	}
}
