package gustavo.mywine.app.core;

import android.content.Context;

public class BaseDao {

    private Context context;

    public BaseDao(Context context){

        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



}
