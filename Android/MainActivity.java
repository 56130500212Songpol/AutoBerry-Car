

        package com.example.pang.test;

        import android.animation.Animator;
        import android.animation.AnimatorListenerAdapter;
        import android.annotation.TargetApi;
        import android.app.ActionBar;
        import android.content.DialogInterface;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Parcelable;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.widget.Button;
        import android.widget.EditText;
        //import com.jcraft.jsch.*;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.content.Context;

        import java.io.Serializable;
        import java.util.Properties;
        import android.widget.Toast;

        import com.jcraft.jsch.JSch;
        import com.jcraft.jsch.Session;

        /**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mIPView;
    private EditText mPortView;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mLoginButton;
    private Button mCancelButton;
    private View mProgressView;
    private View mRaspberryLoginFormView;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set up the login form.
        mIPView = (EditText) findViewById(R.id.ip);
        mPortView = (EditText) findViewById(R.id.port);
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);





        /*mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mCancelButton = (Button) findViewById(R.id.cancel_button);
        //mCancelButton.setVisibility(View.VISIBLE);
        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthTask = null;
                showProgress(false);
            }
        });

        mRaspberryLoginFormView = findViewById(R.id.raspberry_login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mIPView.setError(null);
        mPortView.setError(null);
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String ip = mIPView.getText().toString();
        int port = 0;
        try {
            port = Integer.parseInt(mPortView.getText().toString());
        }catch (Exception e){

        }
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid ip address.
        if (TextUtils.isEmpty(ip)) {
            mIPView.setError(getString(R.string.error_field_required));
            focusView = mIPView;
            cancel = true;
        } else if (!isIPValid(ip)) {
            mIPView.setError(getString(R.string.error_invalid_ip));
            focusView = mIPView;
            cancel = true;
        }

        // Check for a valid port.
        if (TextUtils.isEmpty(""+port)) {
            mPortView.setError(getString(R.string.error_field_required));
            focusView = mPortView;
            cancel = true;
        }else if(port != (int)port){
            mPortView.setError(getString(R.string.error_invalid_port));
            focusView = mPortView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(ip, port, username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isIPValid(String ip) {
        int count = 0;
        for(int i = 0; i < ip.length(); i++)
        {
            if (ip.charAt(i) == '.'){
                count++;
            }
        }
        return count == 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRaspberryLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRaspberryLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRaspberryLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCancelButton.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCancelButton.setVisibility(show ? View.VISIBLE : View.GONE);
            mRaspberryLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mIP;
        private final int mPort;
        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String ip, int port, String username, String password) {
            mIP = ip;
            mPort = port;
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            /*try {
                // Simulate network access.
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                return false;
            }*/

            try {
                JSch jsch = new JSch();
                session = jsch.getSession(mUsername, mIP, mPort);
                session.setPassword(mPassword);
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect(10000);
            }catch (final Exception e) {

            }

            // TODO: register the new account here.
            return session.isConnected();
        }

         @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //finish();
                Intent intent = new Intent(MainActivity.this, ManualMode.class);
                intent.putExtra("IP", mIP);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Connect succesfully ", Toast.LENGTH_SHORT).show();
                //mLoginButton.requestFocus();
                mCancelButton.setVisibility(View.GONE);
                /*AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Cannot Connect");
                alertDialog.setMessage("" + session);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();// use dismiss to cancel alert dialog
                            }
                        });
                alertDialog.show();*/
            } else {
                mLoginButton.setError(getString(R.string.error_field_login));
                //mLoginButton.requestFocus();
                Toast.makeText(getApplicationContext(), "Cannot Connect ", Toast.LENGTH_SHORT).show();
                mCancelButton.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }

}
