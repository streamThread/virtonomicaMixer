import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Parser {

    private List<TempString> stringList = new ArrayList<>();

    public Parser(File file) throws IOException, IllegalArgumentException {
        if (file.getName().endsWith(".html")) {
            stringList = parseHTML(file);
        } else if (file.getName().endsWith(".csv")) {
            stringList = parseCSV(file);
        } else throw new IllegalArgumentException("Wrong argument. Must be .html or .csv");
    }

    public List<TempString> getStringList() {
        return stringList;
    }

    private List<TempString> parseHTML(File file) throws IOException {
        List<TempString> array = new ArrayList<>();
        Document document = Jsoup.parse(file, "UTF-8");
        Elements strings = document.select("tr.wborder");
        for (Element string : strings) {
            String price = string.selectFirst("td.price_w_tooltip:nth-child(6)")
                    .ownText().replaceAll("(\\$| )", "");
            String quality = string.selectFirst("td.supply_data").text().trim();
            array.add(new TempString(Double.parseDouble(price), Double.parseDouble(quality)));
        }
        return array;
    }


    private List<TempString> parseCSV(File file) throws IOException {
        CSVReader reader = new CSVReaderBuilder(new BufferedReader(new FileReader(file)))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();
        Iterator<String[]> iter = reader.iterator();
        List<TempString> tmpList = new ArrayList<>();
        while (iter.hasNext()) {
            String[] tempLine = iter.next();
            Arrays.stream(tempLine).forEach(a -> a.replaceAll("(\\$| )", ""));
            TempString tmpstr = new TempString(Double.parseDouble(tempLine[0]), Double.parseDouble(tempLine[1]));
            tmpList.add(tmpstr);
        }
        return tmpList;
    }
}
