import com.google.gson.Gson;
import model.AllocatorResponse;
import service.CpuAllocatorService;

import java.util.Collections;
import java.util.List;

public class Application {

    public static List<AllocatorResponse> get_costs(int hours, int cpus, double price) {
        if(hours != 0 && cpus !=0 && price == 0 ) {
            return CpuAllocatorService.getCpus(cpus,hours);
        } else if(price != 0 && hours !=0 && cpus == 0) {
            return CpuAllocatorService.getCpusByCostAndHours(price,hours);
        } else if(price != 0 && hours !=0 && cpus != 0){
            return CpuAllocatorService.getCpusByNumbersHoursAndCost(cpus,hours,price);
        }
        return null;
    }


    public static void main(String[] args) {

        List<AllocatorResponse> cpus = get_costs(1,1,0.12);

        Collections.sort(cpus);
        //I need to print them in JSON format
        for(AllocatorResponse response: cpus) {
            System.out.println(new Gson().toJson(response));
        }

    }

}
