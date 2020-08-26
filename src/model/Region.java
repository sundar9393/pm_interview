package model;

import java.util.*;


public class Region {

    private final String name;

    private final List<Server> servers;

    public Region(String name, List<Server> servers) {

        this.name = name;
        //Sort the list before saving
        servers.sort(Comparator.comparing(Server::getCpus));
        this.servers = new LinkedList<>(servers);
    }

    public String getName() {
        return name;
    }

    public List<Server> getServers() {
        return new LinkedList<>(servers);
    }

    public boolean addServer(Server server) {

        if(!servers.contains(server)) {
            servers.add(server);
            servers.sort(Comparator.comparing(Server::getCpus));
            return true;
        }
        return false;
    }


}
