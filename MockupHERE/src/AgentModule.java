
public abstract class AgentModule {
	
	public final Intersection currentLoc;
	public final ResourceAvailabilityModule resMod;
	
	AgentModule (Intersection currentLoc, ResourceAvailabilityModule resMod) {
		this.currentLoc = currentLoc;
		this.resMod = resMod;
	}
	
	abstract Intersection move();
	
}
