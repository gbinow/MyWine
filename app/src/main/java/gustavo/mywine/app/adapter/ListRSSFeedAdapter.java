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
import gustavo.mywine.app.ccu.RSSFeedFragment;
import gustavo.mywine.app.model.RSSDataItem;

public class ListRSSFeedAdapter extends BaseAdapter {

    private Fragment fragment;
    private ArrayList<RSSDataItem> itens;
    private Activity activity;

    private static final int LIMIT_SIZE_TITLE = 40;
    private static final int LIMIT_SIZE_DESCRIPTION = 95;

    public ListRSSFeedAdapter(RSSFeedFragment fragment, ArrayList<RSSDataItem> itens){
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

        RSSDataItem item = (RSSDataItem) getItens().get(position);

        //Responsible for communication between XML, the application screen and the Adapter ListRSS class.
        LayoutInflater inflater;
        inflater = (LayoutInflater) getFragment().getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_rss_feed, null);

        TextView title = (TextView) v.findViewById(R.id.list_rss_feed_title);

        if(item.getItemDescription().length()>=LIMIT_SIZE_TITLE) {
            title.setText(item.getItemTitle().substring(0, LIMIT_SIZE_TITLE));
        }else{
            title.setText(item.getItemTitle());
        }

        TextView description = (TextView) v.findViewById(R.id.list_rss_feed_description);
        if(item.getItemDescription().length()>=LIMIT_SIZE_DESCRIPTION) {
            description.setText(item.getItemDescription().substring(0, LIMIT_SIZE_DESCRIPTION));
        }else{
            description.setText(item.getItemDescription());
        }
        return (v);
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public ArrayList<RSSDataItem> getItens() {
        return itens;
    }

    public void setItens(ArrayList<RSSDataItem> itens) {
        this.itens = itens;
    }

}



