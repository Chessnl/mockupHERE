package COMSETsystem;

public abstract class AgentModule {
	
	public final Intersection currentLoc;
	public final ResourceAnalyzerModule resMod;
	
	public AgentModule (Intersection currentLoc, ResourceAnalyzerModule resMod) {
		this.currentLoc = currentLoc;
		this.resMod = resMod;
	}

	/**
	 * This method should be overridden in every Agent implementation in order to return an Intersection that the
	 * Main can use to move the Agent
	 * @return Intersection that the Agent is going to move to
	 */
	public abstract Intersection move();
	
}
