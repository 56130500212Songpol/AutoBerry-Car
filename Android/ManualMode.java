package com.example.pang.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jcraft.jsch.*;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;

public class ManualMode extends AppCompatActivity implements View.OnClickListener {

    Button bSwitchM, bDisconnectM;
    ImageButton bForward, bBackward, bLeft, bRight;
    String mIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Intent intent = getIntent();
        mIP = intent.getStringExtra("IP");

        bSwitchM = (Button) findViewById(R.id.switchButtonM);
        bSwitchM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManualMode.this, AutoMode.class);
                startActivity(intent);
            }
        });

        bDisconnectM = (Button) findViewById(R.id.disconnectButtonM);
        bDisconnectM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManualMode.this, MainActivity.class);
                startActivity(intent);
            }
        });

        bForward = (ImageButton) findViewById(R.id.forwardButton);
        bForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
            /* button is Forward */
                    new RequestTask().execute("http://"+mIP+"/gpio.php?pin=7&status=1");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    new RequestTask().execute("http://"+mIP+"/gpio.php?pin=7&status=0");
                }
                return true;
            }
        });

        bBackward = (ImageButton) findViewById(R.id.backwardButton);
        bBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
            /* button is Backward */
                    new RequestTask().execute("http://"+mIP+"/gpio.php?pin=0&status=1");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    new RequestTask().execute("http://"+mIP+"/gpio.php?pin=0&status=0");
                }
                return true;
            }
        });

        bRight = (ImageButton) findViewById(R.id.rightButton);
        bRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
            /* button is Right */
                    new RequestTask().execute("http://"+mIP+"/gpio.php?pin=2&status=1");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    new RequestTask().execute("http://"+mIP+"/gpio.php?pin=2&status=0");
                }
                return true;
            }
        });

        bLeft = (ImageButton) findViewById(R.id.leftButton);
        bLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
            /* button is Left */
                    new RequestTask().execute("http://"+mIP+"/gpio.php?pin=3&status=1");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    new RequestTask().execute("http://"+mIP+"/gpio.php?pin=3&status=0");
                }
                return true;
            }
        });

    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }
    }

    @Override
    public void onClick(View v) {
        //startActivity(new Intent(this, ManualMode.class));
    }


}
