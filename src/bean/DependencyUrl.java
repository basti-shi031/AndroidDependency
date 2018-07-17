package bean;

public class DependencyUrl {

    /**
     * groupId : com.actionbarsherlock
     * artifactId : actionbarsherlock
     * type : apklib
     * version : 4.4.0
     */
    private String groupId;
    private String artifactId;
    private String type;
    private String version;

    public DependencyUrl(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }
}
