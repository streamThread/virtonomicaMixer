import lombok.Value;

@Value
public class TempString implements Comparable<TempString> {

    Double firstColumn;
    Double secondColumn;

    @Override
    public int compareTo(TempString o) {
        return secondColumn.compareTo(o.secondColumn);
    }
}
