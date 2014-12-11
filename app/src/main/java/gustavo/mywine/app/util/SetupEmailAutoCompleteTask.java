package gustavo.mywine.app.util;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
 * the email text field with results on the main UI thread.
 */

public class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> implements LoaderManager.LoaderCallbacks<Cursor> {

    private  ContentResolver cr;
    private AutoCompleteTextView emailAutoComplete;
    private Activity activity;
    private LoaderManager loaderManager;

    public SetupEmailAutoCompleteTask(AutoCompleteTextView emailAutoComplete, Activity activity, ContentResolver cr, LoaderManager loaderManager){
        this.emailAutoComplete = emailAutoComplete;
        this.cr = cr;
        this.activity = activity;
        this.loaderManager = loaderManager;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        ArrayList<String> emailAddressCollection = new ArrayList<String>();

        // Get all emails from the user's contacts and copy them to a list.
        Cursor emailCur = getCr().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
        while (emailCur.moveToNext()) {
            String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            emailAddressCollection.add(email);
        }
        emailCur.close();

        return emailAddressCollection;
    }

    @Override
    protected void onPostExecute(List<String> emailAddressCollection) {
        addEmailsToAutoComplete(emailAddressCollection);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        SetupEmailAutoCompleteTask.this.addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /*suggests emails contact list*/
    public void populateAutoComplete() {
        if (Build.VERSION.SDK_INT >= 14) {
            // Use ContactsContract.Profile (API 14+)
            getLoaderManager().initLoader(0, null, this);
        } else if (Build.VERSION.SDK_INT >= 8) {
            // Use AccountManager (API 8+)
            SetupEmailAutoCompleteTask.this.execute(null, null);
        }
    }

    public void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        getEmailAutoComplete().setAdapter(adapter);
    }

    /*Gets an Sets*/
    public ContentResolver getCr() {
        return cr;
    }

    public void setCr(ContentResolver cr) {
        this.cr = cr;
    }

    public AutoCompleteTextView getEmailAutoComplete() {
        return emailAutoComplete;
    }

    public void setEmailAutoComplete(AutoCompleteTextView emailAutoComplete) {
        this.emailAutoComplete = emailAutoComplete;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public LoaderManager getLoaderManager() {
        return loaderManager;
    }

    public void setLoaderManager(LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
    }
}