package gustavo.mywine.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import gustavo.mywine.app.ccu.LoginActivity;
import gustavo.mywine.app.model.IntroductionView;
import gustavo.mywine.app.util.Functions;


public class Main extends Activity {

    private final int time = 5000;
    private Thread splashTread;

    private LinearLayout introductionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        this.introductionView = (LinearLayout) findViewById(R.id.introduction_view);
        this.introductionView.addView(new IntroductionView(getApplicationContext()));
        Functions.alert(getApplicationContext(), getString(R.string.text_dialog_splash_screen_view_draw_canvas));

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
                    /*Ending current activity*/
                    finish();
                    Intent i = new Intent();
                    i.setClass(Main.this, LoginActivity.class);
                    /*Creates a new LoginActivity*/
                    startActivity(i);
                }
            }
        };

        splashTread.start();
    }
}
