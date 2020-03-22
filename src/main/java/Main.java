import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Log4j2
public class Main {

    static final String PATH = "src/main/resources/Снабжение.html";

    public static void main(String[] args) {
        try {
            Parser parser = new Parser(new File(PATH));

            System.out.println(Calculator.divide(15d, Objects.requireNonNull(parser).getStringList(), 400));

        } catch (IOException | RuntimeException e) {
            log.error(e);
        }
    }
}


