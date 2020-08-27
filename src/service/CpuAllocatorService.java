package service;

import model.*;
import util.AllocatorCommonUtil;
import util.RegionFactory;
import java.util.*;

public class CpuAllocatorService {

    private static final List<String> REGION_LIST = new ArrayList<>();

    static {
        REGION_LIST.add("us-east");
        REGION_LIST.add("us-west");
        REGION_LIST.add("asia");
    }

    /*
    This method selects the optimum number of servers from every region to cater the no of cpus requested
    & calculates the cost for them based on region.
     */
    public final List<AllocatorResponse> getCpusByCountForHours(final int cpu, final int hours) {

        //Create a list of return objects
        List<AllocatorResponse> allocatorResponses = new LinkedList<>();

        Map<String, Map<ServerType, Integer>> serversList = getServersToMatchCpus(cpu);

        serversList.forEach((key,value)-> {

            double totalCost = AllocatorCommonUtil.serverCostByRegion(hours, value, key);
            AllocatorResponse allocatorResponse = new AllocatorResponse(key,totalCost,value);
            allocatorResponses.add(allocatorResponse);

        });

        Collections.sort(allocatorResponses);
        return allocatorResponses;
    }

    /*
    This method returns a Map with regionName as key and corresponding value which is a Map that holds ServerTypes as
    key and no of them as value for the corresponding region to cater to certain number of CPUs in optimized way
     */
    private static Map<String, Map<ServerType, Integer>> getServersToMatchCpus(int cpuCount) {

        //need to write an algorithm to return min number of Servers to meet he CPU requirement(Greedy Approach)

        Map<String, Map<ServerType, Integer>> regionalServerChoices = new HashMap<>();

        //iterating for number of regions
        for(int i = 0; i < REGION_LIST.size(); i++) {

            //Map to hold the servertypes as key and corresponding count as values selected from every region
            Map<ServerType, Integer> chosenServers = new HashMap<>();
            //Creating Region object from factory
            Region region = RegionFactory.getRegion(REGION_LIST.get(i));
            List<Server> regionalServers = region.getServers();
            int len = regionalServers.size();
            int cpuRequired = cpuCount;

            //iterating against each server in the region in reverse
            // to always chose the server with max cpus that can fit in our requirements
            for(int j = len-1; j >=0; j--) {
                Server s1 = regionalServers.get(j);
                int serverSize = s1.getType().getCpus();
                int noOfServers;
                if(serverSize <= cpuRequired) {
                    noOfServers = cpuRequired/serverSize;
                    cpuRequired -= noOfServers*serverSize;
                    chosenServers.put(s1.getType(),chosenServers.getOrDefault(s1.getType(),0)+noOfServers);
                }

            }
            regionalServerChoices.put(REGION_LIST.get(i),chosenServers);
        }
        return regionalServerChoices;
    }


    /*
    This method calculates maximum CPUS that can be rendered for the specified hours
    costing not more than the capping on cost.
     */
    public final List<AllocatorResponse> getCpusByCostAndHours(final double cost, final int hours) {

        //Create a list of return objects
        List<AllocatorResponse> allocatorResponses = new LinkedList<>();
        Map<String, Map<ServerType, Integer>> regionalServerChoices = getCpusForHoursAndCost(hours,cost);
        regionalServerChoices.forEach((key,value) -> {
            double totalCost = cost;
            AllocatorResponse allocatorResponse = new AllocatorResponse(key,totalCost,value);
            allocatorResponses.add(allocatorResponse);
        });

        Collections.sort(allocatorResponses);
        return allocatorResponses;
    }

    /*
    Holds the logic to get cpus based on hours and cost
     */
    private static Map<String, Map<ServerType, Integer>> getCpusForHoursAndCost(int hours, double cost) {

        Map<String, Map<ServerType, Integer>> regionalServerChoices = new HashMap<>();

        //iterating for number of regions
        for(int i = 0; i < REGION_LIST.size(); i++) {

            Map<ServerType, Integer> chosenServers = new HashMap<>();
            Region region = RegionFactory.getRegion(REGION_LIST.get(i));
            List<Server> regionalServers = region.getServers();
            int len = regionalServers.size();

            double moneyLeft = cost;
            //Iterate servers starting from one with max capacity
            for(int j = len-1; j>=0; j--) {

                Server s1 = regionalServers.get(j);
                double costPerHour = s1.getCost();
                //Keep adding maximum number of particular server possible till It cannot be added for even an hour
                for(int h = hours; h >=1; h--) {
                    while(h*costPerHour <= moneyLeft) {
                        //Add server to chosen servers map
                        chosenServers.put(s1.getType(), chosenServers.getOrDefault(s1.getType(), 0)+h);
                        //update moneyleft
                        moneyLeft -= h*costPerHour;
                    }
                }

            }
            regionalServerChoices.put(REGION_LIST.get(i),chosenServers);
        }
        return regionalServerChoices;
    }


    /*
    The method returns regions which can provide the demanded number of cpus within the cost constraint.
     */
    public final List<AllocatorResponse> getCpusByNumbersHoursAndCost(final int cpus, final int hours, final double cost) {

        //Create a list of return objects
        List<AllocatorResponse> allocatorResponses = new LinkedList<>();

        //From serverlist1 i need to choose the ones that cost me less (no of cpus and hours are handled already)
        //From serverlist2 I need to choose the ones with more cpus (cost and hours are handled already)
        Map<String, Map<ServerType, Integer>> serversListMatchingCpuCount = getServersToMatchCpus(cpus);
        Map<String, Map<ServerType, Integer>> serversListMatchingCostAndHours = getCpusForHoursAndCost(hours, cost);


        serversListMatchingCpuCount.forEach((key,value)-> {

            double totalCost = AllocatorCommonUtil.serverCostByRegion(hours, value, key);
            if(totalCost <= cost) {
                AllocatorResponse allocatorResponse = new AllocatorResponse(key,totalCost,value);
                allocatorResponses.add(allocatorResponse);
            }

        });

        serversListMatchingCostAndHours.forEach((key,value)-> {
            int cpuCount = AllocatorCommonUtil.getCpuCount(value);

            if(cpuCount >= cpus ) {
                //If the the region is already present choose the one which returns us most CPU and replace it
                AllocatorResponse allocatorResponse = new AllocatorResponse(key,cost,value);

                int index = allocatorResponses.indexOf(allocatorResponse);
                if(index != -1) {
                    AllocatorResponse existingEntry = allocatorResponses.get(index);
                    int existingCpus = AllocatorCommonUtil.getCpuCount_1(existingEntry.getServers());
                    if(existingCpus < cpuCount) {
                        allocatorResponses.remove(index);
                        allocatorResponses.add(allocatorResponse);
                    } else {
                        allocatorResponse = null;
                    }
                }

            }
        });

        Collections.sort(allocatorResponses);
        return allocatorResponses;

    }


}
