import java.util.List;

public abstract class ResourceAvailabilityModule {
	
	List<Intersection> intersections;

	public ResourceAvailabilityModule(List<Intersection> intersections) {
		this.intersections = intersections;
	}
	
}
