package gustavo.mywine.app.model;


import android.app.Application;

public class SessionUser extends Application{

    private int userId;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
