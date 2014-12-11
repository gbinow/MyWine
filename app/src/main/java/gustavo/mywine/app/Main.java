package gustavo.mywine.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import gustavo.mywine.app.ccu.LoginActivity;


public class Main extends Activity {

    private int time = 2500;
    private Thread splashTread;
    private int condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        this.condition = 1;
        startSplashScreen();

    }

    public void startSplashScreen(){

        splashTread = new Thread() {

            @Override
            public void run() {
                try {
                    synchronized(this){
                        wait(time);
                    }

                } catch(InterruptedException e) {}
                finally {

                    if(getCondition()==1){
                        /*Ending current activity*/
                        finish();
                        Intent i = new Intent();
                        i.setClass(Main.this, LoginActivity.class);
                        /*Creates a new LoginActivity*/
                        startActivity(i);
                    }
                }
            }
        };

        splashTread.start();
    }

    //If a key is pressed, go to the login application.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        setCondition(0);

        finish();
        Intent i = new Intent();
        i.setClass(Main.this, LoginActivity.class);
        startActivity(i);

        return(true);
    }

    //If you experience a touch on the screen, go to the login application.
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        setCondition(0);

        finish();
        Intent i = new Intent();
        i.setClass(Main.this, LoginActivity.class);
        startActivity(i);

        return(true);
    }

    public Thread getSplashTread() {
        return splashTread;
    }

    public void setSplashTread(Thread splashTread) {
        this.splashTread = splashTread;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
