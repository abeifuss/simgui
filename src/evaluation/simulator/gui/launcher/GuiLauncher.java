package evaluation.simulator.gui.launcher;

import javax.swing.UnsupportedLookAndFeelException;

import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.gui.service.GuiService;

public class GuiLauncher {

	public static void main(String[] args) {

		SimPropRegistry simPropRegistry = SimPropRegistry.getInstance();
		// simPropRegistry.scan();

		// initial dependency-check for per plugin configurations
		DependencyChecker.checkAll(simPropRegistry);

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

		//
		GuiService.getInstance();
	}
}
