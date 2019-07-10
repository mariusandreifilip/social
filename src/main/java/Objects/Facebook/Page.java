package Objects.Facebook;

public class Page {

    String name;
    long pageid;

    String token;


    public Page(String token, String name, long pageid) {

        this.token = token;
        this.name = name;
        this.pageid = pageid;

    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }


}
