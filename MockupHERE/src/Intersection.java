import java.io.Serializable;
import java.util.*;

public class Intersection implements Serializable {
	private static final long serialVersionUID = -6287246803998233161L;
	
	final public double longitude, latitude;
	final public int index; // unique
	
	private Map<Intersection, Road> roadsHash = new HashMap<>();
	private Set<Road> roadsSet = new HashSet<>();
	private Set<Intersection> intersectionsSet = new HashSet<>();
	
	Intersection (double longitude, double latitude, int index) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.index = index;
	}
	
	protected void addEdge(Intersection i, double distance, long time) {
		Road r = new Road(this, i, distance, time);
		roadsHash.put(i, r);
		roadsSet.add(r);
		intersectionsSet.add(i);
	}
	
	protected void fixStructure () {
		roadsHash = Collections.unmodifiableMap(roadsHash);
		roadsSet = Collections.unmodifiableSet(roadsSet);
		intersectionsSet = Collections.unmodifiableSet(intersectionsSet);
	}
	
	public boolean isAdjacent (Intersection i) {
		return roadsHash.get(i.index) != null;
	}
	
	public Road roadTo (Intersection i) throws Exception {
		Road r = roadsHash.get(i.index);
		if (r == null) {
			throw new Exception();
		} else {
			return r;
		}
	}
	
	public Set<Road> getRoads () {
		return roadsSet;
	}
	
	public Set<Intersection> getAdjacent () {
		return intersectionsSet;
	}
}
