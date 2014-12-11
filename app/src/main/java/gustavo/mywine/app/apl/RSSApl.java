package gustavo.mywine.app.apl;


import android.content.Context;

import java.util.ArrayList;

import gustavo.mywine.app.core.BaseApl;
import gustavo.mywine.app.dao.RSSDao;
import gustavo.mywine.app.model.RSS;

public class RSSApl extends BaseApl {

    private RSSDao dao;

    public  RSSApl(Context context){
        super(context);
        this.dao = new RSSDao(context);
    }

    public void insertObject(RSS rss){
        getDao().insertObject(rss);
    }

    public RSS getObject(int id){
        return getDao().getObject(id);
    }

    public ArrayList<RSS> getObjects(){
        return getDao().getObjects();
    }

    public void deleteObject(int id){
        getDao().deleteObject(id);
    }

    public RSSDao getDao() {
        return dao;
    }

    public void setDao(RSSDao dao) {
        this.dao = dao;
    }
}
