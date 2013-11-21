package evaluation.simulator.gui.results;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GnuplotPanel extends JPanel {

	public GnuplotPanel(String gnuplotResultFileName) {
		BufferedImage resultsDiagram = null;
		try {
			File f = new File("inputOutput/simulator/output/"+gnuplotResultFileName);
			while(!f.exists()){
				Thread.sleep(1);
			}
			while(resultsDiagram == null){
				resultsDiagram = ImageIO.read(f);
			}
			JLabel diagramLabel = new JLabel(new ImageIcon(resultsDiagram));
			this.add(diagramLabel);
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
