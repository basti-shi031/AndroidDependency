import bean.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import util.DBUtil;
import util.FileUtil;
import util.L;
import util.ParseUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainApplication {
    public static void main(String[] args) {

        int index = 3;

        String[] sourceTxts = {"gradle500.txt", "gradle200_500.txt", "maven_gradle500.txt", "maven_gradle200_500.txt"};
        String[] outputs = {"output/gradle500/", "output/gradle200_500/", "output/maven_gradle500/", "output/maven_gradle200_500/"};
        String[] logOuts = {"log/gradle500/", "log/gradle200_500/", "log/maven_gradle500/", "log/maven_gradle200_500/"};
        DBUtil.init();
        List<String> projectFiles = getProjectPathList(sourceTxts[index]);
        boolean checked = true;
        for (String project : projectFiles) {
       /*     if (project.contains("ratpack__fdse__ratpack")) {
                checked = false;
            }
            if (!checked) {*/
                parseProject(project, outputs[index], logOuts[index]);
       //     }
        }


    }

    /**
     * parse project Dependency
     */
    private static void parseProject(String projectPath, String outputFileDir, String logFileDir) {
        L.l("=====================");
        String log = "";
        String[] name = getCompanyAndProjectName(projectPath);
        String companyName = name[0];
        String projectName = name[1];
        String outputFileName = outputFileDir + companyName + "__fdse__" + projectName + ".txt";
        String logFileName = logFileDir + companyName + "__fdse__" + projectName + ".txt";
        ProjectInfo projectInfo = DBUtil.getProjectInfo(companyName + "/" + projectName);
        L.l(companyName, projectName);
        List<GradleFile> gradleFileList = FileUtil.getAllGradleFiles(projectPath);
        if (gradleFileList == null || gradleFileList.size() <= 1) {
            return;
        }
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

        Set<String> dependenciesSet = new HashSet<>(dependencies);
        Project project = new Project();
        project.setProjectInfo(projectInfo);
        DependencyProject dependencyProject;
        DependencyLib dependencyLib;
        DependencyUrl dependencyUrl;
        for (String s : dependenciesSet) {
            if (s == null) {
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
                if (s.contains(":")) {
                    String[] tempDependencies = s.split(":");
                    if (tempDependencies.length >= 3) {
                        String group = tempDependencies[0];
                        String artifactId="";
                        if (tempDependencies.length >= 2) {
                            artifactId = tempDependencies[1];
                        }
                        String version = "";
                        if (tempDependencies.length>=3) {
                            version = tempDependencies[2];
                        }
                        dependencyUrl = new DependencyUrl(group, artifactId, version);
                        project.getDependencyUrls().add(dependencyUrl);
                    }
                }
            }
        }
        for (GradleFile gradleFile : gradleFileList) {
            log = log + gradleFile.getPath() + "\n";
        }
        FileUtil.writeFlie(logFileName, log);
        FileUtil.writeFlie(outputFileName, new GsonBuilder()
                .setPrettyPrinting()
                .create().toJson(project));

        L.l("=====================");

        ParseUtil.clearDependencies();
    }

    private static String[] getCompanyAndProjectName(String projectPath) {
        String companyName = "";
        String projectName = "";
        String[] tempName = projectPath.split("\\\\");
        int size = tempName.length;
        String name = tempName[size - 1];
        if (name.contains("__fdse__")) {
            companyName = name.split("__fdse__")[0];
            projectName = name.split("__fdse__")[1];
        } else {
            companyName = tempName[size - 2];
            projectName = name;
        }
        return new String[]{companyName, projectName};

    }

    private static List<String> getProjectPathList(String fileName) {
        String path = FileUtil.read(fileName);
        List<String> list = new Gson().fromJson(path, new TypeToken<List<String>>() {
        }.getType());
        return list;
    }

    /**
     * @return
     */
    private static List<File> getExperimentFiles() {
        List<File> fileList = new ArrayList<>();
//        List<String> gradle500List = getGradle500("D:/cs/projects_last/gradle500");
//        FileUtil.writeFlie("gradle500.txt", new Gson().toJson(gradle500List));
//        List<String> gradle200_500List = getGradle500_200();
//        FileUtil.writeFlie("gradle200_500.txt", new Gson().toJson(gradle200_500List));
//        List<String> maven_gradle500List = getGradle500("D:\\cs\\projects_last\\gradle_maven500");
//        FileUtil.writeFlie("maven_gradle500.txt", new Gson().toJson(maven_gradle500List));
//        List<String> maven_gradle200_500List = getGradle500("D:\\cs\\projects_last\\gradle_maven200_500");
//        FileUtil.writeFlie("maven_gradle200_500.txt", new Gson().toJson(maven_gradle200_500List));
        return null;
    }

    /**
     * get Gradle Projects stars greater than 200 less than 500
     *
     * @return
     */
    private static List<String> getGradle500_200() {
        List<String> resultProjectList = new ArrayList<>();
        String basePath = "D:\\cs\\projects_last\\gradle200_500\\home\\fdse\\data\\prior_repository";
        File[] companyList = new File(basePath).listFiles();
        for (File company : companyList) {
            File[] projectList = company.listFiles();
            for (File project : projectList) {
                resultProjectList.add(project.getAbsolutePath());
            }
        }
        return resultProjectList;
    }

    /**
     * get Gradle Project stars greater than 500
     *
     * @return
     */
    private static List<String> getGradle500(String basePath) {
        File[] projectList = new File(basePath).listFiles();
        List<String> resultProject = new ArrayList<>();
        int index = 0;
        for (File project : projectList) {
            if (project.isFile()) {
                index++;
                continue;
            }
            //project:[xxx__fdse__xxx,yyy__fdse__yyy,]
            //projectFileList represents a list whose item is file make up the project
            //2 condition:
            //1.projectFileList = [.git,gradle,src....]
            //2.projectFileList = [home]
            File[] projectFileList = project.listFiles();
            if (projectFileList.length == 1) {
                //condition 2:
                String companyName = project.getName().split("__fdse__")[0];
                String projectName = project.getName().split("__fdse__")[1];
                File realProject = new File(project.getAbsolutePath() + "/home/fdse/data/prior_repository/" + companyName + '/' + projectName);
                projectList[index] = realProject;
            }
            resultProject.add(projectList[index].getAbsolutePath());
            index++;
        }
        return resultProject;
    }
}
