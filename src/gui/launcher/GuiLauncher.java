package gui.launcher;

import gui.pluginRegistry.DependencyChecker;
import gui.pluginRegistry.SimPropRegistry;
import gui.service.GuiService;

import javax.swing.UnsupportedLookAndFeelException;

public class GuiLauncher {

	public static void main(String[] args) {
		
		
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		gcr.scan();
		
		// initial dependency-check for per plugin configurations
		DependencyChecker.checkAll(gcr);

		// Change Look and Feel to GTK
		for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
				.getInstalledLookAndFeels()) {
			if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info
					.getClassName())) {
				try {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				break;
			}
		}

		// spawn tool window
		// @SuppressWarnings("unused")
		// ToolFrame toolFrame = ToolFrame.getInstance();
		
		// spawn main window
		//@SuppressWarnings("unused")
		//MainGui mainGui = MainGui.getInstance();
		
		GuiService.getInstance();
		// spawn help window
	}
}
