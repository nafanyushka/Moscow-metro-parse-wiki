package core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class Line {

    private static final HashMap<String, Line> lines = new HashMap<>();
    @Getter
    private final ArrayList<Station> stations;
    @Getter
    private final String number;
    @Getter
    private String name;

    private Line(String number){
        this.number = number;
        stations = new ArrayList<>();
        lines.put(number, this);
    }

    public Line(String number, String name){
        this(number);
        this.name = name;
    }

    public static Line getLine(String number){
        if(!lines.containsKey(number))
            return new Line(number);
        return lines.get(number);
    }

    public static Line getLine(String number, String name){
        if(!lines.containsKey(number))
            return new Line(number, name);
        return lines.get(number);
    }

    public static ArrayList<Line> getLines(){
        ArrayList<Line> alines = new ArrayList<>();
        lines.forEach((k, v) ->alines.add(v));
        return alines;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Линия номер: ").append(number).append(". Название: ").append(name).append(".\n");
        stations.forEach(station -> {
            builder.append("\t").append(station.toString()).append(".\n");
        });
        return builder.toString();
    }

    public static void print(){
        lines.forEach((str, line) -> {
            System.out.println(line.toString());
        });
    }
}
