package gustavo.mywine.app.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

public class Functions {

    /**
     * Displays a temporary alert at the bottom of the page
     * @param context The context
     * @param text The text
     */
    public static void alert(Context context,String text){
        Toast alert = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        alert.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        alert.show();
    }

    /**
     * Displays a popup alert
     * @param context
     * @param title
     * @param msg
     * @param textButtons
     * @param listener Handles button 1 action
     * @param listener2 Handles button 2 action
     * @param listener3 Handles button 3 action
     */
    public static void alertDialog(Context context,String title, String msg, String[] textButtons, DialogInterface.OnClickListener listener,DialogInterface.OnClickListener listener2,DialogInterface.OnClickListener listener3){

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);

        if(textButtons == null){alertDialog.setButton("OK" , listener);
        }
        else{
            alertDialog.setButton(textButtons[0] , listener);
        }

        if(listener2 !=null){
            alertDialog.setButton2(textButtons[1] , listener2);
        }
        if(listener3 !=null){
            alertDialog.setButton3(textButtons[2] , listener3);
        }

        alertDialog.show();
    }

    /**
     * Opens the browser at the given URL
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);

    }
}
