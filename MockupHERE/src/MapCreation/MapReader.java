package MapCreation;/*
 *  Reads the input from a json file generated by OpenStreetMaps and 
 *  generates a map as connected nodes

 */
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import COMSETsystem.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class MapReader {
    
    
    final static int speedMotorway = 120;
    final static int speedMotorwayLink = speedMotorway;
    final static int speedTrunk = 100;
    final static int speedTrunkLink = speedTrunk;
    final static int speedPrimary = 100;
    final static int speedPrimaryLink = speedPrimary;
    final static int speedSecondary = 80;
    final static int speedSecondaryLink = speedSecondary;
    final static int speedTertiary = 50;
    final static int speedTertiaryLink = speedTertiary;
    final static int speedUnclassified = 50;
    final static int speedResidential = 50;
    final static int speedLivingStreet = 30;
    final static int speedDefault = 50;
    
    static List<Intersection> intersections;
    
    public static void readMap() {
        intersections = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            Reader reader = new FileReader("maps/Stockholm.json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject);

            // loop array
            JSONArray elements = (JSONArray) jsonObject.get("elements");
            Iterator<JSONArray> iterator = elements.iterator();
            int counter = 0;
            for (Object elementObject : elements) {
                JSONObject element = (JSONObject) elementObject;
                String type = (String) element.get("type");
                if (type.equals("node")) {
                    long id = (long) element.get("id");
                    double latitude = (double) element.get("lat");
                    double longtitude = (double) element.get("lon");
                    intersections.add(new Intersection(longtitude, latitude, counter, id));
                }
            }
            for (Object elementObject : elements) {
                JSONObject element = (JSONObject) elementObject;
                String type = (String) element.get("type");
                if (type.equals("way")) {
                    long id = (long) element.get("id");
                    String highway;
                    int maxSpeed;
                    boolean oneway = false;
                    Object tagsObject = element.get("tags");
                    JSONObject tags = (JSONObject) tagsObject;
                    highway = (String)tags.get("highway");
                    if (tags.containsKey("maxspeed")) {
                        String speedString = (String) tags.get("maxspeed");
                        if( speedString.contains(" mph")) {
                            speedString = speedString.replace(" mph", "");
                        }
                        maxSpeed = Integer.parseInt(speedString);
                    } else {
                        switch(highway) {
                            case "motorway": 
                                maxSpeed = speedMotorway;
                                break;
                            case "motorway_link":
                                maxSpeed = speedMotorwayLink;
                                break;
                            case "trunk":
                                maxSpeed = speedTrunk;
                                break;
                            case "trunk_link":
                                maxSpeed = speedTrunkLink;
                                break;
                            case "primary":
                                maxSpeed = speedPrimary;
                                break;
                            case "primary_link":
                                maxSpeed = speedPrimaryLink;
                                break;
                            case "secondary":
                                maxSpeed = speedSecondary;
                                break;
                            case "secondary_link":
                                maxSpeed = speedSecondaryLink;
                                break;
                            case "tertiary":
                                maxSpeed = speedTertiary;
                                break;
                            case "tertiary_link": 
                                maxSpeed = speedTertiaryLink;
                                break;
                            case "unclassified":
                                maxSpeed = speedUnclassified;
                                break;
                            case "residential":
                                maxSpeed = speedResidential;
                                break;
                            case "living_street":
                                maxSpeed = speedLivingStreet;
                                break;
                            default:
                                maxSpeed = speedDefault;
                                break;
                        }
                    }
                    if(tags.containsKey("oneway") && ((String)tags.get("oneway")).equals("yes")) {
                        oneway = true;
                    }
                    JSONArray nodes = (JSONArray) element.get("nodes");
                    int length = nodes.size();
                    for (int i = 0; i < length - 1; i++) {
                        long node1 = (long)nodes.get(i);
                        long node2 = (long)nodes.get(i+1);
                        boolean foundNode1 = false;
                        boolean foundNode2 = false;
                        int index1 = 0;
                        int index2 = 0;
                        for (int k = 0; k < intersections.size(); k++) {
                            if (intersections.get(k).id == node1) {
                                foundNode1 = true;
                                index1 = k;
                            } else if (intersections.get(k).id == node2) {
                                foundNode2 = true;
                                index2 = k;
                            }
                        }
                        if (!foundNode1 || !foundNode2) {
                            throw new FileNotFoundException("node in way but no in way");
                        }
                        double x1 = intersections.get(index1).longitude;
                        double x2 = intersections.get(index2).longitude;
                        double y1 = intersections.get(index1).latitude;
                        double y2 = intersections.get(index2).latitude;
                        double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                        intersections.get(index1).addEdge(intersections.get(index2), distance, maxSpeed);
                        if (!oneway) {
                            intersections.get(index2).addEdge(intersections.get(index1), distance, maxSpeed);
                        }
                    }
                } 
            } 
        } catch (FileNotFoundException e) {
            System.out.println("error FileNotFoundException");
            System.out.println(e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("error IOException");
            System.out.println(e);
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("error ParseException");
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    public static void clearMapIteration() throws IllegalStateException{
        Set<Intersection> toRemove = new HashSet<>();
        for (int i = 0; i < intersections.size(); i++) {
            Intersection inter = intersections.get(i);
            Set<Road> roadsFrom = inter.getRoadsFrom();
            Set<Road> roadsTo = inter.getRoadsTo();
            if (roadsFrom.size() <= 1 && roadsTo.size() <= 1) {
                toRemove.add(inter);
                continue;
            }
            
            if (roadsFrom.size() == 2 && roadsTo.size() == 2) {
                boolean checkForAll = true;
                for (Intersection interTo: inter.getAdjacentTo()) {
                    boolean check = false;
                    for (Intersection interFrom : inter.getAdjacentFrom()) {
                        if (interTo.equals(interFrom)) {
                            check = true;
                        }
                    }
                    if (!check) {
                        checkForAll = false;
                    }
                }
                if (checkForAll) {
                    toRemove.add(inter);
                    continue;
                }
            }
        }
        for (Intersection inter : toRemove) {
            int j = -1;
            for (int i = 0; i < intersections.size(); i++) {
                if (intersections.get(i).equals(inter)) {
                    j = i;
                    break;
                    
                }
            }
            if (j == -1) {
                throw new IllegalStateException("Sommething went horribly wrong.");
            }
            intersections.get(j).cutNode();
            intersections.remove(j);
            
        }
    }
    
    public static void clearMap() {
        int previousNumberNodes = intersections().size();
        int newNumberNodes = 9999999;
        while (previousNumberNodes != newNumberNodes) {
            clearMapIteration();
            previousNumberNodes = newNumberNodes;
            newNumberNodes = intersections().size();
        }
    }

    public static List<Intersection> intersections() {
        return intersections;
    }
}
