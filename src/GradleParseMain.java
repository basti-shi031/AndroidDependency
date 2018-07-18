import bean.*;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import config.Config;
import util.DBUtil;
import util.FileUtil;
import util.L;
import util.ParseUtil;

import java.io.File;
import java.util.*;

public class GradleParseMain {

    public static void main(String args[]) {
        String baseStarProjectPath = args[0];
        //1-index文件夹内的文件都已经全部解析完
        DBUtil.init();
        final int MAX = 83;
        int index = getCurrentIndex();
        Project project;
        for (int i = index + 1; i < MAX; i++) {
            String log = "";
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
                String fileName = projectFile.getName();
                String company = fileName.split("__fdse__")[0];
                String projectName = fileName.split("__fdse__")[1];
                ProjectInfo projectInfo = DBUtil.getProjectInfo(company + "/" + projectName);
                if (projectInfo == null) {
                    continue;
                }
                L.l(projectFile.getAbsolutePath());
                if (projectFile.getAbsolutePath().contains("D:\\starproject\\starproject1\\airbnb__fdse__epoxy")) {
                    int a = 1;
                }
                String test = "D:\\starProject\\starproject1\\airbnb__fdse__epoxy";
                String path111 = projectFile.getAbsolutePath();
                if (Config.UNSOLVED_PROJECT.contains(path111)) {
                    continue;
                }
                List<GradleFile> gradleFileList = FileUtil.getAllGradleFiles(projectFile.getAbsolutePath());
                if (gradleFileList == null || gradleFileList.size() <= 1) {
                    continue;
                }
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

                log = log + company + "  " + projectName + "\n" + projectFile.getAbsolutePath() + "\n";
                L.l("============================");
                Set<String> dependenciesSet = new HashSet<>(dependencies);
                project = new Project();
                project.setProjectInfo(projectInfo);
                DependencyProject dependencyProject;
                DependencyLib dependencyLib;
                DependencyUrl dependencyUrl;
                for (String s : dependenciesSet) {
                    if (s == null){
                        continue;
                    }
                    L.l(s);
                    log = log + s + "\n";
                    if (s.startsWith("project(")) {
                        //compile project(xxxxx)
                        dependencyProject = new DependencyProject(s);
                        project.getDependencyProjects().add(dependencyProject);
                    } else if (s.startsWith("files")) {
                        //compile libs
                        dependencyLib = new DependencyLib(s);
                        project.getDependencyLibs().add(dependencyLib);
                    } else {
                        //uk.co.chrisjenx:calligraphy:2.2.0
                        if (s.contains(";")) {
                            String[] tempDependencies = s.split(":");
                            if (tempDependencies.length >= 3) {
                                String group = tempDependencies[0];
                                String artifactId = tempDependencies[1];
                                String version = tempDependencies[2];
                                dependencyUrl = new DependencyUrl(group, artifactId, version);
                                project.getDependencyUrls().add(dependencyUrl);
                            }
                        }
                    }
                }
                log = log + "\n" + "\n" + "\n";
                L.l("============================");
                String newFilePath = "D://gradleProject//starproject" + String.valueOf(i) + "//";
                if (!new File(newFilePath).exists()) {
                    new File(newFilePath).mkdirs();
                }
                FileUtil.writeFlie("D://gradleProject//starproject" + String.valueOf(i) + "//" + projectFile.getName()+".txt", new Gson().toJson(project));
                ParseUtil.clearDependencies();
            }

            L.l(log);
            FileUtil.writeFlie("project" + String.valueOf(i) + ".txt", log);
            String path = System.getProperty("user.dir");
            FileUtil.writeFlie(path + "/src/index.json", new Gson().toJson(new Index(i)));

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
