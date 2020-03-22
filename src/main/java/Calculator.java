import lombok.extern.log4j.Log4j2;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Log4j2
public class Calculator {

    private Calculator() {
    }

    public static ResultOfDividePoint divide(Double resultQuality, List<TempString> stringList, int namesCount) {
        log.traceEntry(String.valueOf(resultQuality), stringList, namesCount);
        for (TempString str : stringList) {
            if (Objects.equals(str.getQuality(), resultQuality)) {
                throw new IllegalArgumentException("Wrong argument in divide(). The required quality should not be present in the scanned list");
            }
        }
        ResultOfDividePoint resultOfDividePoint = new ResultOfDividePoint();
        double price = 0;
        stringList.sort(Comparator.comparing(TempString::getQuality));
        for (int i = 0; stringList.get(i).getQuality() < resultQuality; i++) {
            for (int k = stringList.size() - 1; stringList.get(k).getQuality() > resultQuality; k--) {
                double[] weights = equty(stringList.get(i).getQuality(), stringList.get(k).getQuality(), resultQuality, namesCount);
                double priceTemp = (stringList.get(i).getPrice() * weights[0] + stringList.get(k).getPrice() * weights[1]) / namesCount;
                if (price == 0) {
                    price = priceTemp;
                }
                if (priceTemp <= price) {
                    price = priceTemp;
                    resultOfDividePoint.setCostOfTheFirstProduct(stringList.get(i).getPrice());
                    resultOfDividePoint.setQualityOfTheFirstProduct(stringList.get(i).getQuality());
                    resultOfDividePoint.setRequiredQuantityOfTheFirstProduct(Math.round(weights[0]));
                    resultOfDividePoint.setCostOfTheSecondProduct(stringList.get(k).getPrice());
                    resultOfDividePoint.setQualityOfTheSecondProduct(stringList.get(k).getQuality());
                    resultOfDividePoint.setRequiredQuantityOfTheSecondProduct(Math.round(weights[1]));
                    resultOfDividePoint.setResultingCombinedCost(price);
                }
            }
        }
        return log.traceExit(resultOfDividePoint);
    }

    private static double[] equty(Double quality, Double qualitySecond, Double resultQuality, int namesCount) {
        log.traceEntry("equty: ", quality, qualitySecond, resultQuality, namesCount);
        if (!(quality < resultQuality) || !(resultQuality < qualitySecond)) {
            throw new IllegalArgumentException("Wrong arguments");
        }
        double qualitySecondCount = (resultQuality * namesCount - namesCount * quality) / (qualitySecond - quality);
        double qualityCount = namesCount - qualitySecondCount;
        return log.traceExit(new double[]{qualityCount, qualitySecondCount});
    }
}

