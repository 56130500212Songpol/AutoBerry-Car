package com.example.pang.test;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.Toast;
import android.graphics.Color;
import android.widget.ArrayAdapter;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class AutoMode extends AppCompatActivity {
    Button bSwitchA, bDisconnectA, bStart;
    String mIP, Des;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_mode);
        // Get reference of widgets from XML layout

        Intent intent = getIntent();
        mIP = intent.getStringExtra("IP");

        bSwitchA = (Button) findViewById(R.id.switchButtonA);
        bSwitchA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AutoMode.this, ManualMode.class);
                intent.putExtra("IP", mIP);
                startActivity(intent);
            }
        });

        bDisconnectA = (Button) findViewById(R.id.disconnectButtonA);
        bDisconnectA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AutoMode.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Initializing a String Array
        String[] Destination = new String[]{
                "Select Destination",
                "A",
                "B",
                "C",
                "D",
                "Home"
        };

        final List<String> destinationList = new ArrayList<>(Arrays.asList(Destination));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, destinationList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;

                } else {
                    return true;
                }
            }


            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                final String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                bStart = (Button) findViewById(R.id.startButton);
                bStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AutoMode.this);
                        alertDialogBuilder.setTitle("Confirmation");
                        alertDialogBuilder
                                .setMessage("Select destination " + selectedItemText)
                                .setCancelable(false)
                                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.cancel();

                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        new RequestTask().execute("http://" + mIP + "/destination.php?destination=" + Des);
                                        final ProgressDialog progressDialog = new ProgressDialog(AutoMode.this, R.style.AppTheme_Dark_Dialog);
                                        progressDialog.setIndeterminate(true);
                                        progressDialog.setMessage("Authenticating...");
                                        progressDialog.show();

                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show itd
                        alertDialog.show();


                    }
                });



                if (position > 0) {

                    if (position == 1) {
                        Des = "A";
                    }
                    if (position == 2) {
                        Des = "B";
                    }
                    if (position == 3) {
                        Des = "C";
                    }
                    if (position == 4) {
                        Des = "D";
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
}






