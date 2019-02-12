package COMSETsystem;

import java.io.Serializable;

public class Road implements Serializable {
	private static final long serialVersionUID = -3225294420877470250L;

	public Intersection from;
	public Intersection to;
        public long fromID;
        public long toID;
	public final double distance;
	public final long time;
	public final long speed;

	public Road (Intersection from, Intersection to, double distance, long speed) {
		this.from = from;
		this.to = to;
                this.fromID = from.id;
                this.toID = to.id;
		this.distance = distance;
		this.speed = speed;
		this.time = (long)(distance/(double)speed);
	}

	public boolean equals(Road road) {
		return (road.from.equals(this.from) && road.to.equals(this.to));
	}
        
        public void removeIntersections() {
            from = null;
            to = null;
        }
}
