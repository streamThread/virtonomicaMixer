import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parser {

    private List<TempString> stringList = new ArrayList<>();

    public Parser() {
        try {
            stringList = parseCSV();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Parser(File file) {
        try {
            stringList = parseHTML(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<TempString> getStringList() {
        return stringList;
    }

    private List<TempString> parseHTML(File file) throws IOException {
        List<TempString> array = new ArrayList<>();
        Document document = Jsoup.parse(file, "UTF-8");
//        Document document = Jsoup.connect()
        Elements strings = document.select("tr.wborder");
        Iterator<Element> iter = strings.iterator();
        while (iter.hasNext()) {
            Element string = iter.next();
            String price = string.select("td.price_w_tooltip:nth-child(6)")
                    .iterator().next().ownText().replaceAll("(\\$| )","");
            String quality = string.selectFirst("td.supply_data").text().trim();
            array.add(new TempString(Double.parseDouble(price),Double.parseDouble(quality)));
        }
        return array;
    }


    private List<TempString> parseCSV() throws IOException, CsvException {
        CSVReader reader = new CSVReaderBuilder(new BufferedReader(new FileReader("src\\main\\resources\\virt.csv")))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();
        Iterator<String[]> iter = reader.iterator();
        List<TempString> tmpList = new ArrayList<>();
        while (iter.hasNext()) {
            String[] tempLine = iter.next();
            TempString tmpstr = new TempString(Double.parseDouble(tempLine[0]), Double.parseDouble(tempLine[1]));
            tmpList.add(tmpstr);
        }
        return tmpList;
    }
}
