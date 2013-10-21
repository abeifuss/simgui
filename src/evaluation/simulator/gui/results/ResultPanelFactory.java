package evaluation.simulator.gui.results;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

public class ResultPanelFactory {

	public static JPanel getResultPanel()
	{
		return new ChartPanel(LineJFreeChartCreator.createAChart());
	}	
	
}
