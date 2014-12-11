package gustavo.mywine.app.dao;


import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import gustavo.mywine.app.core.BaseDao;
import gustavo.mywine.app.core.Database;
import gustavo.mywine.app.model.SessionUser;
import gustavo.mywine.app.model.User;

public class UserDao extends BaseDao {

    public static final String TABLE_NAME = User.TABLE_NAME;
    private SessionUser global;

    public UserDao(Context context){
        super(context);
        this.global = (SessionUser)context;
    }

    public void insertObject(User user){

        Database database = new Database(getContext());

        String sql = "insert into " + TABLE_NAME + " (fld_login,fld_password) values ('" + user.getLogin() + "','" + user.getPassword() + "');";
        database.executeInsert(sql);
        database.closeDataBase();

    }

    public User getObject(int id){

        ArrayList<User> result = getObjects()!=null ? getObjects(id) : null;
        return result.get(0);
    }

    public User getObject(String login,String password){

        Database database = new Database(getContext());

        User result = null;

        Cursor c = null;

        String[] fields = {"fld_id","fld_login","fld_password"};

        String where  = null;
        if(login!=null && password !=null){
            where = "fld_login ='"+login+"' AND fld_password = '"+password+"' ";
        }else if(login!=null && password ==null){
            where = "fld_login ='"+login+"' ";
        }

        c = database.executeQuery(TABLE_NAME, fields,where,"fld_id");

        if(c.getCount()!=0){
            while(c.moveToNext()) {

                User aux = new User();

                aux.setId(c.getInt(c.getColumnIndex("fld_id")));
                aux.setLogin(c.getString(c.getColumnIndex("fld_login")));
                aux.setPassword(c.getString(c.getColumnIndex("fld_password")));

                result = aux;
            }
        }
        database.closeDataBase();
        return result;
    }

    public ArrayList<User> getObjects(){
        return getObjects(0);
    }

    public ArrayList<User> getObjects(int id){

        Database database = new Database(getContext());
        ArrayList<User> result = null;

        Cursor c = null;
        String where = null;

        if(id!=0){where = "fld_id = '"+id+"'";};

        String[] fields = {"fld_id","fld_login","fld_password"};
        c = database.executeQuery(TABLE_NAME, fields,where,"fld_id");

        if(c.getCount()!=0){

            result = new ArrayList<User>(c.getCount());

            while(c.moveToNext()){

                User aux = new User();

                aux.setId(c.getInt(c.getColumnIndex("fld_id")));
                aux.setLogin(c.getString(c.getColumnIndex("fld_login")));
                aux.setPassword(c.getString(c.getColumnIndex("fld_password")));

                result.add(aux);
            }
        }
        database.closeDataBase();
        return result;
    }

    public SessionUser getGlobal() {
        return global;
    }

    public void setGlobal(SessionUser global) {
        this.global = global;
    }
}
