package model;

import java.util.*;

import static model.ServerTypes.*;
import static model.Constants.*;


public class Region {

    private final String name;

    private List<Server> servers;

    public Region(String name, List<Server> servers) {
        this.name = name;
        this.servers = servers;
    }

    public String getName() {
        return name;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }


}
