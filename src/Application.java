import com.google.gson.Gson;
import model.AllocatorResponse;
import service.CpuAllocatorService;

import java.util.List;

public class Application {


    public static void main(String[] args) {

        List<AllocatorResponse> cpus = CpuAllocatorService.getCpus(100, 20);


        //I need to print them in JSON format 
        for(AllocatorResponse response: cpus) {
            System.out.println(response);
        }

    }

}
