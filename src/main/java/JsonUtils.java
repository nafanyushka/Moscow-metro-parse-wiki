import core.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static String toJson(){
        ArrayList<Line> lines = Line.getLines();
        JSONObject file = new JSONObject();
        JSONObject jsonStations = new JSONObject();
        JSONArray jsonLines = new JSONArray();
        lines.forEach(line -> {
            String numberLine = line.getNumber();
            JSONArray arrayStations = new JSONArray();
            for(Station station : line.getStations()){
                JSONObject stationObject = new JSONObject();
                stationObject.put("name", station.getName());
                JSONArray connectionArray = new JSONArray();
                station.getConnections().forEach(s -> {
                    JSONObject o = new JSONObject();
                    o.put(s.getLine().getNumber(), s.getName());
                    connectionArray.add(o);
                });
                stationObject.put("connections", connectionArray);
//                arrayStations.add(station.getName());
                arrayStations.add(stationObject);
            }
            jsonStations.put(numberLine, arrayStations);
            JSONObject lineAttr = new JSONObject();
            lineAttr.put("name", line.getName());
            lineAttr.put("number", line.getNumber());
            jsonLines.add(lineAttr);
        });
        file.put("stations", jsonStations);
        file.put("lines", jsonLines);
        return file.toJSONString();
    }

    public static void loadJson(Path filePath) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonData = (JSONObject) parser.parse(getJsonFile(filePath));

        JSONArray lines = (JSONArray) jsonData.get("lines");
        parseLines(lines);

        JSONObject stations = (JSONObject) jsonData.get("stations");
        parseStations(stations);

    }

    private static void parseLines(JSONArray lines){
        lines.forEach(lineObject -> {
            JSONObject lineJsonObject = (JSONObject) lineObject;
            Line.getLine((String) lineJsonObject.get("number"), (String) lineJsonObject.get("name"));
        });
    }

    private static void parseStations(JSONObject stations){
        stations.keySet().forEach(lineNumberObject ->{
            String lineNumber = (String) lineNumberObject;
            Line line = Line.getLine(lineNumber);
            JSONArray stationsArray = (JSONArray) stations.get(lineNumberObject);
            for (Object stationObject : stationsArray){
                JSONObject stationObjectJson = (JSONObject) stationObject;
                JSONArray stationArray = (JSONArray) stationObjectJson.get("connections");
                Station station = Station.getStationToAdd((String) stationObjectJson.get("name"), line);
                if(!station.getLine().getStations().contains(station))
                    station.getLine().getStations().add(station);
                stationArray.forEach(connection -> {
                    JSONObject connectionJson = (JSONObject) connection;
                    connectionJson.keySet().forEach(key -> {
                        String lineNum = (String) key;
                        String stationName = (String) connectionJson.get(lineNum);
                        station.getConnections().add(Station.getStationWithoutAdd(stationName, Line.getLine(lineNum)));
                    });
                });
            }
//            stationsArray.forEach(stationObject -> {
//                JSONObject stationObjectJson = (JSONObject) stationObject;
//                JSONArray stationArray = (JSONArray) stationObjectJson.get("connections");
//                stationArray.forEach(connection -> {
//                    JSONObject connectionJson = (JSONObject) connection;
//                    connectionJson.keySet().forEach(key -> {
//                        String lineNum = (String) key;
//                        String stationName = (String) connectionJson.get(lineNum);
//                        Station.getStationToAdd(stationName, Line.getLine(lineNum));
//                    });
//                });
//                Station station = Station.getStationToAdd((String) stationObjectJson.get("name"), line);
//                station.getConnections().add(i, station);
//                i += 1;
//            });
        });
    }

    private static String getJsonFile(Path filePath){
        StringBuilder builder = new StringBuilder();
        try{
            List<String> list = Files.readAllLines(filePath);
            list.forEach(builder::append);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return builder.toString();
    }
}
