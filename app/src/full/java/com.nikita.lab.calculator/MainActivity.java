package com.nikita.lab.calculator;

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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ScrollView mainScrollView;
    EditText editText;
    Button delB;

    SharedPreferences prefs;
    static final String APP_PREFERENCES = "settings";
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
                editText.setText("");
                isClear = true;
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

    boolean isPIE(StringBuilder str){
        return (str.length() > 0 && str.charAt(str.length() - 1) == 'e')
                || (str.length() > 1 && str.charAt(str.length() - 2) == 'p'
                        && str.charAt(str.length() - 1) == 'i');
    }
    boolean isPIE(Editable str){
        return (str.length() > 0 && (str.charAt(str.length() - 1) == 'e'
                || str.charAt(str.length() - 1) == 'π'));
    }

    public void OnOpClick(View view){
        isClearNum = false;

        Button button = (Button)view;
        String op = button.getText().toString();
        switch (op) {
            case "C":
                textView.setText("");
                textView.append(PADDING);
                editText.setText("");
                result.setLength(0);
                isClear = true;
                break;
            case "⌫":
                Editable txt = editText.getText();
                if (editText.length() > 0) {
                    // del sci functions by 1 click
                    do {
                        editText.setText(txt.delete(txt.length() - 1, txt.length()));
                    }
                    while (txt.length() > 0 && Character.isLetter(txt.charAt(txt.length() - 1))
                            && !isPIE(txt));
                }
                else
                    isClear = true;
                if (result.length() > 0) {
                    // del sci functions by 1 click
                    do {
                        if (result.length() > 5 && result.indexOf("log10(") == result.length() - 6)
                            result.setLength(result.length() - 6);
                        else
                            result.setLength(result.length() - 1);
                    }
                    while (result.length() > 0 && (Character.isLetter(result.charAt(result.length() - 1)))
                            && !isPIE(result));
                }
                else
                    isClear = true;
                return;
            case "=":
                // append ) for convenience
                if(result.indexOf("(") != -1 && result.indexOf(")") == -1)
                    result.append(")");
                double res = Calculator.exec(result.toString());
                if (Double.isNaN(res))
                {
                    editText.setText("");
                    result.setLength(0);
                    textView.append("Invalid expression");
                }
                else {
                    // check if int
                    if(res % 1 == 0)
                        result = new StringBuilder(Integer.toString((int)res));
                    else
                        result = new StringBuilder(Double.toString(res));

                    textView.append(editText.getText());
                    StringBuilder res_text = new StringBuilder(result);
                    editText.setText(res_text.insert(0, "= "));
                }
                textView.append("\n");
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
                    case "%":
                        op_eval = "#";
                        break;
                    case "sin":
                        op_eval = op = "sin(";
                        break;
                    case "cos":
                        op_eval = op = "cos(";
                        break;
                    case "tan":
                        op_eval = op = "tan(";
                        break;
                    case "ln":
                        op_eval = op = "ln(";
                        break;
                    case "lg":
                        op_eval = "log10(";
                        op = "lg(";
                        break;
                    case "√":
                        op_eval = "sqrt(";
                        op = "√(";
                        break;
                    case "\uD835\uDF0B":
                        op = "π";
                        op_eval = "pi";
                        break;
                    default:
                        op_eval = op;
                        break;
                }
                // change operation if already exist
                if (!isClear
                        && result.length() > 0
                        && op_eval.length() < 2 && !op.equals("(")
                        && result.charAt(result.length() - 1) != '(')
                {
                    if(Character.isDigit(result.charAt(result.length() - 1))
                            || result.charAt(result.length() - 1) == ')'
                            || isPIE(result)
                            || result.charAt(result.length() - 1) == '!') {
                        editText.append(op);
                        result.append(op_eval);
                    }
                    else {
                        if (result.length() > 1){
                            Editable txt_sign = editText.getText();
                            editText.setText(txt_sign.replace(txt_sign.length() - 1, txt_sign.length(), op));
                            result.replace(result.length() - 1, result.length(), op_eval);
                        }
                    }
                }
                else if(op_eval.length() > 1 || op.equals("(") || op_eval.equals("-")) {
                    isClear = false;
                    if (result.length() > 0
                            && (Character.isDigit(result.charAt(result.length() - 1))
                                || result.charAt(result.length() - 1) == ')'
                                || isPIE(result)))
                    {
                        result.append("*");
                        editText.append("×");
                    }
                    editText.append(op);
                    result.append(op_eval);
                }
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
        else if(result.length() > 0 && (isPIE(result) || result.charAt(result.length() - 1) == ')'
                || (num.equals("e") && Character.isDigit(result.charAt(result.length() - 1)))))
        {
            result.append("*");
            editText.append("×");
            editText.append(num);
        }
        else{
            editText.append(num);
        }
        result.append(num);
    }

    public void OnSciClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (findViewById(R.id.frm).getHeight() == 0)
        {
            Fragment sciFragment = new ScientificFragment();
            transaction.add(R.id.frm, sciFragment);
        }
        else {
            transaction.remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.frm)));
        }
        transaction.commit();

    }
}
