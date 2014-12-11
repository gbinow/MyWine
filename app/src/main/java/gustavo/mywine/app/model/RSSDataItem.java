package gustavo.mywine.app.model;
import android.os.Parcel;
import android.os.Parcelable;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RSSDataItem implements Parcelable{

    private String itemTitle;
    private String itemDescription;
    private String itemLink;

    public RSSDataItem() {

        this.itemTitle="";
        this.itemDescription ="";
        this.itemLink="";
    }

    public RSSDataItem(Parcel in) {

        this.itemTitle=in.readString();
        this.itemDescription =in.readString();
        this.itemLink=in.readString();
    }

    public List convertRSSDataItemListBasicNameValuePair(){

        List data =  new ArrayList();

        data.add(new BasicNameValuePair("itemTitle",getItemTitle()));
        data.add(new BasicNameValuePair("itemDescription",getItemDescription()));
        data.add(new BasicNameValuePair("itemLink",getItemLink()));

        return(data);
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(itemTitle);
        dest.writeString(itemDescription);
        dest.writeString(itemLink);
    }

    public static final Parcelable.Creator<RSSDataItem> CREATOR = new Parcelable.Creator<RSSDataItem>() {

        @Override
        public RSSDataItem createFromParcel(Parcel in) {
            return new RSSDataItem(in);
        }

        @Override
        public RSSDataItem[] newArray(int size) {
            return new RSSDataItem[size];
        }
    };

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }
}
