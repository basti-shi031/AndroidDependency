import bean.*;
import com.google.gson.GsonBuilder;
import util.DBUtil;
import util.FileUtil;
import util.L;
import util.ParseUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleParse {
    public static void main(String[] args) {
        int index = 0;
        String[] sourceTxts = {"gradle500.txt", "gradle200_500.txt", "maven_gradle500.txt", "maven_gradle200_500.txt"};
        String[] outputs = {"output/gradle500/", "output/gradle200_500/", "output/maven_gradle500/", "output/maven_gradle200_500/"};
        String[] logOuts = {"log/gradle500/", "log/gradle200_500/", "log/maven_gradle500/", "log/maven_gradle200_500/"};
        DBUtil.init();
        String baseProjectPath = "D:\\cs\\projects_last\\gradle500\\rholder__fdse__guava-retrying";
        parseProject(baseProjectPath, outputs[index], logOuts[index]);
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
            if (s.contains("org.springframework.cloud:spring-cloud-starter-eureka-server")) {
                int a = 1;
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
                    if (tempDependencies.length >= 1) {
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

}
