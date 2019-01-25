package UserExamples;

import java.util.Random;

import COMSETsystem.*;

public class AgentCenterWalk extends AgentModule {

	CenterResourceAnalyzer analyzer;
	
	public AgentCenterWalk(Intersection loc, ResourceAnalyzerModule resMod) {
		super(loc, resMod);
		analyzer = (CenterResourceAnalyzer) resMod;
	}

	@Override
	public Intersection move() {
		Intersection c = analyzer.getCenter();
		
		if (c != null) {
			Road nextRoad = null;
			long opt = Long.MAX_VALUE;
			for (Road r : currentLoc.getRoadsFrom()) {
				if (analyzer.map.timeBetween(r.to, c) + r.distance < opt) {
					nextRoad = r;
				}
			}
			return nextRoad.to;
		} else {
	    	Random rnd = new Random();
	        int i = rnd.nextInt(currentLoc.getAdjacentFrom().size());
	        return (Intersection) currentLoc.getAdjacentFrom().toArray()[i];
		}
	}
}
