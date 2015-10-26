package com.example.omeed.sequencealigntool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
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

    TableLayout tableLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setTitle("Sequence Align Teaching Tool");
        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    traceback = new String[sequence1.length() + 1][sequence2.length() + 1];
                                    globalTraceback = new ArrayList<String>();


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
                                        gapValue = -5;
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
                                    }
                                    else {
                                        scoringValue = "Score";
                                    }

                                    buildTable(sequence1.length() + 1, sequence2.length() + 1);
                                    extractTraceback();
                                    displayTable(sequence1.length() + 2, sequence2.length() + 2);
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
                            gap.setVisibility(View.INVISIBLE);
                            match.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                            match.setHint("Match: 0");
                            matchValue = 0;
                            mismatch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                            mismatch.setHint("Mismatch: 1");
                            mismatchValue = 1;
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
        });
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
    }


    private void displayTable(int rows, int cols) {
        //Drawable d = getResources().getDrawable(R.drawable.diag);
        String temp2 = "";
        int row = 1;
        int col = 1;
        int counter2 = 1;

        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow r = new TableRow(this);
            r.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1.0f));


            // inner for loop
            for (int j = 0; j < cols; j++) {

                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                //textView.setTextSize(8);
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
                        textView.setText(String.valueOf(sequence1.getText().charAt(i-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 0 && j > 1){
                        textView.setText(String.valueOf(sequence2.getText().charAt(j-2)).toUpperCase());
                        textView.setTextColor(Color.BLACK);
                    }
                    else if (i == 1 && j == 1) {
                        textView.setText("0");
                        //textView.setText("0" + "  ?" + "\n"+"?");
                        //textView.setText("? ? ?");
                        //textView.setBackgroundColor(Color.rgb(255, 192, 203));
                        //textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.one, 0);

                        if(alignmentValue.equals("Global")) {
                            textView.setBackgroundColor(Color.rgb(255,192,203));
                        }
                    }
                    else if(i > 0 && j > 0) {
                        textView.setText(Integer.toString(matrix[i-1][j-1]));

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

            }
            else if (alignmentValue.equals("Dovetail")) {

            }
            else if (alignmentValue.equals("Pattern")) {

            }
                r.addView(textView);
            }
            tableLayout.addView(r);
        }
    }

    public void buildTable(int r, int c) {
        int min = 100000;
        int max = 0;
        if(scoringValue == "Score") {
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    if (i == 0 && j == 0) {
                        matrix[i][j] = 0;
                        traceback[i][j] = "0";
                    } else if (i == 0 && j > 0) {
                        matrix[i][j] = matrix[i][j - 1] + gapValue;
                        traceback[i][j] = "0";
                    } else if (i > 0 && j == 0) {
                        matrix[i][j] = matrix[i - 1][j] + gapValue;
                        traceback[i][j] = "0";
                    } else {

                        if (alignmentValue.equals("Global")) {
                            if (sequence1.getText().charAt(i - 1) == sequence2.getText().charAt(j - 1)) {
                                max = matrix[i - 1][j - 1] + matchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            } else if (sequence1.getText().charAt(i - 1) != sequence2.getText().charAt(j - 1)) {
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

                        } else if (alignmentValue.equals("Local")) {

                        } else if (alignmentValue.equals("Dovetail")) {

                        } else if (alignmentValue.equals("Pattern")) {

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
                    } else if (i == 0 && j > 0) {
                        matrix[i][j] = matrix[i][j - 1] + mismatchValue;
                        traceback[i][j] = "0";
                    } else if (i > 0 && j == 0) {
                        matrix[i][j] = matrix[i - 1][j] + mismatchValue;
                        traceback[i][j] = "0";
                    } else {

                        if (alignmentValue.equals("Global")) {
                            if (sequence1.getText().charAt(i - 1) == sequence2.getText().charAt(j - 1)) {
                                min = matrix[i - 1][j - 1] + matchValue;
                                traceback[i][j] = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                            } else if (sequence1.getText().charAt(i - 1) != sequence2.getText().charAt(j - 1)) {
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

                        } else if (alignmentValue.equals("Local")) {

                        } else if (alignmentValue.equals("Dovetail")) {

                        } else if (alignmentValue.equals("Pattern")) {

                        }

                    }

                }
            }

        }
    }

    public void extractTraceback() {
        String temp = "";
        int index = 0;
        int i;
        int j;
        int value;

        if(sequence1.length() > sequence2.length()) {
            value = sequence1.length();
        }
        else {
            value = sequence2.length();
        }

        if (alignmentValue.equals("Global")) {
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

        for(int k= 0; k < globalTraceback.size(); k ++) {
            Log.i("SOS", globalTraceback.get(k));
        }

    }

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
