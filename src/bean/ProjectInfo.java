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
    private long commit_count;
    private long fork;
    private long sizes;
    private String local_addr;
    private long repository_id;
    private long id;
    private long stars;
    private String url;

    public ProjectInfo(long commit_count, long fork, long sizes, String local_addr, long repository_id, long id, long stars, String url) {
        this.commit_count = commit_count;
        this.fork = fork;
        this.sizes = sizes;
        this.local_addr = local_addr;
        this.repository_id = repository_id;
        this.id = id;
        this.stars = stars;
        this.url = url;
    }

    public ProjectInfo() {
    }

    public void setCommit_count(long commit_count) {
        this.commit_count = commit_count;
    }

    public void setFork(long fork) {
        this.fork = fork;
    }

    public void setSizes(long sizes) {
        this.sizes = sizes;
    }

    public void setLocal_addr(String local_addr) {
        this.local_addr = local_addr;
    }

    public void setRepository_id(long repository_id) {
        this.repository_id = repository_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStars(long stars) {
        this.stars = stars;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCommit_count() {
        return commit_count;
    }

    public long getFork() {
        return fork;
    }

    public long getSizes() {
        return sizes;
    }

    public String getLocal_addr() {
        return local_addr;
    }

    public long getRepository_id() {
        return repository_id;
    }

    public long getId() {
        return id;
    }

    public long getStars() {
        return stars;
    }

    public String getUrl() {
        return url;
    }
}
