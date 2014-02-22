package evaluation.simulator.gui.actionListeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.batik.transcoder.Transcoder;

import com.google.common.io.Files;

import evaluation.simulator.gui.layout.SimulationTab;
import evaluation.simulator.gui.results.GnuplotPanel;

/**
 * @author nachkonvention
 * 
 *         implements the ActionListener for exporting Simulator results into a
 *         given picture format
 * 
 */
public class ExportButtonAction implements ActionListener {
	Transcoder t;

	@Override
	public void actionPerformed(ActionEvent e) {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
		fileChooser.addChoosableFileFilter(new PngSaveFilter());
		fileChooser.addChoosableFileFilter(new EpsSaveFilter());

		int state = fileChooser.showSaveDialog(null);

		if (state == JFileChooser.APPROVE_OPTION) {

			String ext = "";

			String extension = fileChooser.getFileFilter().getDescription();

			Component selected = SimulationTab.getInstance().getResultsPanel().getSelectedComponent();
			GnuplotPanel selectedPanel = (GnuplotPanel) selected;
			String svgURI = selectedPanel.svgCanvas.getURI().replace("file:", "");
			String exportName;
			String exportPath;

			if (extension.equals("*.png")) {
				ext = ".png";
				exportName = fileChooser.getSelectedFile().getName() + ext;
				exportPath = fileChooser.getSelectedFile().getAbsolutePath() + ext;
				try {
					Files.copy(new File(svgURI.replace("svg", "png")), new File(exportPath));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Could not export " + exportName, "Export Error",
							JOptionPane.ERROR_MESSAGE);
				}
				JOptionPane.showMessageDialog(null, "Successfully exported " + exportName, "Export Successful",
						JOptionPane.INFORMATION_MESSAGE);
			}
			if (extension.equals("*.eps")) {
				ext = ".eps";
				exportName = fileChooser.getSelectedFile().getName() + ext;
				exportPath = fileChooser.getSelectedFile().getAbsolutePath() + ext;
				try {
					Files.copy(new File(svgURI.replace("svg", "eps")), new File(exportPath));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Could not export " + exportName, "Export Error",
							JOptionPane.ERROR_MESSAGE);
				}
				JOptionPane.showMessageDialog(null, "Successfully exported " + exportName, "Export Successful",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private class PngSaveFilter extends FileFilter {
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return false;
			}

			String s = f.getName();

			return s.endsWith(".png") || s.endsWith(".PNG");
		}

		public String getDescription() {
			return "*.png";
		}
	}

	private class EpsSaveFilter extends FileFilter {
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return false;
			}

			String s = f.getName();

			return s.endsWith(".eps") || s.endsWith(".EPS");
		}

		public String getDescription() {
			return "*.eps";
		}
	}
}
