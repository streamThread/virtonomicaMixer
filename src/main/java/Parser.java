import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Double.parseDouble;

@Log4j2
public class Parser {

    private List<TempString> stringList = new ArrayList<>();

    public Parser(File file) throws IOException {
        if (file.getName().endsWith(".html")) {
            parseHTML(file);
        } else if (file.getName().endsWith(".csv")) {
            parseCSV(file);
        } else throw new IllegalArgumentException("Wrong argument. Must be .html or .csv");
    }

    public List<TempString> getStringList() {
        return stringList;
    }

    private void parseHTML(File file) throws IOException {
        log.traceEntry("parse HTML: file = {}", file);
        Document document = Jsoup.parse(file, "UTF-8");
        Elements strings = document.select("tr.wborder");
        for (Element string : strings) {
            String price = string.selectFirst("td.price_w_tooltip:nth-child(6)")
                    .ownText().replaceAll("([$ ])", "");
            String quality = string.selectFirst("td.supply_data").text().trim();
            if (price.isBlank() || quality.isBlank()) {
                throw new IllegalArgumentException("The price or quality parameters can't be empty");
            }
            stringList.add(new TempString(parseDouble(price), parseDouble(quality)));
        }
        log.traceExit(stringList);
    }


    private void parseCSV(File file) throws IOException {
        log.traceEntry("parse CSV: file = {}", file);
        CSVReader reader = new CSVReaderBuilder(new BufferedReader(new FileReader(file)))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();
        Iterator<String[]> iter = reader.iterator();
        while (iter.hasNext()) {
            String[] tempLine = iter.next();
            Arrays.stream(tempLine).forEach(a -> a.replaceAll("([$ ])", ""));
            if (!(tempLine.length == 2)) {
                throw new IllegalArgumentException("The price or quality parameters can't be empty");
            }
            TempString tmpstr = new TempString(parseDouble(tempLine[0]), parseDouble(tempLine[1]));
            stringList.add(tmpstr);
        }
        log.traceExit(stringList);
    }
}
