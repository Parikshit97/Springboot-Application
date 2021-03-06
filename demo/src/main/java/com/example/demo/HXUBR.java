package com.example.demo;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HXUBR implements Feature {
    @Override
    public HashMap<String, HashMap<String, Double>> calculate(ArrayList<Hotels> hotels, String userprofileid){
        HashMap<String, HashMap<String, Double>> outer = new HashMap<>();

        ElasticConfig elasticConfig = new ElasticConfig();
        JestClient jestClient = elasticConfig.jestClient();

        try {

            JestResult jestResult = jestClient.execute(new Get.Builder("db_ranking", "AmIwfnEBmqnhJvawM6FZ").build());
            HXU hxu = jestResult.getSourceAsObject(HXU.class);


            for(Hotels hotel : hotels) {
                HashMap<String, Double> inner = new HashMap<>();

                if(hxu.getHxu_map()==null || hxu.getHxu_map().get(hotel.getHotel_id())==null || hxu.getHxu_map().get(hotel.getHotel_id())==null || hxu.getHxu_map().get(hotel.getHotel_id()).get(userprofileid)==null){
                    continue;
                }

                long details_bookings = hxu.getHxu_map().get(hotel.getHotel_id()).get(userprofileid).bookings;
                long details_impressions = hxu.getHxu_map().get(hotel.getHotel_id()).get(userprofileid).impressions;
                if(details_impressions == 0){
                    continue;
                }
                double hotel_user_br = details_bookings / (1.0 * details_impressions);


                inner.put("hotel_user_br", hotel_user_br);
                outer.put(hotel.getHotel_id(), inner);
            }

;
        }catch (IOException e){
            System.out.println(e.getStackTrace());
        }

        return outer;
    }

    @Override
    public String toString() {
        return "hotel_user_br";
    }
}
