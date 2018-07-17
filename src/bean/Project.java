package bean;

import java.util.ArrayList;
import java.util.List;

public class Project {

    ProjectInfo projectInfo;
    List<DependencyLib> dependencyLibs;
    List<DependencyProject> dependencyProjects;
    List<DependencyUrl> dependencyUrls;


    public ProjectInfo getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
    }

    public List<DependencyLib> getDependencyLibs() {
        return dependencyLibs;
    }

    public void setDependencyLibs(List<DependencyLib> dependencyLibs) {
        this.dependencyLibs = dependencyLibs;
    }

    public List<DependencyProject> getDependencyProjects() {
        return dependencyProjects;
    }

    public void setDependencyProjects(List<DependencyProject> dependencyProjects) {
        this.dependencyProjects = dependencyProjects;
    }

    public List<DependencyUrl> getDependencyUrls() {
        return dependencyUrls;
    }

    public void setDependencyUrls(List<DependencyUrl> dependencyUrls) {
        this.dependencyUrls = dependencyUrls;
    }

    public Project() {
        projectInfo = new ProjectInfo();
        dependencyProjects = new ArrayList<>();
        dependencyLibs = new ArrayList<>();
        dependencyUrls = new ArrayList<>();
    }
}
