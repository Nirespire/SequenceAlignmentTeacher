package com.example.omeed.sequencealigntool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends Activity  {

    public EditText sequence1;
    public EditText sequence2;

    public EditText match;
    public EditText mismatch;
    public EditText gap;
    public Spinner alignmentMethod;
    public Spinner scoringMethod;
    public EditText s1;
    public EditText s2;
    public EditText align;

    TableLayout tableLayout;
    TableLayout tl;

    public ArrayAdapter<String> dataAdapter;
    public ArrayAdapter<String> dataAdapter2;

    public String alignmentValue;
    public String scoringValue;
    public int matchValue;
    public int mismatchValue;
    public int gapValue;
    public int matrix[][];
    final Context context = this;

    public String traceback[][];
    public ArrayList<String> globalTraceback;
    public List<String> list;
    public List<String> list2;
    public int paths[][];
    public ArrayList<String> alignment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setTitle("Sequence Align");
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        s1 = (EditText) findViewById(R.id.s1);
        s2 = (EditText) findViewById(R.id.s2);
        align = (EditText) findViewById(R.id.align);
        s1.setKeyListener(null);
        s2.setKeyListener(null);
        align.setKeyListener(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cart:
                initialize();
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(this, About.class);
                this.startActivity(intent);
                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void displayTable(int rows, int cols) {
        String temp2 = "";
        int row;
        int col;
        int counter2 = 1;
        int counter3;
        int counter4 = 0;

        if(sequence1.length() == sequence2.length()) {
            counter3 = 1;
        }
        else {
            counter3 = 0;
        }

        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow r = new TableRow(this);
            r.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1.0f));

            // inner for loop
            for (int j = 0; j < cols; j++) {

                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                textView.setTextSize(12);
                textView.setPadding(5, 5, 5, 5);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.RED);

                if (alignmentValue.equals("Global")) {


                    if(counter2 < globalTraceback.size()){
                        temp2 = globalTraceback.get(counter2);
                        String[] tokens = temp2.split(",");
                        row = Integer.parseInt(tokens[0]);
                        col = Integer.parseInt(tokens[1]);
                        if ((i == row + 1) && (j == col + 1)) {
                            textView.setBackgroundColor(Color.rgb(255,192,203));
                            counter2++;
                        }
                    }
                    if(i == 0 && j == 0) {
                        textView.setText(" ");
                    }
                    else if (i == 1 && j == 0) {
                        textView.setText(" ");
                    }
                    else if (i == 0 && j == 1) {
                        textView.setText(" ");
                    }
                    else if (j == 0 && i > 1) {
                        textView.setText("\n" + String.valueOf(sequence1.getText().charAt(i-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 0 && j > 1){
                        textView.setText(String.valueOf(sequence2.getText().charAt(j-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 1 && j == 1) {
                        textView.setText("\n" + " " + "0");
                        textView.setBackgroundColor(Color.rgb(255, 192, 203));
                        //textView.setText("⇑" + "\n" + "⇖ 0");
                        //textView.setText("0" +"\n" + "⇑ ⇘");
                        //textView.setText("⇐ ⇖ ⇑");
                        //textView.setBackgroundColor(Color.rgb(255, 192, 203));
                        //textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.one, 0);
                    }
                    else if(i > 0 && j > 0) {

                        //textView.setText("⇐"+ "\n" + "⇖ 0");

                        if(paths[i-1][j-1] == 1) {
                            //textView.setText("⇑" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇑" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇑","<font color='black'>⇑ <br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 2) {
                            //textView.setText ("\n" + "⇐ "+ Integer.toString(matrix[i-1][j-1]));
                            String text = ("\n" + "⇐ "+ Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇐","<font color='black'><br>⇐ </font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 3) {
                            //textView.setText("⇖" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖","<font color='black'>⇖<br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 4) {
                            //textView.setText("⇖"+ "\n" + "⇐ " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖"+ "\n" + "⇐ " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖","<font color='black'>⇖<br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                            text = htmlText;
                            String htmlText2 = text.replace("⇐","<font color='black'>⇐</font>");
                            textView.setText(Html.fromHtml(htmlText2));
                        }
                        else if(paths[i-1][j-1] == 5) {
                            //textView.setText("⇖ ⇑"+ "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖ ⇑"+ "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖ ⇑","<font color='black'>⇖ ⇑ <br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else {
                            textView.setText("\n" + Integer.toString(matrix[i-1][j-1]));
                        }

                        if(i == sequence1.length() + 1 && j == sequence2.length() + 1){
                            textView.setBackgroundColor(Color.rgb(255,192,203));
                        }

                        if(counter2 < globalTraceback.size()){
                            temp2 = globalTraceback.get(counter2);
                            String[] tokens = temp2.split(",");

                            if(!temp2.equals("0")) {
                                row = Integer.parseInt(tokens[0]);
                                col = Integer.parseInt(tokens[1]);

                                if ((i == row + 1) && (j == col + 1)) {
                                    textView.setBackgroundColor(Color.rgb(255,192,203));
                                    counter2++;
                                }
                            }
                        }
                    }
                }

                else if (alignmentValue.equals("Local")) {

                    if(counter4 < globalTraceback.size()){
                        temp2 = globalTraceback.get(counter4);
                        String[] tokens = temp2.split(",");
                        row = Integer.parseInt(tokens[0]);
                        col = Integer.parseInt(tokens[1]);
                        if ((i == row + 1) && (j == col + 1)) {
                            textView.setBackgroundColor(Color.rgb(255,192,203));
                            counter4++;
                        }
                    }
                    if(i == 0 && j == 0) {
                        textView.setText(" ");
                    }
                    else if (i == 1 && j == 0) {
                        textView.setText(" ");
                    }
                    else if (i == 0 && j == 1) {
                        textView.setText(" ");
                    }
                    else if (j == 0 && i > 1) {
                        textView.setText("\n" + String.valueOf(sequence1.getText().charAt(i-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 0 && j > 1){
                        textView.setText(String.valueOf(sequence2.getText().charAt(j-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 1 && j == 1) {
                        textView.setText("\n" + " " + "0");
                    }
                    else if(i > 0 && j > 0) {
                        if(paths[i-1][j-1] == 1) {
                            //textView.setText("⇑" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇑" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇑","<font color='black'>⇑ <br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 2) {
                            //textView.setText ("\n" + "⇐ "+ Integer.toString(matrix[i-1][j-1]));
                            String text = ("\n" + "⇐ "+ Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇐","<font color='black'><br>⇐ </font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 3) {
                            //textView.setText("⇖" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖","<font color='black'>⇖<br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 4) {
                            //textView.setText("⇖"+ "\n" + "⇐ " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖"+ "\n" + "⇐ " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖","<font color='black'>⇖<br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                            text = htmlText;
                            String htmlText2 = text.replace("⇐","<font color='black'>⇐</font>");
                            textView.setText(Html.fromHtml(htmlText2));
                        }
                        else if(paths[i-1][j-1] == 5) {
                            //textView.setText("⇖ ⇑"+ "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖ ⇑"+ "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖ ⇑","<font color='black'>⇖ ⇑ <br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else {
                            textView.setText("\n" + Integer.toString(matrix[i-1][j-1]));
                        }
                    }
                }

                else if (alignmentValue.equals("Dovetail")) {
                    if(counter4 < globalTraceback.size()){
                        temp2 = globalTraceback.get(counter4);
                        String[] tokens = temp2.split(",");
                        row = Integer.parseInt(tokens[0]);
                        col = Integer.parseInt(tokens[1]);
                        if ((i == row + 1) && (j == col + 1)) {
                            textView.setBackgroundColor(Color.rgb(255,192,203));
                            counter4++;
                        }
                    }
                    if(i == 0 && j == 0) {
                        textView.setText(" ");
                    }
                    else if (i == 1 && j == 0) {
                        textView.setText(" ");
                    }
                    else if (i == 0 && j == 1) {
                        textView.setText(" ");
                    }
                    else if (j == 0 && i > 1) {
                        textView.setText("\n" + String.valueOf(sequence1.getText().charAt(i-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 0 && j > 1){
                        textView.setText(String.valueOf(sequence2.getText().charAt(j-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 1 && j == 1) {
                        textView.setText("\n" + " " + "0");
                    }
                    else if(i > 0 && j > 0) {
                        if(paths[i-1][j-1] == 1) {
                            //textView.setText("⇑" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇑" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇑","<font color='black'>⇑ <br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 2) {
                            //textView.setText ("\n" + "⇐ "+ Integer.toString(matrix[i-1][j-1]));
                            String text = ("\n" + "⇐ "+ Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇐","<font color='black'><br>⇐ </font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 3) {
                            //textView.setText("⇖" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖","<font color='black'>⇖<br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 4) {
                            //textView.setText("⇖"+ "\n" + "⇐ " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖"+ "\n" + "⇐ " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖","<font color='black'>⇖<br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                            text = htmlText;
                            String htmlText2 = text.replace("⇐","<font color='black'>⇐</font>");
                            textView.setText(Html.fromHtml(htmlText2));
                        }
                        else if(paths[i-1][j-1] == 5) {
                            //textView.setText("⇖ ⇑"+ "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖ ⇑"+ "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖ ⇑","<font color='black'>⇖ ⇑ <br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else {
                            textView.setText("\n" + Integer.toString(matrix[i-1][j-1]));
                        }
                    }
                }

                else if (alignmentValue.equals("Pattern")) {
                    if(counter3 < globalTraceback.size()){
                        temp2 = globalTraceback.get(counter3);
                        String[] tokens = temp2.split(",");
                        row = Integer.parseInt(tokens[0]);
                        col = Integer.parseInt(tokens[1]);
                        if ((i == row + 1) && (j == col + 1)) {
                            textView.setBackgroundColor(Color.rgb(255,192,203));
                            counter3++;
                        }
                    }
                    if(i == 0 && j == 0) {
                        textView.setText(" ");
                    }
                    else if (i == 1 && j == 0) {
                        textView.setText(" ");
                    }
                    else if (i == 0 && j == 1) {
                        textView.setText(" ");
                    }
                    else if (j == 0 && i > 1) {
                        textView.setText("\n" + String.valueOf(sequence1.getText().charAt(i-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 0 && j > 1){
                        textView.setText(String.valueOf(sequence2.getText().charAt(j-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 1 && j == 1) {
                        textView.setText("\n" + " " + "0");
                    }
                    else if(i > 0 && j > 0) {
                        if(paths[i-1][j-1] == 1) {
                            //textView.setText("⇑" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇑" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇑","<font color='black'>⇑ <br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 2) {
                            //textView.setText ("\n" + "⇐ "+ Integer.toString(matrix[i-1][j-1]));
                            String text = ("\n" + "⇐ "+ Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇐","<font color='black'><br>⇐ </font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 3) {
                            //textView.setText("⇖" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖" + "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖","<font color='black'>⇖<br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else if(paths[i-1][j-1] == 4) {
                            //textView.setText("⇖"+ "\n" + "⇐ " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖"+ "\n" + "⇐ " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖","<font color='black'>⇖<br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                            text = htmlText;
                            String htmlText2 = text.replace("⇐","<font color='black'>⇐</font>");
                            textView.setText(Html.fromHtml(htmlText2));
                        }
                        else if(paths[i-1][j-1] == 5) {
                            //textView.setText("⇖ ⇑"+ "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String text = ("⇖ ⇑"+ "\n" + " " + Integer.toString(matrix[i-1][j-1]));
                            String htmlText = text.replace("⇖ ⇑","<font color='black'>⇖ ⇑ <br></font>");
                            textView.setText(Html.fromHtml(htmlText));
                        }
                        else {
                            textView.setText("\n" + Integer.toString(matrix[i-1][j-1]));
                        }

                        if (counter3 < globalTraceback.size()) {
                            temp2 = globalTraceback.get(counter3);
                            String[] tokens = temp2.split(",");

                            if (!temp2.equals("0")) {
                                row = Integer.parseInt(tokens[0]);
                                col = Integer.parseInt(tokens[1]);

                                if ((i == row + 1) && (j == col + 1)) {
                                    textView.setBackgroundColor(Color.rgb(255, 192, 203));
                                    counter3++;
                                }
                            }
                        }
                    }
                }

                r.addView(textView);
            }
            tableLayout.addView(r);
        }
    }

    public void buildTable(int r, int c) {
        boolean isMatch = false;
        int min = 100000;
        int max = 0;
        if(scoringValue == "Score") {
            if (alignmentValue.equals("Global")) {
                for (int i = 0; i < r; i++) {
                    for (int j = 0; j < c; j++) {
                        if (i == 0 && j == 0) {
                            matrix[i][j] = 0;
                            traceback[i][j] = "0";
                            paths[i][j] = 0;
                        }
                        else if (i == 0 && j > 0) {
                            matrix[i][j] = matrix[i][j - 1] + gapValue;
                            traceback[i][j] = "0";
                            paths[i][j] = 2;

                        }
                        else if (i > 0 && j == 0) {
                            matrix[i][j] = matrix[i - 1][j] + gapValue;
                            traceback[i][j] = "0";
                            paths[i][j] = 1;
                        }
                        else {
                            if (sequence1.getText().charAt(i - 1) == sequence2.getText().charAt(j - 1)) {
                                isMatch = true;
                                max = matrix[i - 1][j - 1] + matchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            }
                            else if (sequence1.getText().charAt(i - 1) != sequence2.getText().charAt(j - 1)) {
                                isMatch = false;
                                max = matrix[i - 1][j - 1] + mismatchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            }

                            int value1 = matrix[i - 1][j] + gapValue;               //⇑
                            int value2 = matrix[i][j - 1] + gapValue;               //⇐

                            if (max < value1) {
                                max = value1;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j);
                            }
                            if (max < value2) {
                                max = value2;
                                traceback[i][j] = Integer.toString(i) + "," + Integer.toString(j - 1);
                            }
                            matrix[i][j] = max;

                            if(isMatch) {
                                if((matrix[i - 1][j - 1] + matchValue == value1)) {
                                    paths[i][j] = 5; // ⇑ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue == value2))  {
                                    paths[i][j] = 4; // ⇐ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue > value1)  && (matrix[i - 1][j - 1] + matchValue > value2)) {
                                    paths[i][j] = 3; // ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue < value2) && (value2 > value1 )) {
                                    paths[i][j] = 2; // ⇐
                                }
                                else if((matrix[i - 1][j - 1] + matchValue < value1) && (value1 > value2 )) {
                                    paths[i][j] = 1; // ⇑
                                }
                                else {
                                    paths[i][j] = 0;
                                }
                            }
                            else if(!isMatch) {
                                if((matrix[i - 1][j - 1] + mismatchValue == value1)) {
                                    paths[i][j] = 5; // ⇑ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue == value2))  {
                                    paths[i][j] = 4; // ⇐ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue > value1)  && (matrix[i - 1][j - 1] + mismatchValue > value2)) {
                                    paths[i][j] = 3; // ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue < value2) && (value2 > value1 )) {
                                    paths[i][j] = 2; // ⇐
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue < value1) && (value1 > value2 )) {
                                    paths[i][j] = 1; // ⇑
                                }
                                else {
                                    paths[i][j] = 0;
                                }
                            }
                        }
                    }
                }
            }
            else if (alignmentValue.equals("Local")) {
                int max2;
                for (int i = 0; i < r; i++) {
                    for (int j = 0; j < c; j++) {
                        if (i == 0 && j == 0) {
                            matrix[i][j] = 0;
                            traceback[i][j] = "0";
                            paths[i][j] = 0;
                        }
                        else if (i == 0 && j > 0) {
                            max2 = matrix[i][j - 1] + gapValue;
                            if(max2 < 0) {
                                matrix[i][j] = 0;
                            }
                            else {
                                matrix[i][j] = max2;
                            }
                            traceback[i][j] = "0";
                            paths[i][j] = 0;
                        }
                        else if (i > 0 && j == 0) {
                            max2 = matrix[i - 1][j] + gapValue;
                            if(max2 < 0) {
                                matrix[i][j] = 0;
                            }
                            else {
                                matrix[i][j] = max2;
                            }
                            traceback[i][j] = "0";
                            paths[i][j] = 0;
                        }
                        else {

                            if (sequence1.getText().charAt(i - 1) == sequence2.getText().charAt(j - 1)) {
                                isMatch = true;
                                max = matrix[i - 1][j - 1] + matchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            }
                            else if (sequence1.getText().charAt(i - 1) != sequence2.getText().charAt(j - 1)) {
                                isMatch = false;
                                max = matrix[i - 1][j - 1] + mismatchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            }

                            int value1 = matrix[i - 1][j] + gapValue;
                            int value2 = matrix[i][j - 1] + gapValue;

                            if (max < value1) {
                                max = value1;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j);
                            }
                            if (max < value2) {
                                max = value2;
                                traceback[i][j] = Integer.toString(i) + "," + Integer.toString(j - 1);
                            }

                            if (max < 0) {
                                max = 0;
                            }
                            matrix[i][j] = max;

                            if(isMatch) {
                                if((matrix[i - 1][j - 1] + matchValue == value1)) {
                                    paths[i][j] = 5; // ⇑ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue == value2))  {
                                    paths[i][j] = 4; // ⇐ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue > value1)  && (matrix[i - 1][j - 1] + matchValue > value2)) {
                                    paths[i][j] = 3; // ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue < value2) && (value2 > value1 )) {
                                    paths[i][j] = 2; // ⇐
                                }
                                else if((matrix[i - 1][j - 1] + matchValue < value1) && (value1 > value2 )) {
                                    paths[i][j] = 1; // ⇑
                                }
                                else {
                                    paths[i][j] = 0;
                                }
                            }
                            else if((!isMatch && matrix[i-1][j-1]!=0) || ((!isMatch && matrix[i-1][j]!=0) && (matrix[i][j] + matrix[i-1][j] >= Math.abs(gapValue))) || ((!isMatch && matrix[i][j-1]!=0) && (matrix[i][j] + matrix[i][j-1] >= Math.abs(gapValue)))) {
                                if((matrix[i - 1][j - 1] + mismatchValue == value1)) {
                                    paths[i][j] = 5; // ⇑ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue == value2))  {
                                    paths[i][j] = 4; // ⇐ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue > value1)  && (matrix[i - 1][j - 1] + mismatchValue > value2)) {
                                    paths[i][j] = 3; // ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue < value2) && (value2 > value1 )) {
                                    paths[i][j] = 2; // ⇐
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue < value1) && (value1 > value2 )) {
                                    paths[i][j] = 1; // ⇑
                                }
                                else {
                                    paths[i][j] = 0;
                                }
                            }
                        }
                    }
                }
            }
            else if (alignmentValue.equals("Dovetail")) {
                int max2;
                for (int i = 0; i < r; i++) {
                    for (int j = 0; j < c; j++) {
                        if ((i == 0 && j == 0) || (i == 0 && j > 0) || (i > 0 && j == 0)) {
                            matrix[i][j] = 0;
                            traceback[i][j] = "0";
                            paths[i][j] = 0;
                        }
                        else {
                            if (sequence1.getText().charAt(i - 1) == sequence2.getText().charAt(j - 1)) {
                                isMatch = true;
                                max = matrix[i - 1][j - 1] + matchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            }
                            else if (sequence1.getText().charAt(i - 1) != sequence2.getText().charAt(j - 1)) {
                                isMatch = false;
                                max = matrix[i - 1][j - 1] + mismatchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            }

                            int value1 = matrix[i - 1][j] + gapValue;
                            int value2 = matrix[i][j - 1] + gapValue;

                            if (max < value1) {
                                max = value1;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j);
                            }
                            if (max < value2) {
                                max = value2;
                                traceback[i][j] = Integer.toString(i) + "," + Integer.toString(j - 1);
                            }
                            matrix[i][j] = max;

                            if(isMatch) {
                                if((matrix[i - 1][j - 1] + matchValue == value1)) {
                                    paths[i][j] = 5; // ⇑ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue == value2))  {
                                    paths[i][j] = 4; // ⇐ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue > value1)  && (matrix[i - 1][j - 1] + matchValue > value2)) {
                                    paths[i][j] = 3; // ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue < value2) && (value2 > value1 )) {
                                    paths[i][j] = 2; // ⇐
                                }
                                else if((matrix[i - 1][j - 1] + matchValue < value1) && (value1 > value2 )) {
                                    paths[i][j] = 1; // ⇑
                                }
                                else {
                                    paths[i][j] = 0;
                                }
                            }
                            else if(!isMatch) {
                                if((matrix[i - 1][j - 1] + mismatchValue == value1)) {
                                    paths[i][j] = 5; // ⇑ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue == value2))  {
                                    paths[i][j] = 4; // ⇐ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue > value1)  && (matrix[i - 1][j - 1] + mismatchValue > value2)) {
                                    paths[i][j] = 3; // ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue < value2) && (value2 > value1 )) {
                                    paths[i][j] = 2; // ⇐
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue < value1) && (value1 > value2 )) {
                                    paths[i][j] = 1; // ⇑
                                }
                                else {
                                    paths[i][j] = 0;
                                }
                            }
                        }
                    }
                }
            }
            else if (alignmentValue.equals("Pattern")) {
                int max2;
                for (int i = 0; i < r; i++) {
                    for (int j = 0; j < c; j++) {
                        if (i == 0 && j == 0) {
                            matrix[i][j] = 0;
                            traceback[i][j] = "0";
                            paths[i][j] = 0;
                        }
                        else if (i == 0 && j > 0) {
                            if( sequence1.length() > sequence2.length()) {
                                matrix[i][j] = matrix[i][j - 1] + gapValue;
                                paths[i][j] = 2;
                            }
                            else {
                                matrix[i][j] = 0;
                                paths[i][j] = 0;
                            }
                            traceback[i][j] = "0";
                        }
                        else if (i > 0 && j == 0) {
                            if( sequence1.length() > sequence2.length()) {
                                matrix[i][j] = 0;
                                paths[i][j] = 0;

                            }
                            else {
                                matrix[i][j] = matrix[i - 1][0] + gapValue;
                                paths[i][j] = 1;
                            }
                            traceback[i][j] = "0";
                        }
                        else {

                            if (sequence1.getText().charAt(i - 1) == sequence2.getText().charAt(j - 1)) {
                                isMatch = true;
                                max = matrix[i - 1][j - 1] + matchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            }
                            else if (sequence1.getText().charAt(i - 1) != sequence2.getText().charAt(j - 1)) {
                                isMatch = false;
                                max = matrix[i - 1][j - 1] + mismatchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            }

                            int value1 = matrix[i - 1][j] + gapValue;
                            int value2 = matrix[i][j - 1] + gapValue;

                            if (max < value1) {
                                max = value1;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j);
                            }
                            if (max < value2) {
                                max = value2;
                                traceback[i][j] = Integer.toString(i) + "," + Integer.toString(j - 1);
                            }
                            matrix[i][j] = max;

                            if(isMatch) {
                                if((matrix[i - 1][j - 1] + matchValue == value1)) {
                                    paths[i][j] = 5; // ⇑ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue == value2))  {
                                    paths[i][j] = 4; // ⇐ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue > value1)  && (matrix[i - 1][j - 1] + matchValue > value2)) {
                                    paths[i][j] = 3; // ⇖
                                }
                                else if((matrix[i - 1][j - 1] + matchValue < value2) && (value2 > value1 )) {
                                    paths[i][j] = 2; // ⇐
                                }
                                else if((matrix[i - 1][j - 1] + matchValue < value1) && (value1 > value2 )) {
                                    paths[i][j] = 1; // ⇑
                                }
                                else {
                                    paths[i][j] = 0;
                                }
                            }
                            else if(!isMatch) {
                                if((matrix[i - 1][j - 1] + mismatchValue == value1)) {
                                    paths[i][j] = 5; // ⇑ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue == value2))  {
                                    paths[i][j] = 4; // ⇐ ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue > value1)  && (matrix[i - 1][j - 1] + mismatchValue > value2)) {
                                    paths[i][j] = 3; // ⇖
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue < value2) && (value2 > value1 )) {
                                    paths[i][j] = 2; // ⇐
                                }
                                else if((matrix[i - 1][j - 1] + mismatchValue < value1) && (value1 > value2 )) {
                                    paths[i][j] = 1; // ⇑
                                }
                                else {
                                    paths[i][j] = 0;
                                }
                            }
                        }
                    }
                }

            }

        }

        else {
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    if (i == 0 && j == 0) {
                        matrix[i][j] = 0;
                        traceback[i][j] = "0";
                        paths[i][j] = 0;
                    }
                    else if (i == 0 && j > 0) {
                        matrix[i][j] = matrix[i][j - 1] + mismatchValue;
                        traceback[i][j] = "0";
                        paths[i][j] = 2;
                    }
                    else if (i > 0 && j == 0) {
                        matrix[i][j] = matrix[i - 1][j] + mismatchValue;
                        traceback[i][j] = "0";
                        paths[i][j] = 1;
                    }
                    else {
                        if (sequence1.getText().charAt(i - 1) == sequence2.getText().charAt(j - 1)) {
                            isMatch = true;
                            min = matrix[i - 1][j - 1] + matchValue;
                            traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                        }
                        else if (sequence1.getText().charAt(i - 1) != sequence2.getText().charAt(j - 1)) {
                            isMatch = false;
                            min = matrix[i - 1][j - 1] + mismatchValue;
                            traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                        }

                        int value1 = matrix[i - 1][j] + mismatchValue;
                        int value2 = matrix[i][j - 1] + mismatchValue;

                        if (min > value1) {
                            min = value1;
                            traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j);
                        }
                        if (min > value2) {
                            min = value2;
                            traceback[i][j] = Integer.toString(i) + "," + Integer.toString(j - 1);
                        }

                        matrix[i][j] = min;

                        if(isMatch) {
                            if((matrix[i - 1][j - 1] + matchValue == value1)) {
                                paths[i][j] = 5; // ⇑ ⇖
                            }
                            else if((matrix[i - 1][j - 1] + matchValue == value2))  {
                                paths[i][j] = 4; // ⇐ ⇖
                            }
                            else if((matrix[i - 1][j - 1] + matchValue < value1)  && (matrix[i - 1][j - 1] + matchValue < value2)) {
                                paths[i][j] = 3; // ⇖
                            }
                            else if((matrix[i - 1][j - 1] + matchValue < value2) && (value2 > value1 )) {
                                paths[i][j] = 1; // ⇐
                            }
                            else if((matrix[i - 1][j - 1] + matchValue < value1) && (value1 > value2 )) {
                                paths[i][j] = 2; // ⇑
                            }
                            else {
                                paths[i][j] = 0;
                            }
                        }
                        else if(!isMatch) {
                            if((matrix[i - 1][j - 1] + mismatchValue == value1)) {
                                paths[i][j] = 5; // ⇑ ⇖
                            }
                            else if((matrix[i - 1][j - 1] + mismatchValue == value2))  {
                                paths[i][j] = 4; // ⇐ ⇖
                            }
                            else if((matrix[i - 1][j - 1] + mismatchValue < value1)  && (matrix[i - 1][j - 1] + mismatchValue < value2)) {
                                paths[i][j] = 3; // ⇖
                            }
                            else if((matrix[i - 1][j - 1] + mismatchValue < value2) && (value2 > value1 )) {
                                paths[i][j] = 1; // ⇐
                            }
                            else if((matrix[i - 1][j - 1] + mismatchValue < value1) && (value1 > value2 )) {
                                paths[i][j] = 2; // ⇑
                            }
                            else {
                                paths[i][j] = 0;
                            }
                        }
                    }
                }
            }

        }
    }


    public void extractTraceback() {

        if (alignmentValue.equals("Global")) {
            int value;
            String temp = "";
            int index = 0;
            int i;
            int j;

            if(sequence1.length() > sequence2.length()) {
                value = sequence1.length();
            }
            else {
                value = sequence2.length();
            }
            globalTraceback.add(traceback[sequence1.length()][sequence2.length()]);
            while(value > index+1){
                temp = globalTraceback.get(index);
                String[] tokens = temp.split(",");

                if(!temp.equals("0")) {
                    i = Integer.parseInt(tokens[0]);
                    j = Integer.parseInt(tokens[1]);
                    if(traceback[i][j].equals("0")) {

                        if(sequence1.length() < sequence2.length()) {
                            if(sequence2.length() > index) {
                                index++;
                                j = j-1;
                                globalTraceback.add(index,i+","+j);

                            }
                        }
                        else {
                            if(sequence1.length() > index) {
                                index++;
                                i = i-1;
                                globalTraceback.add(index,i+","+j);

                            }
                        }
                    }
                    else {
                        index++;
                        globalTraceback.add(index,traceback[i][j]);
                    }

                }
            }
            Collections.reverse(globalTraceback);
        }

        else if (alignmentValue.equals("Local")) {
            String temp;
            String maxIndex = "";
            int value;
            int index = 0;
            int max = 0;
            int i;
            int j;

            if(sequence1.length() > sequence2.length()) {
                value = sequence2.length();
            }
            else {
                value = sequence1.length();
            }
            for(int x = 0; x < sequence1.length() + 1; x++) {
                for(int y = 0; y < sequence2.length() + 1; y++) {
//                    Log.i("NOO",Integer.toString(matrix[x][y]));
                    if(max < matrix[x][y]) {
                        max = matrix[x][y];
                        maxIndex = Integer.toString(x) + "," + Integer.toString(y);
                    }
                }
            }
//            Log.i("NOO",Integer.toString(max));
            globalTraceback.add(maxIndex);
            index++;
            String[] tokens = maxIndex.split(",");
            i = Integer.parseInt(tokens[0]);
            j = Integer.parseInt(tokens[1]);
            globalTraceback.add(traceback[i][j]);
            while(value > index){
                temp = globalTraceback.get(index);
                String[] tokens2 = temp.split(",");

                if(!temp.equals("0")) {
                    i = Integer.parseInt(tokens2[0]);
                    j = Integer.parseInt(tokens2[1]);
//                    if(traceback[i][j].equals("0,0")) {
//                        index++;
//                        globalTraceback.add(index,traceback[i][j]);
//                        break;
//                    }
                    if(Integer.toString(matrix[i][j]).equals("0")) {

                        break;
                    }
                    else {
                        index++;
                        globalTraceback.add(index,traceback[i][j]);
                    }
                }
                else {
                    break;
                }
            }
            Collections.reverse(globalTraceback);
        }
        else if (alignmentValue.equals("Dovetail")) {
            String temp;
            String maxIndex = "";
            int value;
            int index = 0;
            int max = 0;
            int i;
            int j;

            if(sequence1.length() > sequence2.length()) {
                value = sequence2.length();
            }
            else {
                value = sequence1.length();
            }
            for(int x = 0; x < sequence1.length() + 1; x++) {
//                    Log.i("NOO",Integer.toString(matrix[x][y]));
                if(max < matrix[x][sequence2.length()]) {
                    max = matrix[x][sequence2.length()];
                    maxIndex = Integer.toString(x) + "," + Integer.toString(sequence2.length());
                }
            }
            for(int y = 0; y < sequence2.length() + 1; y++) {
//                    Log.i("NOO",Integer.toString(matrix[x][y]));
                if(max < matrix[sequence1.length()][y]) {
                    max = matrix[sequence1.length()][y];
                    maxIndex = Integer.toString(sequence1.length()) + "," + Integer.toString(y);
                }
            }
//            Log.i("NOO",Integer.toString(max));
            globalTraceback.add(maxIndex);
            index++;
            String[] tokens = maxIndex.split(",");
            i = Integer.parseInt(tokens[0]);
            j = Integer.parseInt(tokens[1]);
            globalTraceback.add(traceback[i][j]);
            while(value > index){
                temp = globalTraceback.get(index);
                String[] tokens2 = temp.split(",");

                if(!temp.equals("0")) {
                    i = Integer.parseInt(tokens2[0]);
                    j = Integer.parseInt(tokens2[1]);
                    if(Integer.toString(matrix[i][j]).equals("0")) {
//                        index++;
//                        globalTraceback.add(index,i+","+j);
                        break;
                    }
                    else {
                        index++;
                        globalTraceback.add(index,traceback[i][j]);
                    }
                }
                else {
                    break;
                }
            }
            Collections.reverse(globalTraceback);
        }
        else if (alignmentValue.equals("Pattern")) {
            String temp;
            String maxIndex = "";
            int value;
            int index = 0;
            int max = 0;
            int i;
            int j;

            if(sequence1.length() > sequence2.length()) {
                value = sequence2.length();
                for(int x = 0; x < sequence1.length() + 1; x++) {
//                    Log.i("NOO",Integer.toString(matrix[x][y]));
                    if(max < matrix[x][sequence2.length()]) {
                        max = matrix[x][sequence2.length()];
                        maxIndex = Integer.toString(x) + "," + Integer.toString(sequence2.length());
                    }
                }
            }
            else {
                value = sequence1.length();
                for(int y = 0; y < sequence2.length() + 1; y++) {
//                    Log.i("NOO",Integer.toString(matrix[x][y]));
                    if(max < matrix[sequence1.length()][y]) {
                        max = matrix[sequence1.length()][y];
                        maxIndex = Integer.toString(sequence1.length()) + "," + Integer.toString(y);
                    }
                }
            }


//            Log.i("NOO",Integer.toString(max));
            globalTraceback.add(maxIndex);
            index++;
            String[] tokens = maxIndex.split(",");
            i = Integer.parseInt(tokens[0]);
            j = Integer.parseInt(tokens[1]);
            globalTraceback.add(traceback[i][j]);
            while(value > index){
                temp = globalTraceback.get(index);
                String[] tokens2 = temp.split(",");

                if(!temp.equals("0")) {
                    i = Integer.parseInt(tokens2[0]);
                    j = Integer.parseInt(tokens2[1]);
                    if(Integer.toString(matrix[i][j]).equals("0")) {
                        index++;
                        globalTraceback.add(index,i+","+j);
                        break;
                    }
                    else {
                        index++;
                        globalTraceback.add(index,traceback[i][j]);
                    }
                }
                else {
                    break;
                }
            }
            Collections.reverse(globalTraceback);
        }


        for(int k= 0; k < globalTraceback.size(); k ++) {
            Log.i("SOS", globalTraceback.get(k));
        }

    }

    public void alignSequence() {
        if(alignmentValue == "Global") {
            String temp;
            int x;
            int y;
            int x2;
            int y2;
            int tempX;
            int tempY;
            boolean set = false;

            temp = globalTraceback.get(0);
            String[] tokens2 = temp.split(",");

            x2 = Integer.parseInt(tokens2[0]);
            y2 = Integer.parseInt(tokens2[1]);

            if (sequence1.length() == 1 && sequence2.length() == 1) {
                if (sequence1.getText().charAt(0) == sequence2.getText().charAt(0)) {
                    alignment.add("|");
                } else if (sequence1.getText().charAt(0) != sequence2.getText().charAt(0)) {
                    alignment.add("r");
                }
                set = true;
            }

            for (int i = 1; i < globalTraceback.size(); i++) {

                if (set) {
                    break;
                }

                temp = globalTraceback.get(i);
                String[] tokens = temp.split(",");

                x = Integer.parseInt(tokens[0]);
                y = Integer.parseInt(tokens[1]);

                tempX = x2;
                tempY = y2;

                x2 = x2 - x;
                y2 = y2 - y;

                if (x2 == -1 && y2 == -1) {
                    if (sequence1.getText().charAt(tempX) == sequence2.getText().charAt(tempY)) {
                        alignment.add("|");
                    } else if (sequence1.getText().charAt(tempX) != sequence2.getText().charAt(tempY)) {
                        alignment.add("r");
                    }

                } else if (x2 == 0 && y2 == -1) {
                    alignment.add("d");

                } else {
                    alignment.add("i");
                }
                x2 = x;
                y2 = y;

                if (i == globalTraceback.size() - 1 && (alignmentValue == "Global")) {
                    x = sequence1.length();
                    y = sequence2.length();
                    x2 = x2 - x;
                    y2 = y2 - y;
                    if (x2 == -1 && y2 == -1) {
                        if (sequence1.getText().charAt(sequence1.length() - 1) == sequence2.getText().charAt(sequence2.length() - 1)) {
                            alignment.add("|");
                        } else if (sequence1.getText().charAt(sequence1.length() - 1) != sequence2.getText().charAt(sequence2.length() - 1)) {
                            alignment.add("r");
                        }

                    } else if (x2 == 0 && y2 == -1) {
                        alignment.add("d");

                    } else {
                        alignment.add("i");
                    }
                }
            }
            for (int k = 0; k < alignment.size(); k++) {
                Log.i("LOL", alignment.get(k));
            }

        }

        else {
            String temp;
            int x;
            int y;
            int x2;
            int y2;
            int tempX;
            int tempY;
            boolean set = false;

            temp = globalTraceback.get(0);
            String[] tokens2 = temp.split(",");

            x2 = Integer.parseInt(tokens2[0]);
            y2 = Integer.parseInt(tokens2[1]);

            if (sequence1.length() == 1 && sequence2.length() == 1) {
                if (sequence1.getText().charAt(0) == sequence2.getText().charAt(0)) {
                    alignment.add("|");
                } else if (sequence1.getText().charAt(0) != sequence2.getText().charAt(0)) {
                    alignment.add("r");
                }
                set = true;
            }

            for (int i = 1; i < globalTraceback.size(); i++) {
                if(set) {
                    break;
                }
                temp = globalTraceback.get(i);
                String[] tokens = temp.split(",");

                x = Integer.parseInt(tokens[0]);
                y = Integer.parseInt(tokens[1]);

                tempX = x2;
                tempY = y2;

                x2 = x2 - x;
                y2 = y2 - y;

                if (x2 == -1 && y2 == -1) {
                    if (sequence1.getText().charAt(tempX) == sequence2.getText().charAt(tempY)) {
                        alignment.add("|");
                    } else if (sequence1.getText().charAt(tempX) != sequence2.getText().charAt(tempY)) {
                        alignment.add("r");
                    }

                } else if (x2 == 0 && y2 == -1) {
                    alignment.add("d");

                } else {
                    alignment.add("i");
                }
                x2 = x;
                y2 = y;
            }
            for (int k = 0; k < alignment.size(); k++) {
                Log.i("LOL", alignment.get(k));
            }
        }
    }

    public void padSequence() {
        s1.setText(sequence1.getText());
        s2.setText(sequence2.getText());

        s1.setTextColor(Color.BLACK);
        s2.setTextColor(Color.BLACK);

        for (int k = 0; k < alignment.size(); k++) {
            if (k==0) {
                align.getText().insert(0, " ");
            }
            align.append(alignment.get(k)+ " ");
        }
        align.setTextColor(Color.RED);

        if(alignmentValue == "Global") {
            if (sequence2.length() > sequence1.length()) {
                for (int k = 0; k < sequence2.length(); k++) {
                    if (alignment.get(k).equals("d")) {
                        s1.getText().insert(k, "-");
                    }
                }
            }
            else if (sequence1.length() > sequence2.length()) {
                for (int k = 0; k < sequence1.length(); k++) {
                    if (alignment.get(k).equals("i")) {
                        s2.getText().insert(k, "-");
                    }
                }
            }
        }

        else {
            int index = returnIndex(sequence1.getText().toString(), sequence2.getText().toString());
            int indexDifference = returnIndexDiff(sequence1.getText().toString(), sequence2.getText().toString());
            Log.i("Index", Integer.toString(index));
            Log.i("IndexDiff", Integer.toString(indexDifference));
            if (sequence2.length() > sequence1.length()) {
                for (int k = 0; k < sequence2.length(); k++) {
                    if (k != index) {
                        s1.getText().insert(k, "-");
                    }
                    else {
                        break;
                    }
                }
                int padding = sequence2.length() - sequence1.length();
                Log.i("padding", Integer.toString(padding));
                for (int k = 0; k < padding; k++) {
                    if (index == indexDifference && alignmentValue == "Dovetail") {
                       if(padding == 1) {
                            s2.append("     ");
                            align.getText().insert(0, "   ");
                        }
                        else if(padding <3) {
                            s2.append("    ");
                            align.getText().insert(0, "  ");
                        }
                        else {
                            s2.append(" ");
                            align.getText().insert(0, " ");
                        }
                    }
                    else if (index == indexDifference && alignmentValue == "Pattern") {
                        s2.getText().insert(0, "  ");
                        align.getText().insert(0, "   ");
                    }
                    else if (index == 0 && indexDifference == 3 && alignmentValue == "Dovetail") {
                        s2.getText().insert(0, "                  ");
                        align.getText().insert(0, "         ");
                    }
                    else if (index == 0 && indexDifference == 2 && alignmentValue == "Dovetail") {
                        s2.getText().insert(0, "             ");
                        align.getText().insert(0, "      ");
                    }
                    else if (index == 0 && indexDifference == 1 && alignmentValue == "Dovetail") {
                        s2.getText().insert(0, "      ");
                        align.getText().insert(0, "  ");
                    }

                    else if (index < 2 && padding > 2) {
                        align.append(" ");
                        s1.append(" ");
                    }
                    else if (index < 2) {
                        align.append("   ");
                        s1.append("  ");
                    }


                    else if (index < 4 && padding > 2) {
                        align.append("");
                        s1.append("");
                    }
                    else if (index < 4) {
                        align.append("  ");
                        s1.append("");
                    }
                    else {
                        s1.getText().insert(0, " ");
                        align.getText().insert(0, "  ");
                    }
                }
            }
            else if (sequence1.length() > sequence2.length()) {
                for (int k = 0; k < sequence1.length(); k++) {
                    if (k != index) {
                        s2.getText().insert(k, "-");
                    }
                    else {
                        break;
                    }
                }
                int padding = sequence1.length() - sequence2.length();
                Log.i("padding", Integer.toString(padding));
                for (int k = 0; k < padding; k++) {
                    if (index == indexDifference && alignmentValue == "Dovetail") {
                        if(padding == 1 && index == 0) {
                            s1.getText().insert(0, "  ");
                            align.getText().insert(0, "");
                        }
                        else if(padding == 1) {
                            s1.getText().insert(0, "                    ");
                            align.getText().insert(0, "         ");
                        }
                        else if(padding <3) {
                            s1.append("    ");
                            align.getText().insert(0, "  ");
                        }
                        else {
                            s1.getText().insert(0, "    ");
                            align.getText().insert(0, " ");
                        }
                    }
                    else if (index == indexDifference && alignmentValue == "Pattern") {
                        s1.getText().insert(0, "  ");
                        align.getText().insert(0, "   ");
                    }
                    else if (index == 0 && indexDifference == 4 && alignmentValue == "Dovetail") {
                        if(padding == 1) {
                            s1.append("              ");
                            align.append("      ");
                        }
                        else if(padding < 3) {
                            s1.append("      ");
                            align.append("  ");
                        }
                        else {
                            s1.append("    ");
                            align.append(" ");
                        }
                    }
                    else if (index == 0 && indexDifference == 3 && alignmentValue == "Dovetail") {
                        if(padding == 1) {
                            s1.append("              ");
                            align.append("      ");
                        }
                        else if(padding < 3) {
                            s1.append("      ");
                            align.append("  ");
                        }
                        else {
                            s1.append("    ");
                            align.append(" ");
                        }
                    }
                    else if (index == 0 && indexDifference == 2 && alignmentValue == "Dovetail") {
                        if(padding == 1) {
                            s1.append("              ");
                            align.append("      ");
                        }
                        else if(padding < 3) {
                            s1.append("      ");
                            align.append("  ");
                        }
                        else {
                            s1.append("    ");
                            align.append(" ");
                        }
                    }
                    else if (index == 0 && indexDifference == 1 && alignmentValue == "Dovetail") {
                        if(padding == 1) {
                            s1.append("              ");
                            align.append("      ");
                        }
                        else if(padding < 3) {
                            s1.append("      ");
                            align.append("  ");
                        }
                        else {
                            s1.append("    ");
                            align.append(" ");
                        }
                    }
                    else if (index < 2 && padding > 2) {
                        align.append(" ");
                        s2.append(" ");
                    }
                    else if (index < 2) {
                        align.append("   ");
                        s2.append("  ");
                    }


                    else if (index < 4 && padding > 2) {
                        align.append("");
                        s2.append("");
                    }
                    else if (index < 4) {
                        align.append("  ");
                        s2.append("");
                    }
                    else {
                        s2.getText().insert(0, " ");
                        align.getText().insert(0, "  ");
                    }
                }
            }
            else {

                for (int k = 0; k < 3*indexDifference; k++) {

                    if (index == indexDifference && alignmentValue == "Dovetail") {
                        align.append(" ");
                        s2.append("  ");
                    }

                    else if (index == indexDifference) {
                        align.append(" ");
                        s2.append(" ");
                    }
                    else if(indexDifference == 1 && index > 0 && alignmentValue == "Dovetail") {
                        align.getText().insert(0, "   ");
                        s2.getText().insert(0, "     ");
                    }
                    else if(indexDifference == 1 && index > 0) {
                        align.append("  ");
                        s2.getText().insert(0, "  ");
                    }

                    else {
                        align.getText().insert(0, " ");
                        s2.getText().insert(0, "  ");
                    }

                }
                if(indexDifference == 0 && index != 0) {
                    align.append("          ");
                }
            }
        }
    }

    public boolean hasSameCharacter(String s1, String s2){

        for(int i = 0; i < s1.length(); i++) {
            for(int j = 0; j < s2.length(); j++) {
                if((s1.charAt(i))==((s2.charAt(j)))){
                    return  true;
                }
            }
        }
        return false;
    }

    public int returnIndex(String s1, String s2){

        for(int i = 0; i < s1.length(); i++) {
            for(int j = 0; j < s2.length(); j++) {
                if((sequence1.getText().charAt(i))==((sequence2.getText().charAt(j)))){
                    return j;
                }
            }
        }
        return 0;
    }

    public int returnIndexDiff(String s1, String s2){

        for(int i = 0; i < s1.length(); i++) {
            for(int j = 0; j < s2.length(); j++) {

                if((sequence1.getText().charAt(i))==((sequence2.getText().charAt(j)))){
                    if (i > j) {
                        return i-j;
                    }
                    else {
                        return j-i;
                    }
                }
            }
        }
        return 0;
    }

    public void initialize() {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View promptView = layoutInflater.inflate(R.layout.form, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(promptView);

        sequence1 = (EditText) promptView.findViewById(R.id.sequence1);
        sequence2 = (EditText) promptView.findViewById(R.id.sequence2);
        match = (EditText) promptView.findViewById(R.id.match);
        mismatch = (EditText) promptView.findViewById(R.id.mismatch);
        gap = (EditText) promptView.findViewById(R.id.gap);


        alignmentMethod = (Spinner) promptView.findViewById(R.id.alignmentMethod);
        list = new ArrayList<String>();
        list.add("Alignment Method: Global");
        list.add("Alignment Method: Local");
        list.add("Alignment Method: Dovetail");
        list.add("Alignment Method: Pattern");

        scoringMethod = (Spinner) promptView.findViewById(R.id.scoringMethod);
        list2 = new ArrayList<String>();
        list2.add("Scoring Method: Score");
        list2.add("Scoring Method: Edit Distance");
        setSpace(list, list2);


        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        tableLayout.removeAllViews();
                        if(sequence1.length() > 0 && sequence2.length() > 0) {
                            matrix = new int[sequence1.length() + 1][sequence2.length() + 1];
                            paths = new int[sequence1.length() + 1][sequence2.length() + 1];
                            traceback = new String[sequence1.length() + 1][sequence2.length() + 1];
                            globalTraceback = new ArrayList<String>();
                            alignment = new ArrayList<String>();

                            if(match.getText().toString().equals("") && !scoringMethod.getSelectedItem().toString().equals("Scoring Method: Edit Distance")) {
                                matchValue = 1;
                            }
                            else if(match.getText().toString().equals("") && scoringMethod.getSelectedItem().toString().equals("Scoring Method: Edit Distance")) {
                                matchValue = 0;
                            }
                            else {
                                matchValue = Integer.parseInt(match.getText().toString());
                            }
                            if(mismatch.getText().toString().equals("") && !scoringMethod.getSelectedItem().toString().equals("Scoring Method: Edit Distance")) {
                                mismatchValue = -1;
                            }
                            else if(mismatch.getText().toString().equals("") && scoringMethod.getSelectedItem().toString().equals("Scoring Method: Edit Distance")) {
                                mismatchValue = 1;
                            }
                            else {
                                mismatchValue = Integer.parseInt(mismatch.getText().toString());
                            }
                            if(gap.getText().toString().equals("")) {
                                gapValue = -2;
                            }
                            else {
                                gapValue = Integer.parseInt(gap.getText().toString());
                            }

                            if(alignmentMethod.getSelectedItem().toString().equals("Alignment Method: Global")) {
                                alignmentValue = "Global";
                            }
                            else if(alignmentMethod.getSelectedItem().toString().equals("Alignment Method: Local")) {
                                alignmentValue = "Local";
                            }
                            else if(alignmentMethod.getSelectedItem().toString().equals("Alignment Method: Dovetail")) {
                                alignmentValue = "Dovetail";
                            }
                            else if(alignmentMethod.getSelectedItem().toString().equals("Alignment Method: Pattern")) {
                                alignmentValue = "Pattern";
                            }
                            else {
                                alignmentValue = "Global";
                            }

                            if(scoringMethod.getSelectedItem().toString().equals("Scoring Method: Score")) {
                                scoringValue = "Score";
                            }
                            else if(scoringMethod.getSelectedItem().toString().equals("Scoring Method: Edit Distance")) {
                                scoringValue = "Edit Distance";
                                gap.setVisibility(View.INVISIBLE);
                            } else {
                                scoringValue = "Score";
                            }

                            if(alignmentValue.equals("Local") || alignmentValue.equals("Dovetail") || alignmentValue.equals("Pattern")) {
                                if (hasSameCharacter(sequence1.getText().toString(), sequence2.getText().toString())) {
                                    Log.i("HEY", "GOOD");
                                    buildTable(sequence1.length() + 1, sequence2.length() + 1);
                                    extractTraceback();
                                    align.setText("");
                                    alignSequence();
                                    padSequence();
                                    displayTable(sequence1.length() + 2, sequence2.length() + 2);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "The sequences must share at least 1 common character for this alignment", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                buildTable(sequence1.length() + 1, sequence2.length() + 1);
                                extractTraceback();
                                align.setText("");
                                alignSequence();
                                padSequence();
                                displayTable(sequence1.length() + 2, sequence2.length() + 2);
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        scoringMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = scoringMethod.getSelectedItem().toString();
                if(text.equals("Scoring Method: Edit Distance")){
                    alignmentMethod.setEnabled(false);
                    gap.setVisibility(View.INVISIBLE);
                    match.setInputType(InputType.TYPE_NULL);
                    mismatch.setInputType(InputType.TYPE_NULL);
                    match.setHint("Match: 0");
                    matchValue = 0;
                    mismatch.setHint("Mismatch: 1");
                    mismatchValue = 1;
                    alignmentMethod.setSelection(0);
                }
                else {
                    alignmentMethod.setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // create an alert dialog
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    //Centers Drop down boxes (spinners)
    public void setSpace(List<String> list, List<String> list2) {
        dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view)
            {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        };
        alignmentMethod.setAdapter(dataAdapter);

        dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list2)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view)
            {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        };
        scoringMethod.setAdapter(dataAdapter2);
    }
}
