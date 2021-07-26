package core;

import lombok.*;

import java.util.ArrayList;

public class Station {

    @Getter
    private Line line;
    @Getter
    private String name;
    @Getter
    private ArrayList<Station> connections = new ArrayList<>();

    public Station(String name, Line line){
        this.line = line;
        this.name = name;
        line.getStations().add(this);
    }

    public static Station getStationWithoutAdd(String name, Line line){
        for(Station station : line.getStations()){
            if(station.getName().equals(name)) {
                return station;
            }
        }
        return new Station(name, line);
    }

    public static Station getStationToAdd(String name, Line line){
        for(Station station : line.getStations()){
            if(station.getName().equals(name)) {
                line.getStations().remove(station);
                return station;
            }
        }
        return new Station(name, line);
    }

    @Override
    public String toString(){
        return name;
    }
}
