package net.sourceforge.jasa.report;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.jabm.event.RoundFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.report.XYReportVariables;
import net.sourceforge.jasa.event.OrderPlacedEvent;
import net.sourceforge.jasa.market.MarketSimulation;

public abstract class MarketPriceReportVariables implements XYReportVariables {

	protected double price;
	
	protected int time;
	
	public static final String PRICE_VAR = "price";
	
	@Override
	public Map<Object, Number> getVariableBindings() {
		LinkedHashMap<Object, Number> result = 
			new LinkedHashMap<Object, Number>();
		result.put(getName() + ".t", time);
		result.put(getName() + "." + PRICE_VAR, price);
		return result;
	}

	@Override
	public void compute(SimEvent ev) {
		eventOccurred(ev);
	}

	@Override
	public void dispose(SimEvent event) {
		// Do nothing
	}

	@Override
	public void initialise(SimEvent event) {
		// Do nothing

	}

	@Override
	public Number getX(int seriesIndex) {
		return this.time;
	}

	@Override
	public Number getY(int seriesIndex) {
		return this.price;
	}
	
	public int getNumberOfSeries() {
		return 1;
	}

	@Override
	public void eventOccurred(SimEvent ev) {
		if (ev instanceof RoundFinishedEvent) {
			RoundFinishedEvent event = (RoundFinishedEvent) ev;
			this.price = getPrice(event);
			this.time = (int) 
					event.getSimulation().getSimulationTime().getTicks();
		}
	}

	@Override
	public List<Object> getyVariableNames() {
		LinkedList<Object> result = new LinkedList<Object>();
		result.add(getName() + "." + PRICE_VAR);
		return result;
 	}

	@Override
	public String getxVariableName() {
		return getName() + ".t";
	}

	@Override
	public abstract String getName();
	
	public abstract double getPrice(RoundFinishedEvent event);

}