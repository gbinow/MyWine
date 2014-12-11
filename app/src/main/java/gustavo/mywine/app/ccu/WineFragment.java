package gustavo.mywine.app.ccu;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import gustavo.mywine.app.R;
import gustavo.mywine.app.adapter.ListWineAdapter;
import gustavo.mywine.app.apl.WineApl;
import gustavo.mywine.app.model.SessionUser;
import gustavo.mywine.app.model.Wine;
import gustavo.mywine.app.util.Functions;

public class WineFragment extends Fragment implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private View view;
    private List<String> optionsRating;
    private ArrayList<Wine> wines;
    static final int ORDER_ID = 0;
    static final int ORDER_LABEL = 1;
    static final int ORDER_YEAR = 2;
    private SessionUser global;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_wine, container, false);
        this.optionsRating = new ArrayList<String>();
        this.optionsRating.add(getActivity().getString(R.string.text_wine_options_rating_0));
        this.optionsRating.add(getActivity().getString(R.string.text_wine_options_rating_1));
        this.optionsRating.add(getActivity().getString(R.string.text_wine_options_rating_2));
        this.optionsRating.add(getActivity().getString(R.string.text_wine_options_rating_3));
        this.optionsRating.add(getActivity().getString(R.string.text_wine_options_rating_4));
        this.global = (SessionUser)getActivity().getApplicationContext();
        start();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void start(){
        initializeWineList(ORDER_ID);
    }

    private void initializeWineList(int orderBy){

        WineApl wineApl = new WineApl(getActivity().getApplicationContext());
        ArrayList<Wine> wines = null;

        if(orderBy==ORDER_LABEL) {
            wines = wineApl.getObjectsOrderByLabel();
        }else if(orderBy==ORDER_YEAR) {
            wines = wineApl.getObjectsOrderByYear();
        }else{
            wines = wineApl.getObjects();
        }

        setWines(wines);

        final ListView list = (ListView) getView().findViewById(R.id.list_wine);

        list.setAdapter(new ListWineAdapter(WineFragment.this,wines));

        list.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               String observations = getWines().get(position).getObservations();
               observationsWine(getActivity(),observations);
               return true;
            }
        });

        list.setOnItemClickListener(new ListView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {

                Wine wine = getWines().get(position);
                alertDialogEditDeleteWine(getActivity(),wine);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();


        switch(itemId){
            case R.id.wine_action_add:{
                dialogAddWine();
                return true;
            }
            case R.id.wine_action_order_name:{
                initializeWineList(ORDER_LABEL);
                return true;
            }
            case R.id.wine_action_order_year:{
                initializeWineList(ORDER_YEAR);
                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(MenuItem item) {

    }

    private void dialogAddWine() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_wine_add);
        dialog.setTitle(getActivity().getString(R.string.title_dialog_wine_add));

        final Button save = (Button) dialog.findViewById(R.id.dialog_wine_add_button_save);
        final Button cancel = (Button) dialog.findViewById(R.id.dialog_wine_add_button_cancel);
        final EditText label = (EditText) dialog.findViewById(R.id.dialog_wine_add_label);
        final EditText year = (EditText) dialog.findViewById(R.id.dialog_wine_add_year);
        final EditText grape = (EditText) dialog.findViewById(R.id.dialog_wine_add_grape);
        final Spinner rating = (Spinner) dialog.findViewById(R.id.dialog_wine_add_rating);
        final EditText observations = (EditText) dialog.findViewById(R.id.dialog_wine_add_observations);

        final List<String> stringsRating = getOptionsRating();
        ArrayAdapter<String> adapterRating;

        adapterRating = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item , stringsRating);
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapterRating.setNotifyOnChange(true);
        rating.setAdapter(adapterRating);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean validation = true;
                View focusView = null;

                if(TextUtils.isEmpty(label.getText().toString().trim())){
                    label.setError(getActivity().getString(R.string.error_field_required_dialog_wine_add));
                    focusView =  focusView==null ? label : focusView;
                    validation = false;
                }
                if(TextUtils.isEmpty(year.getText().toString().trim())){
                    year.setError(getActivity().getString(R.string.error_field_required_dialog_wine_add));
                    focusView =  focusView==null ? year : focusView;
                    validation = false;
                }
                if(TextUtils.isEmpty(grape.getText().toString().trim())){
                    focusView =  focusView==null ? grape : focusView;
                    grape.setError(getActivity().getString(R.string.error_field_required_dialog_wine_add));
                    validation = false;
                }

                if(validation==false) {
                    focusView.requestFocus();
                }else{

                    Wine wine = new Wine();
                    wine.setLabel(label.getText().toString().trim());
                    wine.setYear(Integer.parseInt(year.getText().toString().trim()));
                    wine.setGrape(grape.getText().toString().trim());
                    wine.setRating(rating.getSelectedItemPosition());
                    wine.setObservations(observations.getText().toString().trim());
                    wine.setUserId(getGlobal().getUserId());

                    addWine(wine);

                    initializeWineList(ORDER_ID);
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

    private void dialogEditWine(final Wine wine){

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_wine_edit);
        dialog.setTitle(getActivity().getString(R.string.title_dialog_wine_edit));


        final Button save = (Button) dialog.findViewById(R.id.dialog_wine_edit_button_save);
        final Button cancel = (Button) dialog.findViewById(R.id.dialog_wine_edit_button_cancel);
        final EditText label = (EditText) dialog.findViewById(R.id.dialog_wine_edit_label);
        final EditText year = (EditText) dialog.findViewById(R.id.dialog_wine_edit_year);
        final EditText grape = (EditText) dialog.findViewById(R.id.dialog_wine_edit_grape);
        final Spinner rating = (Spinner) dialog.findViewById(R.id.dialog_wine_edit_rating);
        final EditText observations = (EditText) dialog.findViewById(R.id.dialog_wine_edit_observations);

        final List<String> stringsRating = getOptionsRating();
        ArrayAdapter<String> adapterRating;

        adapterRating = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item , stringsRating);
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapterRating.setNotifyOnChange(true);
        rating.setAdapter(adapterRating);

        label.setText(wine.getLabel());
        year.setText(Integer.toString(wine.getYear()));
        grape.setText(wine.getGrape());
        rating.setSelection(wine.getRating());
        observations.setText(wine.getObservations());

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean validation = true;
                View focusView = null;

                if(TextUtils.isEmpty(label.getText().toString().trim())){
                    label.setError(getActivity().getString(R.string.error_field_required_dialog_wine_edit));
                    focusView =  focusView==null ? label : focusView;
                    validation = false;
                }
                if(TextUtils.isEmpty(year.getText().toString().trim())){
                    year.setError(getActivity().getString(R.string.error_field_required_dialog_wine_edit));
                    focusView =  focusView==null ? year : focusView;
                    validation = false;
                }
                if(TextUtils.isEmpty(grape.getText().toString().trim())){
                    focusView =  focusView==null ? grape : focusView;
                    grape.setError(getActivity().getString(R.string.error_field_required_dialog_wine_edit));
                    validation = false;
                }

                if(validation==false) {
                    focusView.requestFocus();
                }else{

                    wine.setLabel(label.getText().toString().trim());
                    wine.setYear(Integer.parseInt(year.getText().toString().trim()));
                    wine.setGrape(grape.getText().toString().trim());
                    wine.setRating(rating.getSelectedItemPosition());
                    wine.setObservations(observations.getText().toString().trim());
                    wine.setUserId(getGlobal().getUserId());

                    updateWine(wine);

                    initializeWineList(ORDER_ID);
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

    private void addWine(Wine wine){

        WineApl wineApl = new WineApl(getActivity().getApplicationContext());
        wineApl.insertObject(wine);
    }


    public void alertDialogEditDeleteWine(Context context,final Wine wine){
        String title,msg;

        title = context.getString(R.string.title_alert_dialog_list_wine_edit_delete);
        msg=context.getString(R.string.text_alert_dialog_list_wine_edit_delete_question);

        String[] text_buttons;
        text_buttons = new String[3];
        text_buttons[0]=context.getString(R.string.text_alert_dialog_list_wine_button_cancel);
        text_buttons[1]=context.getString(R.string.text_alert_dialog_list_wine_button_edit);
        text_buttons[2]=context.getString(R.string.text_alert_dialog_list_wine_button_delete);

        Functions.alertDialog(context,title,msg,text_buttons,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        },
        new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialogEditWine(wine);
                dialog.cancel();
            }
        },
        new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                deleteWine(wine);
                initializeWineList(ORDER_ID);
                dialog.cancel();
            }
        }
        );
    }

    private void updateWine(Wine wine){

        WineApl wineApl = new WineApl(getActivity().getApplicationContext());
        wineApl.insertObject(wine);

    }

    private void deleteWine(Wine wine){

        WineApl wineApl = new WineApl(getActivity().getApplicationContext());
        wineApl.deleteObject(wine.getId());
    }

    private void observationsWine(Context context,String observations){

        String title,msg;
        title = context.getString(R.string.title_alert_dialog_list_wine_observations);
        msg = observations.trim().length() == 0 ? context.getString(R.string.warning_alert_dialog_list_wine_observations) : observations ;

        Functions.alertDialog(context, title, msg, null, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }, null, null);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<String> getOptionsRating() {
        return optionsRating;
    }

    public void setOptionsRating(List<String> optionsRating) {
        this.optionsRating = optionsRating;
    }

    public ArrayList<Wine> getWines() {
        return wines;
    }

    public void setWines(ArrayList<Wine> wines) {
        this.wines = wines;
    }

    public SessionUser getGlobal() {
        return global;
    }

    public void setGlobal(SessionUser global) {
        this.global = global;
    }
}
