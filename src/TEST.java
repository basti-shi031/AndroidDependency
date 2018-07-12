import util.L;

public class TEST {

    public static void main(String args[]) {
        String a = "[group:io.dropwizard, name:dropwizard-jdbi, version:1.0.0-rc2]";
        a = a.substring(1, a.length() - 1);
        L.l(a);
    }
}
