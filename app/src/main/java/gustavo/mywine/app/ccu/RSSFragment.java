package gustavo.mywine.app.ccu;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import gustavo.mywine.app.R;
import gustavo.mywine.app.adapter.ListRSSAdapter;
import gustavo.mywine.app.apl.RSSApl;
import gustavo.mywine.app.model.RSS;
import gustavo.mywine.app.model.SessionUser;
import gustavo.mywine.app.util.Functions;

public class RSSFragment extends Fragment implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private View view;
    private ArrayList<RSS> rsses;
    private SessionUser global;

    public RSSFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_rss, container, false);
        this.global = ((SessionUser) getActivity().getApplicationContext());
        start();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void start(){
        initializeRSSFragment();
    }

    private void initializeRSSFragment(){
       initializeRSSList();
    }

    private void initializeRSSList(){

        RSSApl rssApl = new RSSApl(getActivity().getApplicationContext());
        ArrayList<RSS> itens = rssApl.getObjects();

        if(itens!=null){

            final ListView list = (ListView) getView().findViewById(R.id.list_rss);
            setRsses(itens);
            list.setAdapter(new ListRSSAdapter(RSSFragment.this,itens));

            list.setOnItemClickListener(new ListView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {

                    RSS rss = getRsses().get(position);
                    alertDialogVisualizeEditDeleteWine(getActivity(),rss);

                }
            });

        }else{
            Toast.makeText(getActivity(), getActivity().getString(R.string.warning_list_wine_without_result_feed_rss), Toast.LENGTH_SHORT).show();
        }

    }

    private void dialogAddRSS() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_rss_add);
        dialog.setTitle(getActivity().getString(R.string.title_dialog_rss_add));

        final Button save = (Button) dialog.findViewById(R.id.dialog_rss_add_button_save);
        final Button cancel = (Button) dialog.findViewById(R.id.dialog_rss_add_button_cancel);
        final EditText name = (EditText) dialog.findViewById(R.id.dialog_rss_add_name);
        final EditText url = (EditText) dialog.findViewById(R.id.dialog_rss_add_url);


        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean validation = true;
                View focusView = null;

                if(TextUtils.isEmpty(name.getText().toString().trim())){
                    name.setError(getActivity().getString(R.string.error_field_required_dialog_rss_add));
                    focusView =  focusView==null ? name : focusView;
                    validation = false;
                }
                if(TextUtils.isEmpty(url.getText().toString().trim())){
                    url.setError(getActivity().getString(R.string.error_field_required_dialog_rss_add));
                    focusView =  focusView==null ? url : focusView;
                    validation = false;
                }else{

                    RSS rss = new RSS();
                    rss.setName(name.getText().toString().trim());
                    rss.setUrl(url.getText().toString().trim());
                    rss.setUserId(getGlobal().getUserId());
                    addRSS(rss);

                    initializeRSSList();

                    dialog.cancel();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void dialogEditRSS(final RSS rss) {

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_rss_edit);
        dialog.setTitle(getActivity().getString(R.string.title_dialog_rss_edit));

        final Button save = (Button) dialog.findViewById(R.id.dialog_rss_edit_button_save);
        final Button cancel = (Button) dialog.findViewById(R.id.dialog_rss_edit_button_cancel);
        final EditText name = (EditText) dialog.findViewById(R.id.dialog_rss_edit_name);
        final EditText url = (EditText) dialog.findViewById(R.id.dialog_rss_edit_url);

        name.setText(rss.getName());
        url.setText(rss.getUrl());

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean validation = true;
                View focusView = null;

                if(TextUtils.isEmpty(name.getText().toString().trim())){
                    name.setError(getActivity().getString(R.string.error_field_required_dialog_rss_edit));
                    focusView =  focusView==null ? name : focusView;
                    validation = false;
                }
                if(TextUtils.isEmpty(url.getText().toString().trim())){
                    url.setError(getActivity().getString(R.string.error_field_required_dialog_rss_edit));
                    focusView =  focusView==null ? url : focusView;
                    validation = false;
                }else{

                    rss.setName(name.getText().toString().trim());
                    rss.setUrl(url.getText().toString().trim());
                    rss.setUserId(getGlobal().getUserId());
                    updateRSS(rss);

                    initializeRSSList();

                    dialog.cancel();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void addRSS(RSS rss){

        RSSApl rssApl = new RSSApl(getActivity().getApplicationContext());
        rssApl.insertObject(rss);

    }

    public void alertDialogVisualizeEditDeleteWine(Context context,final RSS rss){
        String title,msg;

        title = context.getString(R.string.title_alert_dialog_list_rss_edit_delete);
        msg=context.getString(R.string.text_alert_dialog_list_rss_edit_delete_question);

        String[] text_buttons;
        text_buttons = new String[3];
        text_buttons[0]=context.getString(R.string.text_alert_dialog_list_rss_button_visualize);
        text_buttons[1]=context.getString(R.string.text_alert_dialog_list_rss_button_edit);
        text_buttons[2]=context.getString(R.string.text_alert_dialog_list_rss_button_delete);

        Functions.alertDialog(context, title, msg, text_buttons, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        visualizeRSS(rss);
                    }
                },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialogEditRSS(rss);
                        dialog.cancel();
                    }
                },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        deleteRSS(rss);
                        initializeRSSList();
                        dialog.cancel();
                    }
                }
        );
    }

    private void updateRSS(RSS rss){
        RSSApl rssApl = new RSSApl(getActivity().getApplicationContext());
        rssApl.insertObject(rss);
        rssApl.deleteObject(rss.getId());
    }

    private void deleteRSS(RSS rss){
        RSSApl rssApl = new RSSApl(getActivity().getApplicationContext());
        rssApl.deleteObject(rss.getId());
    }

    private void visualizeRSS(RSS rss){
        Fragment fragment = new RSSFeedFragment(rss);

        if (fragment != null) {

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId= item.getItemId();
        switch(itemId){
            case R.id.rss_action_add:{
                dialogAddRSS();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(MenuItem item) {
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ArrayList<RSS> getRsses() {
        return rsses;
    }

    public void setRsses(ArrayList<RSS> rsses) {
        this.rsses = rsses;
    }

    public SessionUser getGlobal() {
        return global;
    }

    public void setGlobal(SessionUser global) {
        this.global = global;
    }


}
