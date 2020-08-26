package model;

public class Server implements Comparable<Server> {

    private final ServerType type;

    private double cost;

    public Server(ServerType type, double cost) {
        this.type = type;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public ServerType getType() {
        return type;
    }

    public Integer getCpus() {
        return this.type.getCpus();
    }

    public void updateCost(double cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(Server server) {
        Double cost1 = server.getCost();
        Double cost2 = this.getCost();

        return cost2.compareTo(cost1);

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Server) {
            Server s1 = (Server)obj;
            this.getType().equals(s1.getType());
        }
        return false;
    }
}
