package gustavo.mywine.app.model;


public class User {

    private int id;
    private String login;
    private String password;
    public static final String TABLE_NAME = "tbl_user";

    public void User(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
