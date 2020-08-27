package test;

import model.AllocatorResponse;
import static org.junit.Assert.*;
import org.junit.Test;
import service.CpuAllocatorService;

import java.util.List;
/*
Unit test cases for CpuAllocatorService apis
Coverage Achieved = 100%
 */

public class CpuAllocatorServiceTest {

    CpuAllocatorService cpuAllocatorService = new CpuAllocatorService();

    @Test
    public void getCpusByCountForHours_Test() {

        List<AllocatorResponse> cpusByCountForHours = cpuAllocatorService.getCpusByCountForHours(115, 24);

        assertNotNull(cpusByCountForHours);
        assertEquals(cpusByCountForHours.size() ,3);
        assertTrue(cpusByCountForHours.get(0).getTotal_cost() <= cpusByCountForHours.get(1).getTotal_cost());
        assertTrue(cpusByCountForHours.get(1).getTotal_cost() <= cpusByCountForHours.get(2).getTotal_cost());

    }

    @Test
    public void getCpusByCostAndHours_Test() {

        List<AllocatorResponse> cpusByCostAndHours = cpuAllocatorService.getCpusByCostAndHours(115, 24);

        assertNotNull(cpusByCostAndHours);
        assertEquals(cpusByCostAndHours.size(), 3);
    }


    @Test
    public void getCpusByNumbersHoursAndCost_Test() {

        List<AllocatorResponse> cpusByNumbersHoursAndCost = cpuAllocatorService.getCpusByNumbersHoursAndCost(1, 1, 0.12);

        assertNotNull(cpusByNumbersHoursAndCost);
        assertEquals(cpusByNumbersHoursAndCost.size(), 2);
        assertTrue(cpusByNumbersHoursAndCost.get(0).getTotal_cost() <= cpusByNumbersHoursAndCost.get(1).getTotal_cost());
    }


}
