package COMSETsystem;

import java.io.Serializable;

public class Road implements Serializable {
	private static final long serialVersionUID = -3225294420877470250L;

	public final Intersection from;
	public final Intersection to;
	public final double distance;
	public final long time;
	public final long speed;

	public Road (Intersection from, Intersection to, double distance, long speed) {
		this.from = from;
		this.to = to;
		this.distance = distance;
		this.speed = speed;
		this.time = (long)(distance/(double)speed);
	}

	public boolean equals(Road road) {
		return (road.from.equals(this.from) && road.to.equals(this.to));
	}
}
