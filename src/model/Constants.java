package model;

import java.util.ArrayList;
import java.util.List;

public class Constants {

     public static final String US_EAST = "us-east";
     public static final String US_WEST = "us-west";
     public static final String ASIA = "asia";

     public static final List<String> regionList = new ArrayList<>();

     static {
          regionList.add("us-east");
          regionList.add("us-west");
          regionList.add("asia");
     }
}
