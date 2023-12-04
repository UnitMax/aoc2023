package unitmax;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class Util {
    public static String readResource(String filename) throws IOException {
        InputStream is = Util.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    public static String[] readResourceLines(String filename) throws IOException {
        return readResource(filename).split("\n");
    }
}
