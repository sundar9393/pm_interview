package util;

import model.Region;
import model.RegionFactory;
import model.Server;
import model.ServerTypes;

import java.util.*;

public class AllocatorCommonUtil {

    /*
    Utility method to sort the Map>ServerType, Count> based on count of servers
     */
    public final static Map<String, Integer> getSortedServers(Map<ServerTypes, Integer> regionalServerChoices) {

        //Create a list of return objects
        Map<String, Integer> sortedServers = new LinkedHashMap<>();

        //Copy Map.entry to a list and sort them
        List<Map.Entry<ServerTypes, Integer> > list =
                new LinkedList<Map.Entry<ServerTypes, Integer>>(regionalServerChoices.entrySet());

        list.sort(new Comparator<Map.Entry<ServerTypes, Integer>>() {
            @Override
            public int compare(Map.Entry<ServerTypes, Integer> o1, Map.Entry<ServerTypes, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //Moving from a sorted list to Linked hash map tp preserve insertion order
        for(Map.Entry<ServerTypes, Integer> entry:list) {
            sortedServers.put(entry.getKey().name().toLowerCase(),entry.getValue());
        }

        return sortedServers;
    }


    /*
    Utility methods that returns the cost for certain hours for the given list of serverTypes specific to a region
     */
    public static final double serverCostByRegion(int hours, Map<ServerTypes, Integer> serverList, String regionName) {

        Region region = RegionFactory.getRegion(regionName);
        List<Server> servers = region.getServers();
        double cost = 0;


        for(Map.Entry entry: serverList.entrySet()) {

            ServerTypes serverType = (ServerTypes) entry.getKey();

            Optional<Server> server = servers.stream().
                    filter(f -> f.getType().name().equalsIgnoreCase(serverType.name())).findFirst();

            if(server.isPresent()) {
                cost += server.get().getCost() * (Integer)entry.getValue();
            }

        }

        return cost*hours;
    }


}
