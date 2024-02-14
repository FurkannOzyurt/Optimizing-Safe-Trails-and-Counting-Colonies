import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public static boolean DFS(HashMap<Integer, List<Integer>> map, Set<Integer> visited_cities, Stack<Integer> stack_cities, List<Integer> trap, int city) {
        visited_cities.add(city);
        stack_cities.push(city);

        for (int neighbor : map.get(city)) {
            if (!visited_cities.contains(neighbor)) {
                if (DFS(map, visited_cities, stack_cities, trap, neighbor)) {
                    return true;
                }
            }
            else if (stack_cities.contains(neighbor)) {
                int index = stack_cities.indexOf(neighbor);
                for (int i = index; i <= stack_cities.size() - 1; i++) {
                    trap.add(stack_cities.get(i));
                }
                trap.add(neighbor);
                return true;
            }
        }

        stack_cities.pop();
        return false;
    }
    public static void has_trap(HashMap<Integer, List<Integer>> map, List<Integer> cycle_list) {
        Set<Integer> visited_city = new HashSet<>();
        Stack<Integer> processedNodes = new Stack<>();

        for (int city : map.keySet()) {
            if (!visited_city.contains(city)) {
                if (DFS(map, visited_city, processedNodes, cycle_list, city)) {
                    return;
                }
            }
        }
    }
    public List<List<Integer>> revealTraps() {

        // Trap positions for each colony, should contain an empty array if the colony is safe.
        // I.e.:
        // 0 -> [2, 15, 16, 31]
        // 1 -> [4, 13, 22]
        // 3 -> []
        // ...
        List<List<Integer>> traps = new ArrayList<>();

        // Identify the time traps and save them into the traps variable and then return it.
        // TODO: Your code here
        for (Colony colony: this.colonies) {
            List<Integer> trap = new ArrayList<>();
            has_trap(colony.roadNetwork,trap);
            Set<Integer> trap_set = new HashSet<Integer>(trap);
            List<Integer> real_trap = new ArrayList<Integer>();
            real_trap.addAll(trap_set);
            Collections.sort(real_trap);
            traps.add(real_trap);
        }

        return traps;
    }

    public void printTraps(List<List<Integer>> traps) {
        // For each colony, if you have encountered a time trap, then print the cities that create the trap.
        // If you have not encountered a time trap in this colony, then print "Safe".
        // Print the your findings conforming to the given output format.
        // TODO: Your code here
        System.out.println("Danger exploration conclusions:");
        int col_num = 1;
        for (List<Integer> trap: traps) {
            if (trap.size() == 0){
                System.out.println("Colony " + col_num + ": Safe" );
            }
            else {
                System.out.println("Colony " + col_num + ": Dangerous. Cities on the dangerous path: " + trap);
            }
            col_num++;
        }
    }

}
