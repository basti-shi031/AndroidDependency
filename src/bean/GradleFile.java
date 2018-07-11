package bean;

public class GradleFile {
    String path;
    String content;
    String type;
    boolean isRootFile;
    boolean isBuildFile;

    public boolean isBuildFile() {
        return isBuildFile;
    }

    public void setBuildFile(boolean buildFile) {
        isBuildFile = buildFile;
    }

    public GradleFile(String path, String content) {
        this.path = path;
        this.content = content;
    }

    public GradleFile() {

    }

    public boolean isRootFile() {
        return isRootFile;
    }

    public void setRootFile(boolean rootFile) {
        isRootFile = rootFile;
    }

    public GradleFile(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
