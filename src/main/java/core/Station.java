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

    @Override
    public String toString(){
        return name;
    }
}
