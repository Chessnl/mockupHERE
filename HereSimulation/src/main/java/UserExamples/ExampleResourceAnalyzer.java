package UserExamples;

import COMSETsystem.CityMap;
import COMSETsystem.Intersection;
import COMSETsystem.ResourceAnalyzerModule;

import java.util.ArrayList;


/**
 * Implements a simple algorithm to produce an intersection that the cars should be near to
 */
public class ExampleResourceAnalyzer extends ResourceAnalyzerModule {

    // Size of amount of resources to be considered
    private int SAMPLESIZE = 30;
    // size of coefficient
    private int DEGREE = 10;

    private ArrayList<Intersection> prevResources;

    public ExampleResourceAnalyzer(CityMap map) {
        super(map);
        //list of all previous n resources
        prevResources = new ArrayList<>();
    }

    @Override
    public Intersection updateList(Intersection e){

        if(prevResources.size() == SAMPLESIZE ){

            prevResources.remove(1);
        }
        prevResources.add(e);
        return giveStatistics();
    }

    private Intersection giveStatistics(){

        Intersection statistic = null;
        double latitude = 0;
        double longitude = 0 ;

        if(prevResources.size() == SAMPLESIZE){
            //least recent information saved is most important and is at postiton 1 of array
            //assign the biggest probability to the position 1 value
            for (int i = 0 ; i < SAMPLESIZE ; i++) {
                latitude += Math.pow((1/DEGREE)*prevResources.get(i).latitude, i+1);
                longitude += Math.pow((1/DEGREE)*prevResources.get(i).longitude, i+1);
            }

            // create the node statistic that will have the latitude and longitude created as above
            return new Intersection(longitude, latitude, -1);
        }else{
            // sample size not big enough, do random walk instead
            return null;
        }

    }
}
