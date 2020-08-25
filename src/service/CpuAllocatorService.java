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

            List<Map.Entry<ServerTypes, Integer> > list =
                    new LinkedList<Map.Entry<ServerTypes, Integer>>(value.entrySet());

            list.sort(new Comparator<Map.Entry<ServerTypes, Integer>>() {
                @Override
                public int compare(Map.Entry<ServerTypes, Integer> o1, Map.Entry<ServerTypes, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });


            Map<String, Integer> servers = new LinkedHashMap<>();
            for(Map.Entry<ServerTypes, Integer> entry:list) {
                servers.put(entry.getKey().name().toLowerCase(),entry.getValue());
            }
            allocatorResponse.setServers(servers);

            allocatorResponses.add(allocatorResponse);

        });

        return allocatorResponses;
    }

    /*
    This method returns a Map of all chosen server types for all regions
    to cater to certain number of CPUs
     */
    private static Map<String, Map<ServerTypes, Integer>> getServers(int cpuCount) {

        //need to write an algorithm to return min number of CPUS(Greedy Approach)

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

}
