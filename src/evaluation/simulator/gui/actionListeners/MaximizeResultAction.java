package evaluation.simulator.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import evaluation.simulator.gui.layout.frames.GraphFrame;

public class MaximizeResultAction implements ActionListener {

	private String pathToResultsFile;
	private String resultsName;

	public MaximizeResultAction(String pathToResultsFile, String resultsName) {
		super();
		this.pathToResultsFile = pathToResultsFile;
		this.resultsName = resultsName;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GraphFrame.getInstance(pathToResultsFile, resultsName);
	}

}
