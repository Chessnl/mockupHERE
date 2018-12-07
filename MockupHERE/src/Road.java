import java.io.Serializable;

public class Road implements Serializable {
	private static final long serialVersionUID = -3225294420877470250L;
	
	public final Intersection from;
	public final Intersection to;
	public final double distance;
	public final long time;
	public final double speed;
	
	public Road (Intersection from, Intersection to, double distance, long time) {
		this.from = from;
		this.to = to;
		this.distance = distance;
		this.time = time;
		this.speed = distance / time;
	}
}