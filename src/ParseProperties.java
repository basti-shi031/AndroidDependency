import util.L;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ParseProperties {

    public static void main(String args[]) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("D:\\starProject\\starproject1\\AAkira__fdse__ExpandableLayout\\gradle.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        L.l(properties.getProperty("BUILD_TOOLS_VERSION"));
    }

}
