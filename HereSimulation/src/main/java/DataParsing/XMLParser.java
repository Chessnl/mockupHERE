package DataParsing;

/**
 * Parses the original dataset.
 *
 * An object is created for each agent (parseIndex)
 * Each agent object contains a list with all its timestamps (parseTimestamps)
 * Each change in availability is captured in the events list (parseEvents)
 *
 * Timestamp type Agent: an agent becomes available
 * Timestamp type Resource: an agent becomes occupied (so a resource became available)
 */

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class XMLParser {

    private File file;
    private Scanner sc;
    private String pathIndex;
    private String pathTimestamps;
    private ArrayList<AgentDataset> agents = new ArrayList<>();

    public XMLParser(String pathIndex, String pathTimestamps) {
        this.pathIndex = pathIndex;
        this.pathTimestamps = pathTimestamps;
    }

    public void parseIndex() {

        File file = new File(pathIndex);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            for (int i = 0; i < 500; i++) {
                AgentDataset agent = new AgentDataset();
                String ID = doc.getDocumentElement().getElementsByTagName("cab").item(i).getAttributes().
                        getNamedItem("id").getNodeValue();
                String update = doc.getDocumentElement().getElementsByTagName("cab").item(i).getAttributes().
                        getNamedItem("updates").getNodeValue();
                agent.setID(ID);
                agent.setUpdate(update);
                agents.add(agent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseTimestamps() {
        for (int i = 0; i < agents.size(); i++) {
            String agentName = agents.get(i).getID();
            file = new File(pathTimestamps + agentName + ".txt");
            try {
                sc = new Scanner(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (sc.hasNext()) {
                double lat = sc.nextDouble();
                double lon = sc.nextDouble();
                boolean avail;
                if (sc.nextInt() == 1) {
                    avail = true;
                } else {
                    avail = false;
                }
                int time = sc.nextInt();
                TimestampDataset timestamp = new TimestampDataset(lat, lon, avail, time);
                agents.get(i).addStamp(timestamp);
            }


        }
    }

    public void printData() {
        for (AgentDataset agent: agents) {
            System.out.println("ID is " + agent.getID());
            System.out.println("Update is " + agent.getUpdate());
            for (TimestampDataset stamp: agent.getStamps()) {
                System.out.println("latitude is " + stamp.getLat());
                System.out.println("longitude is " + stamp.getLon());
                System.out.println("available is " + stamp.getAvail());
                System.out.println("time is " + stamp.getTime());
            }
        }
    }

    public ArrayList<TimestampAgRe> parseEvents() {
        ArrayList<TimestampAgRe> events = new ArrayList();
        for (AgentDataset agent: agents) {
            boolean avalPre = false;
            boolean avalNext = false;
            for (TimestampDataset stamp: agent.getStamps()) {
                avalNext = stamp.getAvail();
                if (!avalPre && avalNext) {
                    TimestampAgRe stampAgRe = new TimestampAgRe(stamp.getLat(), stamp.getLon(), stamp.getTime(), "Agent");
                    events.add(stampAgRe);
                }
                if (avalPre && !avalNext) {
                    TimestampAgRe stampAgRe = new TimestampAgRe(stamp.getLat(), stamp.getLon(), stamp.getTime(), "Resource");
                    events.add(stampAgRe);
                }
                avalPre = stamp.getAvail();
            }
        }
        return events;
    }

    public void printEvents(ArrayList<TimestampAgRe> events) {

        for (TimestampAgRe timestamp : events) {
            System.out.println("latitude is " + timestamp.getLat());
            System.out.println("longitude is " + timestamp.getLon());
            System.out.println("time is " + timestamp.getTime());
            System.out.println("type is " + timestamp.getType());
        }
    }

    public ArrayList<AgentDataset> getAgents() {
        return agents;
    }
}
