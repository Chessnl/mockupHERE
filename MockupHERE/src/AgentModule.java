
public abstract class AgentModule {
	
	public final Intersection currentLoc;
	public final ResourceAnalyzerModule resMod;
	
	AgentModule (Intersection currentLoc, ResourceAnalyzerModule resMod) {
		this.currentLoc = currentLoc;
		this.resMod = resMod;
	}
	
	abstract Intersection move();
	
}
