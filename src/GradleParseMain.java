import bean.GradleFile;
import com.google.gson.JsonParser;
import util.FileUtil;
import util.L;
import util.ParseUtil;

import java.io.File;
import java.util.*;

public class GradleParseMain {

    public static void main(String args[]) {

        String baseStarProjectPath = args[0];
        //1-index文件夹内的文件都已经全部解析完
        final int MAX = 3;
        int index = getCurrentIndex();

        for (int i = index + 1; i < MAX; i++) {
            String projectPath = baseStarProjectPath + "//starproject" + String.valueOf(i);
            File[] tempFiles = new File(projectPath).listFiles();
            List<File> projectFiles = new ArrayList<>();
            //筛去非dir类型的文件
            for (File projectFile : tempFiles) {
                if (projectFile.isDirectory()) {
                    projectFiles.add(projectFile);
                }
            }
            //根据文件名排序
            Collections.sort(projectFiles, new FileComparator());
            for (File projectFile : projectFiles) {
                //projectFile指的每一个项目文件夹
                List<GradleFile> gradleFileList = FileUtil.getAllGradleFiles(projectFile.getAbsolutePath());
                if (gradleFileList == null || gradleFileList.size() <= 1) {
                    continue;
                }
                int size = gradleFileList.size();
                for (int gradleFileIndex = 0; gradleFileIndex < size; gradleFileIndex++) {
                    ParseUtil.parse(gradleFileList.get(gradleFileIndex).getContent());
                }
                List<String> dependencies = ParseUtil.getDependencies();
                String fileName = projectFile.getName();
                String company = fileName.split("__fdse__")[0];
                String projectName = fileName.split("__fdse__")[1];

                L.l(company, projectName);
                L.l("============================");
                Set<String> dependenciesSet = new HashSet<>(dependencies);
                for (String s: dependenciesSet){
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
