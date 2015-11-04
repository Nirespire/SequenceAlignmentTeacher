package com.example.omeed.sequencealigntool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class About extends Activity {
    final Context context = this;

    boolean alignmentMethod = false;
    boolean scoringMethod = false;
    boolean about = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_about);

        Button button = (Button)findViewById(R.id.button);                     //Database button


        button.setOnClickListener(new View.OnClickListener() {                             //Switches to Database page on button press
            @Override
            public void onClick(View v) {                                                 //Activates on button press
                alignmentMethod = true;
                scoringMethod = false;
                about = false;
                initialize();
            }
        });

        Button button2 = (Button)findViewById(R.id.button2);                     //Database button


        button2.setOnClickListener(new View.OnClickListener() {                             //Switches to Database page on button press
            @Override
            public void onClick(View v) {                                                 //Activates on button press
                scoringMethod = true;
                alignmentMethod = false;
                about = false;
                initialize();
            }
        });

        Button button3 = (Button)findViewById(R.id.button3);                     //Database button


        button3.setOnClickListener(new View.OnClickListener() {                             //Switches to Database page on button press
            @Override
            public void onClick(View v) {                                                 //Activates on button press
                about = true;
                scoringMethod = false;
                alignmentMethod = false;
                initialize();
            }
        });


    }


    public void initialize() {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View promptView = layoutInflater.inflate(R.layout.about, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(promptView);

        if(alignmentMethod) {
            TextView foo = (TextView) promptView.findViewById(R.id.textView);
            foo.setText(Html.fromHtml(getString(R.string.alignment_method)));
        }
        else if (scoringMethod){
            TextView foo2 = (TextView) promptView.findViewById(R.id.textView2);
            foo2.setText(Html.fromHtml(getString(R.string.scoring_method)));
        }
        else {
            TextView foo3 = (TextView) promptView.findViewById(R.id.textView3);
            foo3.setText(Html.fromHtml(getString(R.string.about)));
        }

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result

                    }
                });
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });



        // create an alert dialog
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }
}
