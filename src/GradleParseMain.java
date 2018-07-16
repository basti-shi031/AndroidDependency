import bean.GradleFile;
import com.google.gson.JsonParser;
import config.Config;
import util.FileUtil;
import util.L;
import util.ParseUtil;

import java.io.File;
import java.util.*;

public class GradleParseMain {

    public static void main(String args[]) {

        String baseStarProjectPath = args[0];
        //1-index文件夹内的文件都已经全部解析完
        final int MAX = 2;
        int index = getCurrentIndex();

        for (int i = index + 1; i < MAX; i++) {
            String projectPath = baseStarProjectPath + "//starproject" + String.valueOf(i);
            File[] tempFiles = new File(projectPath).listFiles();
            List<File> projectFiles = new ArrayList<>();
            for (String s : Config.UNSOLVED_PROJECT) {
                L.l(s);
            }
            //筛去非dir类型的文件
            for (File projectFile : tempFiles) {
                if (projectFile.isDirectory()) {
                    projectFiles.add(projectFile);
                }
            }
            //根据文件名排序
            Collections.sort(projectFiles, new FileComparator());
            L.l(String.valueOf(projectFiles.size()));
            for (File projectFile : projectFiles) {
                //projectFile指的每一个项目文件夹
                L.l(projectFile.getAbsolutePath());
                if (projectFile.getAbsolutePath().contains("D:\\starproject\\starproject1\\airbnb__fdse__epoxy"))
                {
                    int a = 1;
                }
                String test = "D:\\starProject\\starproject1\\airbnb__fdse__epoxy";
                String path111 = projectFile.getAbsolutePath();
                if (Config.UNSOLVED_PROJECT.contains(test)){
                    int b = 1;
                }
                boolean equal = path111.equals(test);
                if (Config.UNSOLVED_PROJECT.contains(path111)) {
                    continue;
                }
                List<GradleFile> gradleFileList = FileUtil.getAllGradleFiles(projectFile.getAbsolutePath());
                if (gradleFileList == null || gradleFileList.size() <= 1) {
                    continue;
                }
                String fileName = projectFile.getName();
                String company = fileName.split("__fdse__")[0];
                String projectName = fileName.split("__fdse__")[1];

                L.l(company, projectName);
                int size = gradleFileList.size();
                for (int gradleFileIndex = 0; gradleFileIndex < size; gradleFileIndex++) {
                    GradleFile gradleFile = gradleFileList.get(gradleFileIndex);
                    if (gradleFile.getType().equals("build.gradle")) {
                        ParseUtil.parse(gradleFile.getContent(), gradleFile.getPath());
                    } else if (gradleFile.getType().endsWith("properties")) {
                        ParseUtil.parseProperties(gradleFile.getPath());
                    }
                }
                ParseUtil.replaceDependencyValue();
                ParseUtil.replaceDependencyValue();
                ParseUtil.clearDependencyValue();
                List<String> dependencies = ParseUtil.getDependencies();

                L.l("============================");
                Set<String> dependenciesSet = new HashSet<>(dependencies);
                for (String s : dependenciesSet) {
                    L.l(s);
                }
                L.l("============================");
                ParseUtil.clearDependencies();
            }
        }

    }

    /**
     * 获取当前已经解析完的index文件夹
     *
     * @return
     */
    private static int getCurrentIndex() {
        String path = System.getProperty("user.dir");
        String indexStr = FileUtil.read(path + "/src/index.json");
        return new JsonParser().parse(indexStr).getAsJsonObject().get("index").getAsInt();
    }

    static class FileComparator implements Comparator<File> {

        @Override
        public int compare(File o1, File o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
