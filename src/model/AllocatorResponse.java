package model;

import java.text.DecimalFormat;
import java.util.*;

public final class AllocatorResponse implements Comparable<AllocatorResponse> {

    private final String region;

    private final double total_cost;

    private final LinkedHashMap<String, Integer> servers;

    public AllocatorResponse(String region, double total_cost, Map<ServerType, Integer> servers) {
        this.region = region;
        this.total_cost = roundOffCost(total_cost);
        this.servers = new LinkedHashMap<>(getSortedServers(servers));
    }

    public String getRegion() {
        return region;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public LinkedHashMap<String, Integer> getServers() {
        return new LinkedHashMap<String, Integer>(servers);
    }


    @Override
    public int compareTo(AllocatorResponse o) {
        Double cost1 = o.getTotal_cost();
        Double cost2 = this.getTotal_cost();
        return cost2.compareTo(cost1);
    }


    /*
    Utility method to roundOff the cost to 2 decimal places
     */
    private final double roundOffCost(double cost) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(cost));
    }


    /*
   Utility method to sort the Map<ServerType, Count> based on count of servers
    */
    private final Map<String, Integer> getSortedServers(Map<ServerType, Integer> regionalServerChoices) {

        //Create a list of return objects
        Map<String, Integer> sortedServers = new LinkedHashMap<>();

        //Copy Map.entry to a list and sort them
        List<Map.Entry<ServerType, Integer> > list =
                new LinkedList<Map.Entry<ServerType, Integer>>(regionalServerChoices.entrySet());

        list.sort(new Comparator<Map.Entry<ServerType, Integer>>() {
            @Override
            public int compare(Map.Entry<ServerType, Integer> o1, Map.Entry<ServerType, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //Moving from a sorted list to Linked hash map to preserve insertion order
        for(Map.Entry<ServerType, Integer> entry:list) {
            sortedServers.put(entry.getKey().name().toLowerCase(),entry.getValue());
        }

        return sortedServers;
    }
}
