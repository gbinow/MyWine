package gustavo.mywine.app.ccu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import gustavo.mywine.app.R;
import gustavo.mywine.app.adapter.ListRSSFeedAdapter;
import gustavo.mywine.app.model.RSS;
import gustavo.mywine.app.model.RSSDataItem;
import gustavo.mywine.app.model.SessionUser;
import gustavo.mywine.app.util.AsyncRSSFeed;
import gustavo.mywine.app.util.Functions;

public class RSSFeedFragment extends Fragment implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private View view;
    private ArrayList<RSSDataItem> rsses;
    private RSS rss;
    private SessionUser global;

    public RSSFeedFragment(RSS rss){

        this.rss= rss;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_rss_feed, container, false);
        this.global = ((SessionUser) getActivity().getApplicationContext());
        start();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.findItem(R.id.rss_action_add).setVisible(false);

        super.onCreateOptionsMenu(menu,inflater);
    }

    private void start(){
        initializeRSSFragment();
    }

    private boolean initializeRSSFragment(){

        AsyncRSSFeed asyncRSSFeed = new AsyncRSSFeed(getActivity().getApplicationContext(),getRss().getUrl());

        try {
            ArrayList<RSSDataItem> itens = asyncRSSFeed.execute("").get();
            if (itens != null) {
                initializeRSSList(itens);
                return true;
            }
        }catch(ExecutionException e){
            e.printStackTrace();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        Toast.makeText(getActivity(), getActivity().getString(R.string.warning_list_wine_without_result_feed_rss), Toast.LENGTH_SHORT).show();

        return false;
    }

    private void initializeRSSList(ArrayList<RSSDataItem> itens){

        final ListView list = (ListView) getView().findViewById(R.id.list_rss);
        setRsses(itens);
        list.setAdapter(new ListRSSFeedAdapter(RSSFeedFragment.this,itens));

        list.setOnItemClickListener(new ListView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {

                Functions.openBrowser(getActivity(), getRsses().get(position).getItemLink());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId= item.getItemId();

        //switch(itemId){

        //}

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

    public ArrayList<RSSDataItem> getRsses() {
        return rsses;
    }

    public void setRsses(ArrayList<RSSDataItem> rsses) {
        this.rsses = rsses;
    }

    public SessionUser getGlobal() {
        return global;
    }

    public void setGlobal(SessionUser global) {
        this.global = global;
    }

    public RSS getRss() {
        return rss;
    }

    public void setRss(RSS rss) {
        this.rss = rss;
    }

}
