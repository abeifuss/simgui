/*
 * gMix open source project - https://svs.informatik.uni-hamburg.de/gmix/
 * Copyright (C) 2013  Karl-Peter Fuchs
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
package evaluation.simulator.plugins.plotType;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import evaluation.simulator.annotations.property.StringSimulationProperty;
import evaluation.simulator.core.statistics.ResultSet;

public abstract class Plotter {

	@StringSimulationProperty( name = "Plot script",
			key = "NAME_OF_PLOT_SCRIPT",
			inject = "0:PLOTTYPE,Plottype",
			isStatic = true)
	String plotscript;

	public final static DecimalFormat decimalFormat;
	static {
		DecimalFormatSymbols sym = new DecimalFormatSymbols();
		sym.setDecimalSeparator('.');
		decimalFormat = new DecimalFormat("#.##########", sym);
	}

	public abstract void plot(ResultSet resultSet);

}
