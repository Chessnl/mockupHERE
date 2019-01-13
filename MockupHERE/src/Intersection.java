import java.io.Serializable;
import java.util.*;
import java.lang.Math.*;

public class Intersection implements Serializable {
	private static final long serialVersionUID = -6287246803998233161L;

	final public double longitude, latitude;
	final public long id;

	private Set<Road> adjacentRoads = new HashSet<>();
	private Set<Intersection> adjacentIntersections = new HashSet<>();

	// from i to this
	private java.util.Map<Intersection, Road> roadsMapTo = new HashMap<>();

	// from this to i
	private java.util.Map<Intersection, Road> roadsMapFrom = new HashMap<>();

	Intersection (double longitude, double latitude, long id) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.id = id;
	}

	protected void addEdge (Intersection i, double distance, long speed) {
		Road r = new Road(this, i, distance, speed);
		adjacentRoads.add(r);
		adjacentIntersections.add(i);
		roadsMapFrom.put(i, r);
		i.roadsMapTo.put(this, r);
	}

	protected void removeEdge (Intersection i) {
		adjacentRoads.remove(roadsMapFrom.get(i));
		adjacentIntersections.remove(i);
		roadsMapFrom.remove(i);
		i.roadsMapTo.remove(this);
	}

	protected void cutNode () {
		for (Road roadFrom : roadsMapFrom.values()) {
			for (Road roadTo : roadsMapTo.values()) {
				roadTo.from.addEdge(roadFrom.to, roadFrom.distance +
				roadTo.distance, Math.min(roadFrom.speed, roadTo.speed));
			}
		}

		boolean check = true;
		while (check) {
			check = false;
			for (Intersection i : roadsMapFrom.keySet()) {
				removeEdge(i);
				check = true;
				break;
			}
		}

		check = true;
		while (check) {
			check = false;
			for (Intersection i : roadsMapTo.keySet()) {
				i.removeEdge(this);
				check = true;
				break;
			}
		}
	}

	// code to make the map unmodifiable
	protected void fixStructure () {
		adjacentRoads = Collections.unmodifiableSet(adjacentRoads);
		adjacentIntersections = Collections.unmodifiableSet(adjacentIntersections);
		roadsMapFrom = Collections.unmodifiableMap(roadsMapFrom);
		roadsMapTo = Collections.unmodifiableMap(roadsMapTo);
	}

	// returns whether Intersection i is adjacent to this
	protected boolean isAdjacent (Intersection i) {
		// @TODO I changed this evaluation to all reachable nodes from this, I dont know if you need your structure
		// return (roadsMapFrom.keySet().contains(i) || roadsMapTo.keySet().contains(i));
		return adjacentIntersections.contains(i);
	}

	// returns the road from this to Intersection i, null if i is not adjacent to this
	public Road roadTo (Intersection i) throws Exception {
		return roadsMapFrom.get(i);
	}

	public Set<Road> getRoadsFrom () {
		return new HashSet<>(roadsMapFrom.values());
	}

	public Set<Road> getRoadsTo () {
		return new HashSet<>(roadsMapTo.values());
	}

	public Set<Intersection> getAdjacentFrom () {
		return roadsMapFrom.keySet();
	}

	public Set<Intersection> getAdjacentTo () {
		return roadsMapTo.keySet();
	}

	public boolean equals(Intersection i) {
		return (i.id == this.id);
	}

	// returns all roads connected to this
	public Set<Road> getAdjacentRoads () {
		return adjacentRoads;
	}

	// returns all Intersections connected to this
	public Set<Intersection> getAdjacentIntersections () {
		return adjacentIntersections;
	}
}
