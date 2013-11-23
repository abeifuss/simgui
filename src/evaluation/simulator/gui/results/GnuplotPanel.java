package evaluation.simulator.gui.results;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.batik.swing.JSVGCanvas;

import evaluation.simulator.gui.layout.SimulationTab;

@SuppressWarnings("serial")
public class GnuplotPanel extends JPanel {

	public GnuplotPanel(String gnuplotResultFileName) {
//		BufferedImage resultsDiagram = null;
		try {
			File f = new File("inputOutput/simulator/output/"+gnuplotResultFileName);
			while(!f.exists()){
				Thread.sleep(1);
			}
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.anchor = GridBagConstraints.NORTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
//			gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
//			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
//			gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
//			gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
			gridBagLayout.setConstraints(this, gridBagConstraints);
			this.setLayout(gridBagLayout);
			
			// TODO: Dangerous, may block forever under certain conditions
//			while(resultsDiagram == null){
//				resultsDiagram = ImageIO.read(f);
//			}
			
//			JLabel diagramLabel = new JLabel(new ImageIcon(resultsDiagram));
//			this.add(diagramLabel);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			JSVGCanvas svgCanvas = new JSVGCanvas();
			this.add(svgCanvas,gridBagConstraints);
			svgCanvas.setURI(f.toURL().toString());

			
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
