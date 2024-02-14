import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TravelMap {

    // Maps a single Id to a single Location.
    public Map<Integer, Location> locationMap = new HashMap<>();

    // List of locations, read in the given order
    public List<Location> locations = new ArrayList<>();

    // List of trails, read in the given order
    public List<Trail> trails = new ArrayList<>();

    // TODO: You are free to add more variables if necessary.

    public void initializeMap(String filename) {
        // Read the XML file and fill the instance variables locationMap, locations and trails.
        // TODO: Your code here
        try {
            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList locationList = doc.getElementsByTagName("Location");

            for (int i = 0; i < locationList.getLength(); i++) {
                Node locationNode = locationList.item(i);
                if (locationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element locationElement = (Element) locationNode;
                    int id = Integer.parseInt(locationElement.getElementsByTagName("Id").item(0).getTextContent());
                    String name = locationElement.getElementsByTagName("Name").item(0).getTextContent();
                    Location new_location = new Location(name,id);
                    locations.add(new_location);
                    locationMap.put(id, new_location);
                }
            }

            NodeList trailList = doc.getElementsByTagName("Trail");
            for (int temp = 0; temp < trailList.getLength(); temp++) {

                Element trailElement = (Element) trailList.item(temp);
                String source = trailElement.getElementsByTagName("Source").item(0).getTextContent();
                String destination = trailElement.getElementsByTagName("Destination").item(0).getTextContent();
                String danger = trailElement.getElementsByTagName("Danger").item(0).getTextContent();
                Location source_loc = locationMap.get(Integer.parseInt(source));
                Location destination_loc = locationMap.get(Integer.parseInt(destination));
                Trail new_trail = new Trail(source_loc,destination_loc,Integer.parseInt(danger));
                trails.add(new_trail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Trail> getSafestTrails() {
        List<Trail> safestTrails = new ArrayList<>();
        // Fill the safestTrail list and return it.
        // Select the optimal Trails from the Trail list that you have read.
        // TODO: Your code here
        Collections.sort(trails);
        Set<Location> visited = new HashSet<>();
        PriorityQueue<Trail> pq = new PriorityQueue<>(Comparator.comparingInt(t -> t.danger));
        // start with first location
        Location first_loc = trails.get(0).source;
        visited.add(first_loc);

        // add all trails connected to first location to priority queue
        for (Trail trail : trails) {
            if (trail.source == first_loc || trail.destination == first_loc) {
                pq.add(trail);
            }
        }

        // continue until all locations are visited
        while (visited.size() < locations.size()) {

            // get minimum danger trail
            Trail min_trail = pq.poll();
            Location source = min_trail.source;
            Location dest = min_trail.destination;

            safestTrails.add(min_trail);

            if (visited.contains(source)){
                visited.add(dest);
            }
            if (visited.contains(dest)){
                visited.add(source);
            }

            for (Trail trail : trails) {
                if (trail.source == dest || trail.destination == dest || trail.source == source || trail.destination == source ) {
                    if (!safestTrails.contains(trail) && !pq.contains(trail)){
                        pq.add(trail);
                    }
                }
                if ((visited.contains(trail.source) && visited.contains(trail.destination))){
                    pq.remove(trail);
                }
            }
        }

        return safestTrails;
    }

    public void printSafestTrails(List<Trail> safestTrails) {
        // Print the given list of safest trails conforming to the given output format.
        // TODO: Your code here
        int total_danger = 0;
        System.out.println("Safest trails are:");
        for (Trail safe_trail : safestTrails){
            System.out.println("The trail from " + safe_trail.source.name + " to " + safe_trail.destination.name + " with danger " + safe_trail.danger);
            total_danger += safe_trail.danger;
        }
        System.out.println("Total danger: " + total_danger);
    }
}
