package evaluation.simulator.gui.results;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

/**
 * @author Infectiou
 * 
 */
public class ResultPanelFactory {

	/**
	 * @return
	 */
	public static JPanel getResultPanel() {
		return new ChartPanel(LineJFreeChartCreator.createAChart());
		// return new TextResult();
	}

	/**
	 * @param gnuplotResultFileName
	 * @return an instance of {@link GnuPlotPanel}
	 */
	public static JPanel getGnuplotResultPanel(String gnuplotResultFileName) {
		return new GnuplotPanel(gnuplotResultFileName);
	}
}
