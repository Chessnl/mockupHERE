package DataParsing;

/**
 * Parses the CSV file prepared by Bo Xu.
 * Creates the same list of events as the XML parser.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVParser {

    private String path;
    private ArrayList<TimestampAgRe> events = new ArrayList<>();

    public CSVParser(String path) {
        this.path = path;
    }

    public ArrayList<TimestampAgRe> parse() {

        try {
            Scanner sc = new Scanner(new File(path));
            sc.useDelimiter(",|\n");
            sc.nextLine();
            while (sc.hasNext()) {
                Double lat = sc.nextDouble();
                Double lon = sc.nextDouble();
                int time = sc.nextInt();
                String type = sc.next();
                events.add(new TimestampAgRe(lat, lon, time, type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }
}
