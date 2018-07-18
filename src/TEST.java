import com.google.gson.Gson;
import util.L;

import java.util.ArrayList;
import java.util.List;

public class TEST {

    public static void main(String args[]) {

        List<String> a= new ArrayList<>();
        a.add(null);
        a.add("");
        L.l(new Gson().toJson(a));
    }
}
