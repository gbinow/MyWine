package gustavo.mywine.app.dao;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import gustavo.mywine.app.core.BaseDao;
import gustavo.mywine.app.core.Database;
import gustavo.mywine.app.model.SessionUser;
import gustavo.mywine.app.model.Wine;

public class WineDao extends BaseDao {

    public static final String TABLE_NAME = Wine.TABLE_NAME;
    private Database database;
    private SessionUser global;

    public WineDao(Context context) {
        super(context);
        this.global = (SessionUser)context;
    }

    public void insertObject(Wine wine){

        Database database = new Database(getContext());

        database.executeInsert("insert into "+TABLE_NAME+" (fld_label,fld_year,fld_grape,fld_rating,fld_observations,tbl_user_fld_id)" +
        " values ('" + wine.getLabel() + "','" + wine.getYear()  + "','" + wine.getGrape() + "','" + wine.getRating()  +
        "','" + wine.getObservations()+"','" + wine.getUserId()+ "')");
        database.closeDataBase();
    }

    public Wine getObject(int id){

        ArrayList<Wine> result = getObjects()!=null ? getObjects(id,null) : null;
        return result.get(0);
    }

    public ArrayList<Wine> getObjects(){
        return getObjects(0,null);
    }

    public ArrayList<Wine> getObjectsOrderByLabel(){
        return getObjects(0,"fld_label");
    }

    public ArrayList<Wine> getObjectsOrderByYear(){
        return getObjects(0,"fld_year");
    }

    public ArrayList<Wine> getObjects(int id, String orderBy){

        Database database = new Database(getContext());

        ArrayList<Wine> result = null;

        Cursor c = null;
        String where = "";

        orderBy = orderBy==null ? "fld_id" : orderBy;

        if(id!=0){
            where = "fld_id = '"+id+"' AND tbl_user_fld_id='" + getGlobal().getUserId() + "' ";
        }else {
            where = where + " tbl_user_fld_id='" + getGlobal().getUserId() + "' ";
        }

        String[] fields = {"fld_id","fld_label","fld_year","fld_grape","fld_rating","fld_observations"};
        c = database.executeQuery(TABLE_NAME, fields,where,orderBy);

        if(c.getCount()>0){

            result = new ArrayList<Wine>(c.getCount());

            while(c.moveToNext()){

                Wine aux = new Wine();

                aux.setId(c.getInt(c.getColumnIndex("fld_id")));
                aux.setLabel(c.getString(c.getColumnIndex("fld_label")));
                aux.setYear(c.getInt(c.getColumnIndex("fld_year")));
                aux.setGrape(c.getString(c.getColumnIndex("fld_grape")));
                aux.setRating(c.getInt(c.getColumnIndex("fld_rating")));
                aux.setObservations(c.getString(c.getColumnIndex("fld_observations")));

                result.add(aux);
            }
        }
        database.closeDataBase();
        return result;
    }

    public void deleteObject(int id){
        Database database = new Database(getContext());
        database.executeDelete(TABLE_NAME,"fld_id = '"+id+"' AND tbl_user_fld_id='" + getGlobal().getUserId() + "' ");
    }

    public SessionUser getGlobal() {
        return global;
    }

    public void setGlobal(SessionUser global) {
        this.global = global;
    }
}
