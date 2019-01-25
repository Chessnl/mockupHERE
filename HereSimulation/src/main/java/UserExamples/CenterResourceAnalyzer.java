package UserExamples;

import COMSETsystem.CityMap;
import COMSETsystem.Intersection;
import COMSETsystem.ResourceAnalyzerModule;

import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Implements a simple algorithm to produce an intersection that the cars should be near to
 */
public class CenterResourceAnalyzer extends ResourceAnalyzerModule {

    // Size of amount of resources to be considered
    private int SAMPLESIZE = 30;

    private ArrayList<Intersection> prevResources = new ArrayList<>();

    public CenterResourceAnalyzer(CityMap map) {
        super(map);
    }
    
    @Override
	public void newResource(Intersection e) {
    	if (prevResources.size() == SAMPLESIZE) {
            prevResources.remove(1);
        }
        prevResources.add(e);
	}
    
    
    public Intersection getCenter(){

        double latitude = 0;
        double longitude = 0;
        
        if (prevResources.size() > 0) {
        	for (Intersection i : prevResources) {
        		latitude += i.latitude / prevResources.size();
        		longitude += i.longitude / prevResources.size();
        	}
        	
       
        	// TODO function based on parsing map, should not be needed.
        	try {
				return map.getNearestIntersection(longitude, latitude);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
        	
        }
        
        // no center is defined yet
        return null;
    }
}
