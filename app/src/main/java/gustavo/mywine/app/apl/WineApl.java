package gustavo.mywine.app.apl;

import android.content.Context;

import java.util.ArrayList;

import gustavo.mywine.app.core.BaseApl;
import gustavo.mywine.app.dao.WineDao;
import gustavo.mywine.app.model.Wine;

public class WineApl extends BaseApl {

    private WineDao dao;

    public WineApl(Context context){
        super(context);
        this.dao = new WineDao(context);
    }

    public void insertObject(Wine wine){
        getDao().insertObject(wine);
    }

    public Wine getObject(int id){
        return getDao().getObject(id);
    }

    public ArrayList<Wine> getObjects(){
        return getDao().getObjects();
    }

    public ArrayList<Wine> getObjectsOrderByLabel(){
        return getDao().getObjectsOrderByLabel();
    }

    public ArrayList<Wine> getObjectsOrderByYear(){
        return getDao().getObjectsOrderByYear();
    }

    public void deleteObject(int id){
        getDao().deleteObject(id);
    }

    public WineDao getDao() {
        return dao;
    }

    public void setDao(WineDao dao) {
        this.dao = dao;
    }
}
