package util;

import config.Config;

public class L {

    public static void l(String... content) {
        if (Config.DEBUG) {
            for (String s:content){
                System.out.print(s+" ");
            }
            System.out.println();
        }
    }

}
