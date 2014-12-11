package gustavo.mywine.app.ccu;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import gustavo.mywine.app.R;
import gustavo.mywine.app.apl.RSSApl;
import gustavo.mywine.app.apl.UserApl;
import gustavo.mywine.app.apl.WineApl;
import gustavo.mywine.app.model.RSS;
import gustavo.mywine.app.model.SessionUser;
import gustavo.mywine.app.model.User;
import gustavo.mywine.app.model.Wine;
import gustavo.mywine.app.util.Functions;
import gustavo.mywine.app.util.SetupEmailAutoCompleteTask;


public class LoginActivity extends Activity {

    // UI references.
    private AutoCompleteTextView emailAutoComplete;
    private EditText passwordEditText;
    private View loginFormView;
    private Button signInButton;
    private Button registerButton;
    private SetupEmailAutoCompleteTask setupEmailAutoCompleteTask;
    private SessionUser global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.global = ((SessionUser) getApplicationContext());

        this.emailAutoComplete = (AutoCompleteTextView) findViewById(R.id.email);
        this.passwordEditText = (EditText) findViewById(R.id.password);
        this.signInButton = (Button) findViewById(R.id.sign_in_button);
        this.registerButton = (Button) findViewById(R.id.register_button);
        this.loginFormView = findViewById(R.id.login_form);

        this.setupEmailAutoCompleteTask = new SetupEmailAutoCompleteTask(getEmailAutoComplete(),LoginActivity.this,getContentResolver(),getLoaderManager());

        start();
    }

    private void start(){

        initializeEvents();
    }

    private void initializeEvents(){

        populateAutoComplete();

        getPasswordEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        getSignInButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        getRegisterButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerLogin();
            }
        });


    }

    /*suggests emails contact list*/
    private void populateAutoComplete() {
        getSetupEmailAutoCompleteTask().populateAutoComplete();
    }

    /**
     * Validade form login and authentication.
     */
    public void attemptLogin() {

        // Reset errors.
        emailAutoComplete.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String email = emailAutoComplete.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailAutoComplete.setError(getString(R.string.error_field_required));
            focusView = emailAutoComplete;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailAutoComplete.setError(getString(R.string.error_invalid_email));
            focusView = emailAutoComplete;
            cancel = true;
        }

        if(cancel){
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        }


        UserApl userApl = new UserApl(getApplicationContext());
        User user = userApl.getObject(email,password);

        if (loginAuthentication(user)) {
            //Finish current activity
            finish();
            Intent i = new Intent();
            i.setClass(LoginActivity.this, MainMenuActivity.class);
            startActivity(i);
        }else{
            Functions.alert(getApplicationContext(),getApplicationContext().getString(R.string.error_invalid_authentication));
        }
    }

    public void registerLogin(){

        // Reset errors.
        emailAutoComplete.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        final String email = emailAutoComplete.getText().toString();
        final String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailAutoComplete.setError(getString(R.string.error_field_required));
            focusView = emailAutoComplete;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailAutoComplete.setError(getString(R.string.error_invalid_email));
            focusView = emailAutoComplete;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        }

        final UserApl userApl = new UserApl(getApplicationContext());
        final User user = userApl.getObject(email,null);

        if(user==null){

            final User newUser = new User();
            newUser.setLogin(email);
            newUser.setPassword(password);

            String[] text_buttons = new String[2];
            text_buttons[0]="Cancel";
            text_buttons[1]="Yes";

            Functions.alertDialog(LoginActivity.this, "New user", "Confirms creation of user login", text_buttons, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                },
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        userApl.insertObject(newUser);

                        createInitialData(userApl.getObject(email,password));
                        loginAuthentication(userApl.getObject(email,password));

                        //Finish current activity
                        finish();
                        Intent i = new Intent();
                        i.setClass(LoginActivity.this, MainMenuActivity.class);
                        startActivity(i);

                        dialog.cancel();
                    }
                },null
            );

        }else{

            emailAutoComplete.setError(getString(R.string.error_exist_user));
            focusView = emailAutoComplete;
            focusView.requestFocus();
        }

    }

    /*Login authentication*/
    private boolean loginAuthentication(User user){

        if(user!=null){
            getGlobal().setUserId(user.getId());
            return true;
        }

        return false;

    }

    public void createInitialData(User user){

        int userId = user.getId();

        RSSApl rssApl = new RSSApl(getApplicationContext());

        RSS rss = new RSS();

        rss.setName("Wine Spectator");
        rss.setUrl("http://www.winespectator.com/rss/rss?t=news");
        rss.setUserId(userId);
        rssApl.insertObject(rss);

        rss.setName("Great Capitals");
        rss.setUrl(" http://greatwinecapitals.com/feeds/news/all/rss.xml");
        rss.setUserId(userId);
        rssApl.insertObject(rss);

        WineApl wineApl = new WineApl(getApplicationContext());

        Wine wine = new Wine();

        wine.setLabel("Royal Porto");
        wine.setYear(2001);
        wine.setGrape("Malbec");
        wine.setRating(2);
        wine.setObservations("Bland");
        wine.setUserId(userId);
        wineApl.insertObject(wine);


        wine.setLabel("Toro Loco");
        wine.setYear(2013);
        wine.setGrape("Tempranillo");
        wine.setRating(3);
        wine.setObservations("Tinto");
        wine.setUserId(userId);
        wineApl.insertObject(wine);

        wine.setLabel("Trapezio");
        wine.setYear(1999);
        wine.setGrape("Tempranillo");
        wine.setRating(0);
        wine.setObservations("Tinto");
        wine.setUserId(userId);
        wineApl.insertObject(wine);

        wine.setLabel("Apothic");
        wine.setYear(2010);
        wine.setGrape("Colombard");
        wine.setRating(5);
        wine.setObservations("Bland");
        wine.setUserId(userId);
        wineApl.insertObject(wine);

        wine.setLabel("Fantinel");
        wine.setYear(1995);
        wine.setGrape("Malbec");
        wine.setRating(3);
        wine.setObservations("Tinto");
        wine.setUserId(userId);
        wineApl.insertObject(wine);

        wine.setLabel("Cantinho da Serra");
        wine.setYear(2009);
        wine.setGrape("Tempranillo");
        wine.setRating(0);
        wine.setObservations("Bland");
        wine.setUserId(userId);
        wineApl.insertObject(wine);

        wine.setLabel("Galiotto");
        wine.setYear(2011);
        wine.setGrape("Colombard");
        wine.setRating(5);
        wine.setObservations("Tinto");
        wine.setUserId(userId);
        wineApl.insertObject(wine);

    }

    /*Validation form */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    /* Gets and Sets*/

    public AutoCompleteTextView getEmailAutoComplete() {
        return emailAutoComplete;
    }

    public void setEmailAutoComplete(AutoCompleteTextView emailAutoComplete) {
        this.emailAutoComplete = emailAutoComplete;
    }

    public EditText getPasswordEditText() {
        return passwordEditText;
    }

    public void setPasswordEditText(EditText passwordEditText) {
        this.passwordEditText = passwordEditText;
    }

    public View getLoginFormView() {
        return loginFormView;
    }

    public void setLoginFormView(View loginFormView) {
        this.loginFormView = loginFormView;
    }

    public Button getSignInButton() {
        return signInButton;
    }

    public void setSignInButton(Button emailSignInButton) {
        this.signInButton = emailSignInButton;
    }

    public SetupEmailAutoCompleteTask getSetupEmailAutoCompleteTask() {
        return setupEmailAutoCompleteTask;
    }

    public void setSetupEmailAutoCompleteTask(SetupEmailAutoCompleteTask setupEmailAutoCompleteTask) {
        this.setupEmailAutoCompleteTask = setupEmailAutoCompleteTask;
    }

    public SessionUser getGlobal() {
        return global;
    }

    public void setGlobal(SessionUser global) {
        this.global = global;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(Button registerButton) {
        this.registerButton = registerButton;
    }
}



