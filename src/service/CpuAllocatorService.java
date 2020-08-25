package service;

import model.AllocatorResponse;
import model.Region;
import model.Server;
import model.ServerTypes;
import static model.Constants.*;

import java.text.DecimalFormat;
import java.util.*;

public class CpuAllocatorService {

    //It will take in number of CPU and hours
    public static List<AllocatorResponse> getCpus(int cpu, int hours) {

        //Create a list of return objects
        List<AllocatorResponse> allocatorResponses = new LinkedList<>();

        Map<String, Map<ServerTypes, Integer>> serversList = getServers(cpu);

        serversList.forEach((key,value)-> {

            AllocatorResponse allocatorResponse = new AllocatorResponse();
            allocatorResponse.setRegion(key);
            double totalCost = Region.serverCost(hours, value, key);
            //Rounding to 2 decimal spaces
            DecimalFormat df = new DecimalFormat("#.##");
            totalCost = Double.valueOf(df.format(totalCost));
            allocatorResponse.setTotal_cost(totalCost);
            allocatorResponse.setServers(getSortedServers(value));

            allocatorResponses.add(allocatorResponse);

        });

        return allocatorResponses;
    }

    /*
    This method returns a Map of all chosen <region, servertypes> for all regions
    to cater to certain number of CPUs in optimized way
     */
    private static Map<String, Map<ServerTypes, Integer>> getServers(int cpuCount) {

        //need to write an algorithm to return min number of Servers to meet he CPU requirement(Greedy Approach)

        Map<String, Map<ServerTypes, Integer>> regionalServerChoices = new HashMap<>();

        //iterating for number of regions
        for(int i = 0; i < regionList.size(); i++) {

            Map<ServerTypes, Integer> chosenServers = new HashMap<>();
            List<Server> regionalServers = Region.getRegionalServers(regionList.get(i));
            int len = regionalServers.size();
            int cpuRequired = cpuCount;

            while (cpuRequired > 0) {

                //iterating against each server in the region
                for(int j = len-1; j >=0; j--) {
                    Server s1 = regionalServers.get(j);
                    int serverSize = s1.getType().getCpus();
                    if( serverSize <= cpuRequired) {
                        //add to hashmap
                        chosenServers.put(s1.getType(), chosenServers.getOrDefault(s1.getType(),0)+1);
                        //reduce cpu requiredcount
                        cpuRequired -= serverSize;
                        //break from for loop
                        break;

                    }
                }

            }
            regionalServerChoices.put(regionList.get(i),chosenServers);
        }
        return regionalServerChoices;
    }


    //write a method that takes in max cost and hours
    public static List<AllocatorResponse> getCpusByCostAndHours(double cost, int hours) {

        //I need to choose servers till i hit the max cost or hours
        //Need to choose maximum number of servers possible for the cost/hours
        /*
        i will iterate a region
        check from reverse in servers list
        I will keep adding the servers that fit our time and money constraints
         */

        //Create a list of return objects
        List<AllocatorResponse> allocatorResponses = new LinkedList<>();

        Map<String, Map<ServerTypes, Integer>> regionalServerChoices = new HashMap<>();

        Map<String, Double> moneyLeftMap = new HashMap<>();

        //iterating for number of regions
        for(int i = 0; i < regionList.size(); i++) {

            Map<ServerTypes, Integer> chosenServers = new HashMap<>();
            List<Server> regionalServers = Region.getRegionalServers(regionList.get(i));
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
            regionalServerChoices.put(regionList.get(i),chosenServers);
            //Add the money left if any to the map
            moneyLeftMap.put(regionList.get(i),moneyLeft);

        }

        regionalServerChoices.forEach((key,value) -> {

            AllocatorResponse allocatorResponse = new AllocatorResponse();
            allocatorResponse.setRegion(key);
            double totalCost = cost - moneyLeftMap.get(key);
            DecimalFormat df = new DecimalFormat("#.##");
            totalCost = Double.valueOf(df.format(totalCost));
            allocatorResponse.setTotal_cost(totalCost);
            allocatorResponse.setServers(getSortedServers(value));

            allocatorResponses.add(allocatorResponse);
        });


        return allocatorResponses;
    }


    /*
    Util method to get map of servers sorted based on count
     */
    private static Map<String, Integer> getSortedServers(Map<ServerTypes, Integer> regionalServerChoices) {

        //Create a list of return objects
        List<Map.Entry<ServerTypes, Integer> > list =
                new LinkedList<Map.Entry<ServerTypes, Integer>>(regionalServerChoices.entrySet());

        list.sort(new Comparator<Map.Entry<ServerTypes, Integer>>() {
            @Override
            public int compare(Map.Entry<ServerTypes, Integer> o1, Map.Entry<ServerTypes, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //Moving from a sorted list to Linked hash map tp preserve insertion order
        Map<String, Integer> sortedServers = new LinkedHashMap<>();
        for(Map.Entry<ServerTypes, Integer> entry:list) {
            sortedServers.put(entry.getKey().name().toLowerCase(),entry.getValue());
        }

        return sortedServers;
    }



}
