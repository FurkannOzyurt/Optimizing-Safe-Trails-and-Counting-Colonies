import java.io.*;
import java.util.*;

public class Kingdom {

    // TODO: You should add appropriate instance variables.
    public ArrayList<ArrayList<Integer>> adj_matrix = new ArrayList<>();
    public  ArrayList<ArrayList<Integer> > adj_list = new ArrayList<>();

    public void initializeKingdom(String filename) {
        // Read the txt file and fill your instance variables
        // TODO: Your code here
        File file = new File(filename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().trim().split("\\s+");
            ArrayList<Integer> row = new ArrayList<>();

            for (String num : line) {
                row.add(Integer.parseInt(num));
            }
            adj_matrix.add(row);
        }

        for (int i = 0; i < adj_matrix.size(); i++) {
            adj_list.add(i, new ArrayList<>());
        }

        for (int i = 0; i < adj_matrix.size(); i++) {
            for (int j = 0; j < adj_matrix.size(); j++) {
                if (adj_matrix.get(i).get(j) == 1){
                    adj_list.get(i).add(j);
                    adj_list.get(j).add(i);
                }
            }
        }
    }

    void DFS(int city, boolean[] visited,Colony colony)
    {
        visited[city] = true;
        colony.cities.add(city+1);
        for (int element : adj_list.get(city)) {
            if (!visited[element])
                DFS(element, visited,colony);
        }
    }
    public List<Colony> getColonies() {
        List<Colony> colonies = new ArrayList<>();
        // TODO: DON'T READ THE .TXT FILE HERE!
        // Identify the colonies using the given input file.
        // TODO: Your code here
        int col_num = 0;
        boolean[] visited = new boolean[adj_matrix.size()];
        for (int i = 0; i < adj_matrix.size(); ++i) {
            if (!visited[i]) {
                Colony colony = new Colony();
                DFS(i, visited,colony);
                colonies.add(colony);
            }
        }

        for (Colony colony: colonies) {
            for (int city: colony.cities) {
                ArrayList<Integer> road_net = new ArrayList<>();
                for (int i = 0; i < adj_matrix.size(); i++) {
                    if (adj_matrix.get(city-1).get(i) == 1){
                        road_net.add(i+1);
                    }
                }
                colony.roadNetwork.put(city,road_net);
            }
        }

        return colonies;
    }

    public void printColonies(List<Colony> discoveredColonies) {
        // Print the given list of discovered colonies conforming to the given output format.
        // TODO: Your code here
        System.out.println("Discovered colonies are:");
        int colony_no = 1;
        for (Colony component : discoveredColonies) {
            Collections.sort(component.cities);
            System.out.println("Colony " + colony_no + ": " + component.cities);
            colony_no++;
        }
    }
}
