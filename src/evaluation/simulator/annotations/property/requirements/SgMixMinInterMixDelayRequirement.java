package evaluation.simulator.annotations.property.requirements;

import java.util.HashSet;
import java.util.Set;

import evaluation.simulator.annotations.property.Requirement;
import evaluation.simulator.annotations.property.SimProp;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class SgMixMinInterMixDelayRequirement extends Requirement {
	
	
@Override
public boolean check() {
	SimPropRegistry gcr = SimPropRegistry.getInstance();
	SimProp thisSimprop = gcr.getProperties().get("SGMIX_MIN_INTER_MIX_DELAY");
	SimProp dependSimprop = gcr.getProperties().get("SGMIX_MAX_INTER_MIX_DELAY");
	
	Integer valuethisSimpProp = (Integer)thisSimprop.getValue();
	Integer valuedependSimprop = (Integer)dependSimprop.getValue();

	String msg = "Depedency Violation: Must be equal or less than \"SGMIX_MAX_INTER_MIX_DELAY\"";

	if( thisSimprop.getErrors() == null ){
		thisSimprop.setErrors( new HashSet<String>() );
	}
	Set<String> errors = thisSimprop.getErrors();
	
	if (!(valuethisSimpProp <= valuedependSimprop)){
		errors.add(msg);
	} else {
		errors.remove(msg);
	}
	thisSimprop.setErrors(errors);
	return true;
}

}
