package COMSETsystem;

public abstract class ResourceAnalyzerModule {

	public CityMap map;

	public ResourceAnalyzerModule(CityMap map) {
		this.map = map;
	}

	/**
	 * This method is invoked whenever a resource becomes available. The resource analyzer module should update accordingly
	 * @param e - Intersection where a resource appears
	 */
	public abstract void newResource(Intersection e);
	
}