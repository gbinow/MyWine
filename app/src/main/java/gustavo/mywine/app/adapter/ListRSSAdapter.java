package gustavo.mywine.app.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gustavo.mywine.app.R;
import gustavo.mywine.app.ccu.RSSFragment;
import gustavo.mywine.app.model.RSS;


public class ListRSSAdapter extends BaseAdapter {

    private Fragment fragment;
    private ArrayList<RSS> itens;
    private Activity activity;


    public ListRSSAdapter(RSSFragment fragment, ArrayList<RSS> itens){
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

        RSS item = (RSS) getItens().get(position);

        //Responsible for communication between XML, the application screen and the Adapter ListRSS class.
        LayoutInflater inflater;
        inflater = (LayoutInflater) getFragment().getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_rss, null);

        TextView name = (TextView) v.findViewById(R.id.list_rss_name);
        TextView url = (TextView) v.findViewById(R.id.list_rss_url);

        name.setText(item.getName());
        url.setText(item.getUrl());

        return (v);
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public ArrayList<RSS> getItens() {
        return itens;
    }

    public void setItens(ArrayList<RSS> itens) {
        this.itens = itens;
    }

}



