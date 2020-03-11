import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Parser parser = new Parser(new File("src\\main\\resources\\Снабжение.html"));

        Arrays.stream(divide(13d, parser.getStringList(), 15))
                .forEach(a -> Arrays.stream(a).forEach(System.out::println));
    }

    public static double[][] divide(Double resultQuality, List<TempString> stringList, int namesCount) {
        List<TempString> moreThanDelimeter = new ArrayList<>();
        List<TempString> lessThanDelimeter = new ArrayList<>();
        for (TempString temp : stringList) {
            if (temp.getString()[1] > resultQuality) {
                moreThanDelimeter.add(temp);
            } else if (temp.getString()[1] < resultQuality) {
                lessThanDelimeter.add(temp);
            }
        }
        double[][] goodPoint = new double[3][];
        double price = 0;
        for (TempString strLess : lessThanDelimeter) {
            for (TempString strMore : moreThanDelimeter) {
                double[] weights = equty(strLess.getString()[1], strMore.getString()[1], resultQuality, namesCount);
                double priceTemp = (strLess.getString()[0] * weights[0] + strMore.getString()[0] * weights[1]) / namesCount;
                if (price == 0) {
                    price = priceTemp;
                }
                if (priceTemp <= price) {
                    price = priceTemp;
                    goodPoint = new double[][]{new double[]{strLess.getString()[0],strLess.getString()[1], weights[0]}
                            , new double[]{strMore.getString()[0],strMore.getString()[1], weights[1]}, new double[]{price}};
                }
            }
        }
        return goodPoint;
    }

    public static double[] equty(Double quality, Double qualitySecond, Double resultQuality, int namesCount) {
        if (!(quality < resultQuality) || !(resultQuality < qualitySecond)) {
            throw new IllegalArgumentException("Wrong arguments");
        }
        double qualitySecondCount = (resultQuality * namesCount - namesCount * quality) / (qualitySecond - quality);
        double qualityCount = namesCount - qualitySecondCount;
        return new double[]{qualityCount, qualitySecondCount};
    }
}
