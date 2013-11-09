package evaluation.simulator.gui.launcher;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.UnsupportedLookAndFeelException;

import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;
import evaluation.simulator.gui.service.GuiService;

public class GuiLauncher {

	public static void main(String[] args) {

		final SimPropRegistry simPropRegistry = null;;
		
		class simPropInitializer implements Callable<Boolean>
		{
		    public Boolean call()
		    {
		        SimPropRegistry.getInstance();
				
				// initial dependency-check for per plugin configurations
				DependencyChecker.checkAll(simPropRegistry);
		        return true;
		    }
		}
		
		final ExecutorService service;
        final Future<Boolean>  task;
		
        service = Executors.newFixedThreadPool(1);
        task    = service.submit(new simPropInitializer());
        
        try 
        {
        	// block until finished
            task.get();
        }
        catch(final InterruptedException ex)
        {
            ex.printStackTrace();
        }
        catch(final ExecutionException ex)
        {
            ex.printStackTrace();
        }

        service.shutdownNow();

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
