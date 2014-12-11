package gustavo.mywine.app.core;

import android.content.Context;

public class BaseApl {

    private Context context;

    public BaseApl(Context context){

        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
