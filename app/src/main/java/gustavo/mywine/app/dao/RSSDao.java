package gustavo.mywine.app.dao;


import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import gustavo.mywine.app.core.BaseDao;
import gustavo.mywine.app.core.Database;
import gustavo.mywine.app.model.RSS;
import gustavo.mywine.app.model.SessionUser;

public class RSSDao extends BaseDao {

    private static final String TABLE_NAME = RSS.TABLE_NAME;
    private Database database;
    private SessionUser global;

    public RSSDao(Context context) {

        super(context);
        this.global = (SessionUser)context;
    }

    public void insertObject(RSS rss){

        Database database = new Database(getContext());

        database.executeInsert("insert into "+TABLE_NAME+" (fld_name,fld_url,tbl_user_fld_id) values ('" + rss.getName() + "','" + rss.getUrl()+ "','" + rss.getUserId()+ "')");
        database.closeDataBase();
    }

    public RSS getObject(int id){

        ArrayList<RSS> result = getObjects()!=null ? getObjects(id) : null;
        return result.get(0);
    }

    public ArrayList<RSS> getObjects(){
        return getObjects(0);
    }

    public ArrayList<RSS> getObjects(int id){

        Database database = new Database(getContext());

        ArrayList<RSS> result = null;

        Cursor c = null;
        String where = "";

        if(id!=0){where = " fld_id = '"+id+"' AND tbl_user_fld_id='" + getGlobal().getUserId() + "' ";}
        else {where = " tbl_user_fld_id='" + getGlobal().getUserId() + "' ";}

        String[] fields = {"fld_id","fld_name","fld_url","tbl_user_fld_id"};
        c = database.executeQuery(TABLE_NAME, fields,where,"fld_id");

        if(c.getCount()>0){

            result = new ArrayList<RSS>(c.getCount());

            while(c.moveToNext()){

                RSS aux = new RSS();

                aux.setId(c.getInt(c.getColumnIndex("fld_id")));
                aux.setName(c.getString(c.getColumnIndex("fld_name")));
                aux.setUrl(c.getString(c.getColumnIndex("fld_url")));
                aux.setUserId(c.getInt(c.getColumnIndex("tbl_user_fld_id")));

                result.add(aux);
            }
        }
        database.closeDataBase();
        return result;
    }

    public void deleteObject(int id){
        Database database = new Database(getContext());
        database.executeDelete(TABLE_NAME,"fld_id = '"+id+"'  AND tbl_user_fld_id='" + getGlobal().getUserId() + "' ");
    }

    public SessionUser getGlobal() {
        return global;
    }

    public void setGlobal(SessionUser global) {
        this.global = global;
    }

}
