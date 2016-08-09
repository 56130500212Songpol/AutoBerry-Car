package com.example.pang.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jcraft.jsch.*;

public class ManualMode extends AppCompatActivity implements View.OnClickListener {

    Button bSwitchM,bDisconnectM;
    ImageButton mArrowButton;
    EditText etName, etUsername, etPassword;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

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

/*        if (session.isConnected()){
            AlertDialog alertDialog = new AlertDialog.Builder(ManualMode.this).create();
            alertDialog.setTitle("Cannot Connect");
            alertDialog.setMessage(getString(R.string.error_field_login));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();// use dismiss to cancel alert dialog
                        }
                    });
            alertDialog.show();
        }*/
    }

    @Override
    public void onClick(View v) {
        //startActivity(new Intent(this, ManualMode.class));
        }


}