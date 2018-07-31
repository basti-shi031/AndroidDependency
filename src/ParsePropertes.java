import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class ParsePropertes {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("D:\\cs\\projects_last\\gradle200_500\\home\\fdse\\data\\prior_repository\\cfg4j\\cfg4j\\classes\\test\\cfg4j-core\\malformed.properties"));
            Iterator it = properties.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
