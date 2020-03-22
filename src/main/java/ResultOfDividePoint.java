import lombok.Data;

import java.util.Locale;

@Data
public class ResultOfDividePoint {
    private double costOfTheFirstProduct;
    private double qualityOfTheFirstProduct;
    private double requiredQuantityOfTheFirstProduct;
    private double costOfTheSecondProduct;
    private double qualityOfTheSecondProduct;
    private double requiredQuantityOfTheSecondProduct;
    private double resultingCombinedCost;

    @Override
    public String toString() {
        return String.format(Locale.US, "Первый товар: \n" +
                "Стоимость: " + costOfTheFirstProduct + "\n" +
                "Качество: " + qualityOfTheFirstProduct + "\n" +
                "Необходимое количество: " + requiredQuantityOfTheFirstProduct + " шт.\n" +
                "Второй товар:\n" +
                "Стоимость: " + costOfTheSecondProduct + "\n" +
                "Качество: " + qualityOfTheSecondProduct + "\n" +
                "Необходимое количество: " + requiredQuantityOfTheSecondProduct + " шт.\n" +
                "Результирующая стоимость: %.2f", resultingCombinedCost);
    }
}
