package model;

import java.util.*;

import static model.ServerTypes.*;
import static model.Constants.*;

//Seeds server data into regions
public class Region {


    public static List<Server> getRegionalServers(String region) {

        if(US_EAST.equalsIgnoreCase(region)) return getUSEast();
        else if(US_WEST.equalsIgnoreCase(region)) return getUSWest();
        else if(ASIA.equalsIgnoreCase(region)) return getAsia();
        else return null;

    }

    public static double serverCost(int hours, Map<ServerTypes, Integer> serverList, String region) {
        List<Server> regionalServerList = getRegionalServers(region);
        double cost = 0;


        for(Map.Entry entry: serverList.entrySet()) {

            ServerTypes serverType = (ServerTypes) entry.getKey();

            Optional<Server> server = regionalServerList.stream().
                    filter(f -> f.getType().name().equalsIgnoreCase(serverType.name())).findFirst();

            if(server.isPresent()) {
                cost += server.get().getCost() * (Integer)entry.getValue();
            }

        }

        return cost*hours;
    }


    private static List<Server> getUSEast() {

        List<Server> serverList = new LinkedList<>();
        serverList.add(new Server(LARGE, 0.12));
        serverList.add(new Server(LARGE_X, 0.23));
        serverList.add(new Server(LARGE_2X, 0.45));
        serverList.add(new Server(LARGE_4X, 0.774));
        serverList.add(new Server(LARGE_8X, 1.4));
        serverList.add(new Server(LARGE_10X, 2.82));

        return serverList;

    }

    private static List<Server> getUSWest() {

        List<Server> serverList = new LinkedList<>();
        serverList.add(new Server(LARGE, 0.14));
        serverList.add(new Server(LARGE_2X, 0.413));
        serverList.add(new Server(LARGE_4X, 0.89));
        serverList.add(new Server(LARGE_8X, 1.3));
        serverList.add(new Server(LARGE_10X, 2.97));

        return serverList;

    }

    private static List<Server> getAsia() {

        List<Server> serverList = new LinkedList<>();
        serverList.add(new Server(LARGE, 0.11));
        serverList.add(new Server(LARGE_X, 0.20));
        serverList.add(new Server(LARGE_4X, 0.67));
        serverList.add(new Server(LARGE_8X, 1.18));

        return serverList;

    }


}
