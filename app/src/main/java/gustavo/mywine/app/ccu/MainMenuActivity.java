package gustavo.mywine.app.ccu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import gustavo.mywine.app.R;
import gustavo.mywine.app.model.SessionUser;
import gustavo.mywine.app.util.Functions;

public class MainMenuActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private NavigationDrawerFragment navigationDrawerFragment;
    private CharSequence titleFragment;
    private int idMenuOptionSelected;
    private SessionUser global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        navigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));

        this.global = ((SessionUser) getApplicationContext());

        titleFragment = getTitle();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(getIdMenuOptionSelected()==R.id.nav_menu_wine){
            getMenuInflater().inflate(R.menu.wine_menu, menu);
            restoreActionBar();
            return true;
        }else if(getIdMenuOptionSelected()==R.id.nav_menu_rss){
            getMenuInflater().inflate(R.menu.rss_menu, menu);
            restoreActionBar();
            return true;
        }
        else if (!getNavigationDrawerFragment().isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(titleFragment);
    }

    @Override
    public void onNavigationDrawerItemSelected(MenuItem item) {

        setIdMenuOptionSelected(item.getItemId());

        android.support.v4.app.Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.nav_menu_wine:{
                setTitleFragment(item.getTitle());
                fragment = new WineFragment();
                break;
            }
            case R.id.nav_menu_rss:{
                setTitleFragment(item.getTitle());
                fragment = new RSSFragment();
                break;
            }
            case R.id.nav_menu_about:{
                about(MainMenuActivity.this);
                break;
            }
            case R.id.nav_menu_logout:{
                logout();
                break;
            }
        }

        if (fragment != null) {
            // update selected item and title, then close the drawer
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(getTitleFragment());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void logout(){

        finish();
        Intent i = new Intent();
        i.setClass(MainMenuActivity.this, LoginActivity.class);
        startActivity(i);

    }

    public void about(Context context){
        String title,msg;
        title = context.getString(R.string.title_alert_dialog_about);
        msg = context.getString(R.string.text_alert_dialog_about);

        Functions.alertDialog(context,title,msg,null,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        },null,null);
    }

    public NavigationDrawerFragment getNavigationDrawerFragment() {
        return navigationDrawerFragment;
    }

    public void setNavigationDrawerFragment(NavigationDrawerFragment navigationDrawerFragment) {
        this.navigationDrawerFragment = navigationDrawerFragment;
    }

    public CharSequence getTitleFragment() {
        return titleFragment;
    }

    public void setTitleFragment(CharSequence titleFragment) {
        this.titleFragment = titleFragment;
    }

    public int getIdMenuOptionSelected() {
        return idMenuOptionSelected;
    }

    public void setIdMenuOptionSelected(int idMenuOptionSelected) {
        this.idMenuOptionSelected = idMenuOptionSelected;
    }

    public SessionUser getGlobal() {
        return global;
    }

    public void setGlobal(SessionUser global) {
        this.global = global;
    }

}
