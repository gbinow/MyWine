package gustavo.mywine.app.apl;


import android.content.Context;

import java.util.ArrayList;

import gustavo.mywine.app.core.BaseApl;
import gustavo.mywine.app.dao.UserDao;
import gustavo.mywine.app.model.User;

public class UserApl extends BaseApl{

    private UserDao dao;

    public UserApl(Context context){
        super(context);
        this.dao = new UserDao(context);
    }

    public void insertObject(User user){
        getDao().insertObject(user);
    }

    public User getObject(int id){
        return getDao().getObject(id);
    }

    public User getObject(String login,String password){
        return getDao().getObject(login,password);
    }

    public ArrayList<User> getObjects(){
        return getDao().getObjects();
    }

    public UserDao getDao() {
        return dao;
    }

    public void setDao(UserDao dao) {
        this.dao = dao;
    }


}
