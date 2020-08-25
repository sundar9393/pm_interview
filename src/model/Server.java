package model;

public class Server implements Comparable<Server> {

    ServerTypes type;

    double cost;

    public Server(ServerTypes type, double cost) {
        this.type = type;
        this.cost = cost;
    }

    public ServerTypes getType() {
        return type;
    }

    public void setType(ServerTypes type) {
        this.type = type;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(Server server) {
        double cost1 = server.getCost();
        double cost2 = this.getCost();

        return cost2 < cost1 ? -1 : (cost2 == cost1 ? 0 : 1);

    }
}
