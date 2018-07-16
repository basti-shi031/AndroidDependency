import util.L;

public class main {
    public static void main(String args[]) {
     /*   //获得所有gradle文件
        List<GradleFile> files = findGradleFilePath("C:\\cs\\starProject\\apache");
        //根据得到的路径获取其内容
        readBuildContent(files);
        //解析内容
        resolveBuildGradle(files);*/
     String a = "abc";
        L.l(String.valueOf(a.split("b").length));
        L.l(String.valueOf(a.split("a").length));
        L.l(String.valueOf(a.split("c").length));
        L.l(String.valueOf(a.split("d").length));
    }

}
