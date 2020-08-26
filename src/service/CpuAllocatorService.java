package service;

import model.*;
import util.AllocatorCommonUtil;

import static model.Constants.*;

import java.text.DecimalFormat;
import java.util.*;

public class CpuAllocatorService {

    /*
    This method selects the optimum number of servers from every region to cater the no of cpus requested
    & calculates the cost for them based on region.
     */
    public static List<AllocatorResponse> getCpus(int cpu, int hours) {

        //Create a list of return objects
        List<AllocatorResponse> allocatorResponses = new LinkedList<>();

        Map<String, Map<ServerTypes, Integer>> serversList = getServersToMatchCpus(cpu);

        serversList.forEach((key,value)-> {

            double totalCost = AllocatorCommonUtil.serverCostByRegion(hours, value, key);
            //Rounding to 2 decimal spaces
            DecimalFormat df = new DecimalFormat("#.##");
            totalCost = Double.valueOf(df.format(totalCost));

            AllocatorResponse allocatorResponse = new AllocatorResponse(key,totalCost,
                    AllocatorCommonUtil.getSortedServers(value));

            allocatorResponses.add(allocatorResponse);

        });

        return allocatorResponses;
    }

    /*
    This method returns a Map with regionName as key and corresponding value which is a Map that holds ServerTypes as key and no of them as value for the corresponding region
    to cater to certain number of CPUs in optimized way
     */
    private static Map<String, Map<ServerTypes, Integer>> getServersToMatchCpus(int cpuCount) {

        //need to write an algorithm to return min number of Servers to meet he CPU requirement(Greedy Approach)

        Map<String, Map<ServerTypes, Integer>> regionalServerChoices = new HashMap<>();

        //iterating for number of regions
        for(int i = 0; i < REGION_LIST.size(); i++) {

            //Map to hold the servertypes as key and corresponding count as values selected from every region
            Map<ServerTypes, Integer> chosenServers = new HashMap<>();
            //Creating Region object from factory
            Region region = RegionFactory.getRegion(REGION_LIST.get(i));
            List<Server> regionalServers = region.getServers();
            int len = regionalServers.size();
            int cpuRequired = cpuCount;

            while (cpuRequired > 0) {

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

            }
            regionalServerChoices.put(REGION_LIST.get(i),chosenServers);
        }
        return regionalServerChoices;
    }


    /*
    This method calculates maximum CPUS that can be rendered for the specified hours
    costing not more than the capping on cost.
     */
    public static List<AllocatorResponse> getCpusByCostAndHours(double cost, int hours) {
        //The methods returns CPUS based on cost || hours which ever breaches first
        //Tries to return max CPUS possible

        //Create a list of return objects
        List<AllocatorResponse> allocatorResponses = new LinkedList<>();

        Map<String, Map<ServerTypes, Integer>> regionalServerChoices = new HashMap<>();

        //Map to store money that remains if any when choosing servers from a region
        //Happens when the no of hours is reached before money fully spent
        Map<String, Double> moneyLeftMap = new HashMap<>();

        //iterating for number of regions
        for(int i = 0; i < REGION_LIST.size(); i++) {

            Map<ServerTypes, Integer> chosenServers = new HashMap<>();
            Region region = RegionFactory.getRegion(REGION_LIST.get(i));
            List<Server> regionalServers = region.getServers();
            int len = regionalServers.size();

            int timeLeft = hours;
            double moneyLeft = cost;
            for(int j = len-1; j>=0; j--) {

                Server s1 = regionalServers.get(j);
                double costPerHour = s1.getCost();

                //idea: it is to have the max number of servers possible in specified hours
                while((costPerHour < moneyLeft) && timeLeft != 0) {

                    if(timeLeft == 0) break;
                    //Add server to chosen servers map
                    chosenServers.put(s1.getType(), chosenServers.getOrDefault(s1.getType(), 0)+1);
                    //deduct cost of the chosen server from money left
                    moneyLeft -= s1.getCost();
                    //reduce hours by one
                    --timeLeft;

                }
            }
            //Before iterating to next region add servers chosen from a region to main map
            regionalServerChoices.put(REGION_LIST.get(i),chosenServers);
            //Add the money left if any to the map
            moneyLeftMap.put(REGION_LIST.get(i),moneyLeft);

        }

        regionalServerChoices.forEach((key,value) -> {

            double totalCost = cost - moneyLeftMap.get(key);
            DecimalFormat df = new DecimalFormat("#.##");
            totalCost = Double.valueOf(df.format(totalCost));

            AllocatorResponse allocatorResponse = new AllocatorResponse(key,
                    totalCost,AllocatorCommonUtil.getSortedServers(value));
            allocatorResponses.add(allocatorResponse);
        });


        return allocatorResponses;
    }


}
