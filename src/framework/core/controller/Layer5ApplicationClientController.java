/*
 * gMix open source project - https://svs.informatik.uni-hamburg.de/gmix/
 * Copyright (C) 2012  Karl-Peter Fuchs
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package framework.core.controller;

import framework.core.AnonNode;
import framework.core.clock.Clock;
import framework.core.gui.model.XMLResource;
import framework.core.interfaces.Layer5ApplicationClient;
import framework.core.userDatabase.UserDatabase;
import framework.infoService.InfoServiceClient;

public class Layer5ApplicationClientController extends Controller implements Layer5ApplicationClient {

	public Layer5ApplicationClientController(AnonNode anonNode, XMLResource settings, UserDatabase userDatabase,
			Clock clock, InfoServiceClient infoService) {
		super(anonNode, settings, userDatabase, clock, infoService);
	}

	@Override
	public void instantiateSubclass() {
		String packag = settings.getPropertyAsString("/gMixConfiguration/composition/layer5/client/plugIn/package");
		LocalClassLoader.instantiateImplementation(packag, "ClientPlugIn.java", this, Layer5ApplicationClient.class);
	}

}