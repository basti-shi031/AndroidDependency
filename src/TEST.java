import util.L;

public class TEST {

    public static void main(String args[]) {
        String a = "localGroovy()";
        String b = "^[a-zA-z0-9]+\\(\\)$";
        L.l(String.valueOf(a.matches(b)));
    }
}
