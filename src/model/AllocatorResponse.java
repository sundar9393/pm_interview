package model;

import java.util.Map;

public class AllocatorResponse implements Comparable<AllocatorResponse> {

    private String region;

    private double total_cost;

    private Map<String, Integer> servers;

    public AllocatorResponse() {
    }

    public AllocatorResponse(String region, double total_cost, Map<String, Integer> servers) {
        this.region = region;
        this.total_cost = total_cost;
        this.servers = servers;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public Map<String, Integer> getServers() {
        return servers;
    }

    public void setServers(Map<String, Integer> servers) {
        this.servers = servers;
    }

    @Override
    public String toString() {
        return this.getRegion()+"\n"+this.total_cost+"\n"+servers.toString();
    }

    @Override
    public int compareTo(AllocatorResponse o) {
        Double cost1 = o.getTotal_cost();
        Double cost2 = this.getTotal_cost();
        return cost2.compareTo(cost1);
    }
}
