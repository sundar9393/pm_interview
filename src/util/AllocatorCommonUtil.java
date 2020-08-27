package util;

import model.Region;
import model.Server;
import model.ServerType;

import java.util.*;

public class AllocatorCommonUtil {


    /*
    Utility methods that returns the cost for certain hours for the given list of serverTypes specific to a region
     */
    public static final double serverCostByRegion(int hours, Map<ServerType, Integer> serverList, String regionName) {

        Region region = RegionFactory.getRegion(regionName);
        List<Server> servers = region.getServers();
        double cost = 0;


        for(Server server: servers) {

            if(serverList.containsKey(server.getType())) {
                cost += server.getCost() * serverList.get(server.getType());
            }
        }

        return cost*hours;
    }

    /*
    Returns total number of cpus in the given servers
    Takes in a Map with ServerType enum as Key in input
     */
    public static final int getCpuCount(Map<ServerType, Integer> servers) {
        int totalCount = 0;

        for(Map.Entry<ServerType,Integer> entry: servers.entrySet()) {
            totalCount += entry.getKey().getCpus()*entry.getValue();
        }

        return totalCount;
    }

    /*
    Returns total number of cpus in the given servers
    Takes in a Map with String as Key in input
     */
    public static final int getCpuCount_1(Map<String, Integer> servers) {
        int totalCount = 0;

        for(Map.Entry<String,Integer> entry: servers.entrySet()) {
            ServerType serverType = ServerType.valueOf(entry.getKey().toUpperCase());
            if(null != serverType) {
                totalCount += serverType.getCpus()*entry.getValue();
            }
        }

        return totalCount;
    }

}
