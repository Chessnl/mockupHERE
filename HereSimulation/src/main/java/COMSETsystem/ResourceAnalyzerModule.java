package COMSETsystem;

import java.io.Serializable;

public abstract class ResourceAnalyzerModule implements Serializable {

	CityMap map;

	public ResourceAnalyzerModule(CityMap map) {
		this.map = map;
	}

	/**
	 * This method should be overridden in every ResourceAnalyzer implementation in order to produce an object
	 * the agents can use to guide themselves where to go
	 *
	 * @param e - Intersection where a resource appears
	 * @return Object that the Agents will use as a guide to know where to go
	 */
	public Object updateList(Intersection e){return null;}
	
}
