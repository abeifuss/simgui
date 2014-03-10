package evaluation.simulator.gui.service;

import evaluation.simulator.gui.customElements.ProgressWorker;

public class ProgressRegistry {
	private static ProgressRegistry instance = null;
	
	private ProgressWorker progressWorker;
	
	private ProgressRegistry() {}
	
	public static ProgressRegistry getInstance() {
        if (instance == null) {
            instance = new ProgressRegistry();
        }
        return instance;
    }

	public ProgressWorker getProgressWorker() {
		// TODO Auto-generated method stub
		return progressWorker;
	}
	
	public ProgressWorker setProgressWorker( ProgressWorker progress ) {
		return progressWorker = progress;
	}
}
