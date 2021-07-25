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
                    o.put("line", s.getLine().getNumber());
                    o.put("name", s.getName());
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
            stationsArray.forEach(stationObject -> {
                Station station = new Station((String) stationObject, line);
            });
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
