package gustavo.mywine.app.util;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import gustavo.mywine.app.R;
import gustavo.mywine.app.model.RSSDataItem;

/**
 * This class is responsible for making the asynchronous request for obtaining a
 * RSS feed.
 */
public class AsyncRSSFeed extends AsyncTask<String,Integer,ArrayList<RSSDataItem>> {

    private Context context;
    private String urlRSS;
    private ArrayList<RSSDataItem> items;


    public AsyncRSSFeed(Context context, String urlRSS){
        this.context = context;
        this.urlRSS = urlRSS;
    }

    private ArrayList<RSSDataItem> processFeedRSS(){

        try {
            URL url = new URL(getUrlRSS());

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            // We will get the XML from an input stream

            InputStream inputStream = getInputStream(url);

            if(inputStream==null){
                return null;
            }

            xpp.setInput(inputStream, "UTF_8");

            boolean insideItem = false;

            setItems(new ArrayList<RSSDataItem>());

            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();

            String title = null;
            String link = null;
            String description = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {

                RSSDataItem item = new RSSDataItem();

                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    }else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem)
                            title = xpp.nextText();
                    }else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideItem)
                            link =  xpp.nextText();
                    }else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (insideItem)
                            description =  xpp.nextText();
                    }
                }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                    insideItem=false;
                }

                if(title!=null && link !=null && description!=null) {
                    item.setItemTitle(title);
                    item.setItemLink(link);
                    item.setItemDescription(description);
                    title = null;
                    link = null;
                    getItems().add(item);
                }

                eventType = xpp.next(); //move to next element
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getItems();
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected ArrayList<RSSDataItem> doInBackground(String... params) {
        return processFeedRSS();
    }

    @Override
    protected void onPreExecute(){
        Toast.makeText(getContext(), getContext().getString(R.string.utils_assync_rss_feed_started_process), Toast.LENGTH_SHORT).show();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUrlRSS() {
        return urlRSS;
    }

    public void setUrlRSS(String urlRSS) {
        this.urlRSS = urlRSS;
    }

    public ArrayList<RSSDataItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<RSSDataItem> items) {
        this.items = items;
    }
}
