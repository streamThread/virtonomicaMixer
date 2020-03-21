import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Main {

    static final String PATH = "src\\main\\resources\\Снабжение.html";

    public static void main(String[] args) {

        Parser parser = null;
        try {
            parser = new Parser(new File(PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] result = divide(15d, Objects.requireNonNull(parser).getStringList(), 400);
        System.out.printf("Первый товар: \n" +
                "Стоимость: "+ result[0][0]+"\n" +
                "Качество: "+result[0][1]+"\n" +
                "Необходимое количество: "+ result[0][2]+"\n" +
                "Второй товар:\n" +
                "Стоимость: "+ result[1][0]+"\n" +
                "Качество: "+result[1][1]+"\n" +
                "Необходимое количество: "+ result[1][2]+"\n");
        System.out.printf("Результирующая стоимость: %.2f",result[2][0]);
    }

    public static double[][] divide(Double resultQuality, List<TempString> stringList, int namesCount) {
        for (TempString str : stringList){
            if (str.getSecondColumn() == resultQuality){
                throw new IllegalArgumentException("Wrong argument in divide(). The required quality should not be present in the scanned list");
            }
        }
        double[][] goodPoint = new double[3][];
        double price = 0;
        Collections.sort(stringList);
        for (int i = 0; stringList.get(i).getSecondColumn() < resultQuality; i++) {
            for (int k = stringList.size() - 1; stringList.get(k).getSecondColumn() > resultQuality; k--) {
                double[] weights = equty(stringList.get(i).getSecondColumn(), stringList.get(k).getSecondColumn() , resultQuality, namesCount);
                double priceTemp = (stringList.get(i).getFirstColumn() * weights[0] + stringList.get(k).getFirstColumn() * weights[1]) / namesCount;
                if (price == 0) {
                    price = priceTemp;
                }
                if (priceTemp <= price) {
                    price = priceTemp;
                    goodPoint = new double[][]{new double[]{stringList.get(i).getFirstColumn(), stringList.get(i).getSecondColumn(), Math.round(weights[0])}
                    , new double[]{stringList.get(k).getFirstColumn(), stringList.get(k).getSecondColumn(), Math.round(weights[1])}, new double[]{price}};
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
