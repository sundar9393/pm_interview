package model;

import java.util.ArrayList;
import java.util.List;

public class Constants {

     public static final String US_EAST = "us-east";
     public static final String US_WEST = "us-west";
     public static final String ASIA = "asia";

     public static final List<String> REGION_LIST = new ArrayList<>();

     static {
          REGION_LIST.add("us-east");
          REGION_LIST.add("us-west");
          REGION_LIST.add("asia");
     }
}
