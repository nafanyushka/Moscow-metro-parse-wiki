import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WikiParser {

    private static final String PATH_TO_WIKI = "data/wiki.html";
    private static final String[] LINE_NUMBERS = {"1", "2"};

    public static void parseWiki(){

        Document doc = Jsoup.parse(wikiToString(PATH_TO_WIKI));
        Element help = doc.select("table.standard.sortable").first();
        assert help != null;

        //----------------->

        Elements trs = help.select("tbody > tr");
        trs.remove(0);
        ArrayList<String> from = new ArrayList<>();
        ArrayList<ArrayList<String>> to = new ArrayList<>();
        ArrayList<Station> stations = new ArrayList<>();
        for(Element element : trs){
            Line line = Line.getLine(element.selectFirst("tr > td:nth-child(1) > span:nth-child(1)").text(),
                    element.selectFirst("tr > td:nth-child(1) > span:nth-child(2)[title]").attr("title"));
            Station station = new Station(element.selectFirst("tr > td:nth-child(2)").text(), line);
            stations.add(station);
            Element value = element.selectFirst("tr > td:nth-child(4)");
            ArrayList<String> tos = new ArrayList<>();
            if(!value.attr("data-sort-value").equals("Infinity")) {
                value.select("span[title]").forEach(e -> tos.add(e.selectFirst("a").attr("href")));
            }
            from.add(element.selectFirst("tr > td:nth-child(2) > a, tr > td:nth-child(2) > span > a").attr("href"));
            to.add(tos);
        }
        AddConnections(from, to, stations);
    }

    private static void AddConnections(ArrayList<String> from,
                                       ArrayList<ArrayList<String>> to,
                                       ArrayList<Station> stations){
        for(int i = 0; i < from.size(); i++){
               Station curStation = stations.get(i);
               for(String href : to.get(i)){
                   int index = from.indexOf(href);
                   if(index >= 0){
                       curStation.getConnections().add(stations.get(index));
                   }
               }
//               to.get(i).forEach(href -> {
//                   int index = from.indexOf(href);
//                   if(index >= 0){
//                       curStation.getConnections().add(stations.get(index));
//                   }
//               });
        }
    }

    private static String wikiToString(String path){
        StringBuilder builder = new StringBuilder();
        try{
            List<String> strings = Files.readAllLines(Paths.get(path));
            strings.forEach(s -> builder.append(s + "\n"));
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return builder.toString();
    }
}
