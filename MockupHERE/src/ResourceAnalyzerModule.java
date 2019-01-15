import java.util.List;

public abstract class ResourceAnalyzerModule {
	
	List<Intersection> intersections;

	public ResourceAnalyzerModule(List<Intersection> intersections) {
		this.intersections = intersections;
	}
	
}
