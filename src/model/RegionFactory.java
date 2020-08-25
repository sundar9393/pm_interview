package model;

import java.util.LinkedList;
import java.util.List;

import static model.Constants.*;
import static model.ServerTypes.*;

/*
Factory class to create regions with enlisted servers and corresponding prices
 */
public class RegionFactory {


    /*
    Factory method that creates region objects based on regionName input parameter
     */
    public final Region getRegion(String regionName) {

        if(regionName == null) return null;

        switch (regionName.toLowerCase()) {
            case US_EAST:
                return getUSEast();
            case US_WEST:
                return getUSWest();
            case ASIA:
                return getAsia();
            default:
                return null;
        }
    }

    /*
    Seeds server data for US_EAST region
     */
    private static Region getUSEast() {

        List<Server> serverList = new LinkedList<>();
        serverList.add(new Server(LARGE, 0.12));
        serverList.add(new Server(LARGE_X, 0.23));
        serverList.add(new Server(LARGE_2X, 0.45));
        serverList.add(new Server(LARGE_4X, 0.774));
        serverList.add(new Server(LARGE_8X, 1.4));
        serverList.add(new Server(LARGE_10X, 2.82));

        Region region = new Region(US_EAST,serverList);

        return region;

    }

    /*
    Seeds server data for US_WEST region
     */
    private static Region getUSWest() {

        List<Server> serverList = new LinkedList<>();
        serverList.add(new Server(LARGE, 0.14));
        serverList.add(new Server(LARGE_2X, 0.413));
        serverList.add(new Server(LARGE_4X, 0.89));
        serverList.add(new Server(LARGE_8X, 1.3));
        serverList.add(new Server(LARGE_10X, 2.97));

        Region region = new Region(US_WEST,serverList);

        return region;
    }


    /*
    Seeds server data for ASIA region
     */
    private static Region getAsia() {

        List<Server> serverList = new LinkedList<>();
        serverList.add(new Server(LARGE, 0.11));
        serverList.add(new Server(LARGE_X, 0.20));
        serverList.add(new Server(LARGE_4X, 0.67));
        serverList.add(new Server(LARGE_8X, 1.18));

        Region region = new Region(ASIA,serverList);

        return region;
    }
}
