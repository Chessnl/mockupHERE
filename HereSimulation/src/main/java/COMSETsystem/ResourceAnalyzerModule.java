package COMSETsystem;

import java.io.Serializable;

public abstract class ResourceAnalyzerModule implements Serializable {

	CityMap map;

	public ResourceAnalyzerModule(CityMap map) {
		this.map = map;
	}
	
}
