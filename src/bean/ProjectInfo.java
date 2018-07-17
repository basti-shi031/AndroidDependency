package bean;

public class ProjectInfo {


    /**
     * commit_count : 1480
     * fork : 3949
     * sizes : 21244
     * local_addr : ../data/prior_repository/JakeWharton/ActionBarSherlock
     * repository_id : 1451060
     * id : 1
     * stars : 7320
     * url : https://github.com/JakeWharton/ActionBarSherlock
     */
    private int commit_count;
    private int fork;
    private int sizes;
    private String local_addr;
    private int repository_id;
    private int id;
    private int stars;
    private String url;

    public void setCommit_count(int commit_count) {
        this.commit_count = commit_count;
    }

    public void setFork(int fork) {
        this.fork = fork;
    }

    public void setSizes(int sizes) {
        this.sizes = sizes;
    }

    public void setLocal_addr(String local_addr) {
        this.local_addr = local_addr;
    }

    public void setRepository_id(int repository_id) {
        this.repository_id = repository_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCommit_count() {
        return commit_count;
    }

    public int getFork() {
        return fork;
    }

    public int getSizes() {
        return sizes;
    }

    public String getLocal_addr() {
        return local_addr;
    }

    public int getRepository_id() {
        return repository_id;
    }

    public int getId() {
        return id;
    }

    public int getStars() {
        return stars;
    }

    public String getUrl() {
        return url;
    }
}
