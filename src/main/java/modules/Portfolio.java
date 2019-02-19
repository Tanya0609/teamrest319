package modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Portfolio {
    private String id;
    private String deviation;
    private String type;
    private HashMap<String,Integer> allocations;

    public Portfolio(){
        //
    }

    public String getId(){
        return this.id;
    }

    public String getDeviation(){
        return this.deviation;
    }

    public String getType(){
        return this.type;
    }

    public HashMap<String, Integer> getAllocations(){
        return this.allocations;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setDeviation(String deviation){
        this.deviation = deviation;
    }

    public void setAllocations(HashMap<String, Integer> allocations) {
        this.allocations = allocations;
    }
}
