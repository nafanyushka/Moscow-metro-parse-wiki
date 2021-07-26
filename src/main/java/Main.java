import core.Line;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class Main{

    private static final String PATH_TO_JSON = "json/moscow_metro.json";

    public static void main(String[] args) throws IOException, ParseException {
        File jsonFile = new File(PATH_TO_JSON);
        if(!jsonFile.exists()) {
            WikiParser.parseWiki();
            String json = JsonUtils.toJson();
            jsonFile.createNewFile();
            Files.write(jsonFile.toPath(), Collections.singleton(json));
        }
        else{
            JsonUtils.loadJson(Paths.get(PATH_TO_JSON));
        }
        Line.print();
    }


}
