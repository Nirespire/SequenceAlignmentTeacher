package com.example.omeed.sequencealigntool;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity {

    public EditText sequence1;
    public EditText sequence2;
    public TextView myText;
    TableLayout tableLayout;

    public String alignment;
    public int value;
    public int match;
    public int mismatch;
    public int gap;
    public int matrix[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sequence1 = (EditText)findViewById(R.id.sequence1);
        sequence2 = (EditText)findViewById(R.id.sequence2);
        //myText = (TextView) findViewById(R.id.sequence1Text);

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        //createMatrix(matrix);
        alignment = "global";



        sequence2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent){
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_DONE) {
                    //myText.setText(sequence1.getText());
                    tableLayout.removeAllViews();

                    if(sequence1.length() > 0 && sequence2.length() > 0) {
                        matrix = new int[sequence1.length()][sequence2.length()];
                        match = 1;
                        mismatch = -1;
                        gap = -5;
                        buildTable(sequence1.length(), sequence2.length());
                        displayTable(sequence1.length() + 1, sequence2.length() + 1);
                    }

                    //Hides keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    handled = true;
                }
                return handled;
            }
        });
    }

    private void displayTable(int rows, int cols) {
        //Drawable d = getResources().getDrawable(R.drawable.arrow);


        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);


            // inner for loop
            for (int j = 0; j < cols; j++) {

                TextView textView = new TextView(this);
                textView.setPadding(4, 4, 4, 4);
                textView.setGravity(Gravity.CENTER);

                if(i == 0 && j == 0) {
                    textView.setText(" ");
                }

                else if (j == 0 && i != 0) {
                    textView.setText(String.valueOf(sequence1.getText().charAt(i-1)).toUpperCase());
                    textView.setTextColor(Color.BLACK);
                }
                else if (i == 0 && j != 0){
                    textView.setText(String.valueOf(sequence2.getText().charAt(j-1)).toUpperCase());
                    textView.setTextColor(Color.BLACK);
                }
                else if (i == 1 && j == 1) {
                    textView.setText("0");
                    textView.setBackgroundColor(Color.BLUE);
                    //textView.setBackground(d);
                }
                else {

                    if (alignment.equals("global")) {
                        textView.setText(Integer.toString(matrix[i-1][j-1]));


                    }
                    else if (alignment.equals("local")) {

                    }
                    else if (alignment.equals("dovetail")) {

                    }
                    else if (alignment.equals("pattern")) {

                    }





                }

                row.addView(textView);
            }

            tableLayout.addView(row);

        }
    }

    public void buildTable(int r, int c) {
        int max = 0;
        for (int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                if(i == 0 && j == 0) {
                    matrix[i][j] = 0;
                }
                else if(i == 0 && j > 0) {
                    matrix[i][j] = matrix[i][j - 1] + gap;
                    Log.i("SOS", Integer.toString(matrix[i][j]));
                }
                else if(i > 0 && j == 0) {
                    matrix[i][j] = matrix[i - 1][j] + gap;
                }
                else {

                    if (alignment.equals("global")) {
                        if(sequence1.getText().charAt(i) == sequence2.getText().charAt(j)) {
                            max = matrix[i - 1][j - 1] + match;
                        }
                        else if(sequence1.getText().charAt(i) != sequence2.getText().charAt(j)) {
                            max = matrix[i - 1][j - 1] + mismatch;
                        }

                        int value1 = matrix[i-1][j] + gap;
                        int value2 = matrix[i][j-1] + gap;

                        if (max < value1) {
                            max = value1;
                        }
                        if (max < value2) {
                            max = value2;
                        }

                        matrix[i][j] = max;

                    }
                    else if (alignment.equals("local")) {

                    }
                    else if (alignment.equals("dovetail")) {

                    }
                    else if (alignment.equals("pattern")) {

                    }





                }

            }
        }
    }





}
