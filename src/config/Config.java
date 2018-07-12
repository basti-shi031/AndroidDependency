package config;

import java.util.Arrays;
import java.util.List;

public class Config {
    //打包前修改为false
    public static final boolean DEBUG = true;

    //不需要遍历的文件夹
    private static final String PASS_FILE_RAW[] = {".git", "gradle", "src"};
    public static final List<String> PASS_FILE = Arrays.asList(PASS_FILE_RAW);

    //gradle文件下依赖关键字
//    https://blog.csdn.net/yuzhiqiang_1993/article/details/78366985?locationNum=6&fps=1
    private static final String DEPENDENCY_TAG_RAW[] = {"compile", "provided", "apk", "implementation", "api", "only",
            "androidtestimplementation", "debugimplementation", "androidtestcompile", "testcompile"};
    public static final List<String> DEPENDENCY_TAG = Arrays.asList(DEPENDENCY_TAG_RAW);

    //暂时不解决的项目
    private static final String UNSOLVED_PROJECT_RAW[] = {"D:\\starProject\\starproject1\\UnderwaterApps__fdse__overlap2d"};
    public static final List<String> UNSOLVED_PROJECT = Arrays.asList(UNSOLVED_PROJECT_RAW);
}
