package gustavo.mywine.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import gustavo.mywine.app.R;
import gustavo.mywine.app.ccu.WineFragment;
import gustavo.mywine.app.model.Wine;

public class ListWineAdapter extends BaseAdapter {

    private Fragment fragment;
    private ArrayList<Wine> itens;
    private Activity activity;

    public ListWineAdapter(WineFragment fragment, ArrayList<Wine> itens){
        this.fragment 	= fragment;
        this.itens		= itens;
        this.activity   = fragment.getActivity();
    }

    public int getCount() {
        return(this.itens.size());
    }

    public Object getItem(int position) {
        return(this.itens.get(position));
    }


    public long getItemId(int position) {
        return(position);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Wine item = (Wine) getItens().get(position);

        //Responsible for communication between XML, the application screen and the Adapter ListWine class.
        LayoutInflater inflater;
        inflater = (LayoutInflater) getFragment().getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_wine, null);

        TextView label = (TextView) v.findViewById(R.id.list_wine_label);
        label.setText(getFragment().getString(R.string.list_wine_label)+" "+item.getLabel());

        TextView year = (TextView) v.findViewById(R.id.list_wine_year);
        year.setText(getFragment().getString(R.string.list_wine_year)+" "+Integer.toString(item.getYear()));

        TextView grape = (TextView) v.findViewById(R.id.list_wine_grape);
        grape.setText(getFragment().getString(R.string.list_wine_grape)+" "+item.getGrape());

        RatingBar rating = (RatingBar) v.findViewById(R.id.list_wine_rating);
        rating.setRating(item.getRating());
        LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        return (v);
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public ArrayList<Wine> getItens() {
        return itens;
    }

    public void setItens(ArrayList<Wine> itens) {
        this.itens = itens;
    }

}
