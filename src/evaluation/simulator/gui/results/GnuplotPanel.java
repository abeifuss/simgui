package evaluation.simulator.gui.results;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;

import evaluation.simulator.gui.customElements.ConfigChooserPanel;

@SuppressWarnings("serial")
public class GnuplotPanel extends JPanel {

	public JSVGCanvas svgCanvas;
	public static String outputFolder = "inputOutput/simulator/output/";

	public GnuplotPanel(String gnuplotResultFileName) {
		// BufferedImage resultsDiagram = null;
		try {
			File f = new File(outputFolder + gnuplotResultFileName);
			while (!f.exists()) {
				Thread.sleep(1);
			}

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
			svgCanvas = new JSVGCanvas();
			this.add(svgCanvas, gridBagConstraints);
			svgCanvas.setURI(f.toURL().toString());
			ConfigChooserPanel.getInstance().exportPictureButton.setEnabled(true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

}
