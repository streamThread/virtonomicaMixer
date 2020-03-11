public class TempString {

    private double[] string;

    TempString(double firstColumn, double secondColumn) {
        string = new double[]{firstColumn, secondColumn};
    }

    public double[] getString() {
        return string;
    }
}
