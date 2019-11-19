package com.nikita.lab.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ScrollView mainScrollView;
    EditText editText;
    Button delB;

    private SharedPreferences prefs;
    public static final String APP_PREFERENCES = "settings";
    boolean isClear;
    boolean isClearNum;
    String PADDING = "\n\n\n\n\n";
    StringBuilder result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        result = new StringBuilder(prefs.getString("result", "0"));
        textView.setText(prefs.getString("textView", ""));
        editText.setText(prefs.getString("editText", ""));
        isClear = prefs.getBoolean("isClear", true);
        isClearNum = prefs.getBoolean("isClearNum", false);
    }
    private void init(){
        delB = findViewById(R.id.delB);
        delB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditText editText = findViewById(R.id.editText);
                editText.setText("0");
                return true;
            }
        });
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        mainScrollView = findViewById(R.id.scrollView);

        mainScrollView.post(new Runnable() {
            @Override
            public void run() {
                mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("textView", textView.getText().toString());
        ed.putString("editText", editText.getText().toString());
        ed.putString("result", result.toString());
        ed.putBoolean("isClear", isClear);
        ed.putBoolean("isClearNum", isClearNum);
        ed.apply();
    }


    public void OnOpClick(View view){
        isClearNum = false;

        Button button = (Button)view;
        String op = button.getText().toString();
        switch (op) {
            case "C":
                textView.setText("");
                textView.append(PADDING);
                editText.setText("0");
                result.setLength(0);
                result.append("0");
                isClear = true;
                break;
            case "⌫":
                Editable txt = editText.getText();
                if (editText.length() > 0) {
                    editText.setText(txt.delete(txt.length() - 1, txt.length()));
                }
                else
                    editText.setText("0");
                if (result.length() > 0)
                    result.setLength(result.length() - 1);
                else {
                    result.append("0");
                    isClear = true;
                }
                return;
            case "=":
                if(result.indexOf("(") != -1 && result.indexOf(")") == -1)
                    result.append(")");
                double res = Calculator.exec(result.toString());
                if (Double.isNaN(res))
                {
                    result = new StringBuilder("0");
                    editText.setText("Invalid expression");
                }
                else
                    result = new StringBuilder(Double.toString(res));
                textView.append(editText.getText());
                textView.append("\n");
                StringBuilder res_text = new StringBuilder(result);
                editText.setText(res_text.insert(0, "= "));
                isClearNum = true;
                break;
            default:
                String op_eval;
                switch (op) {
                    case "−":
                        op_eval = "-";
                        break;
                    case "×":
                        op_eval = "*";
                        break;
                    case "÷":
                        op_eval = "/";
                        break;
                    default:
                        op_eval = op;
                        break;
                }
                // TODO: change sign if sign in editText now else add sign
                if (isClear)
                {
                    editText.setText(op);
                    result.setLength(0);
                    isClear = false;
                }
                else
                {
                    editText.append(op);
                }
                result.append(op_eval);
                break;
        }

        mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }
    public void OnNumberClick(View view){
        Button button = (Button)view;
        String num = button.getText().toString();
        if (isClear || isClearNum){
            textView.append(PADDING);
            editText.setText(num);
            isClear = false;
            isClearNum = false;
            result.setLength(0);
        }
        else{
            editText.append(num);
        }
        if (result.length() > 1 && result.charAt(result.length() - 1) == '/')
            result.append(Double.toString(Double.parseDouble(num)));
        else
            result.append(num);
    }

}
