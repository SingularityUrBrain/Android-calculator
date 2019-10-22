package com.nikita.lab.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlArithmetic;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;



public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {

    TextView textView;
    ScrollView mainScrollView;
    EditText editText;

    String PADDING = "\n\n\n\n\n";
    //StringBuilder buff = new StringBuilder(PADDING);
    StringBuilder result = new StringBuilder("0");
    double answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }
    private void init(){
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
    public void onFragmentInteraction(String link) {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager()
                .findFragmentById(R.id.baseFragment);
//        if (fragment != null && fragment.isInLayout()) {
//            fragment.onAction(link);
//        }
    }

    public void OnOpClick(View view){
        Button button = (Button)view;
        String op = button.getText().toString();
        switch (op) {
            case "C":
                textView.setText("");
                textView.append(PADDING);
                editText.setText("0");
                result.setLength(0);
                result.append("0");
                break;
            case "⌫":
                Editable txt = editText.getText();
                editText.setText(txt.delete(txt.length() - 1, txt.length()));
                result.setLength(result.length() - 1);
                return;
            case "=":
                JexlEngine jexl = new JexlEngine();
                String jexlExp = result.toString();
                Expression e = jexl.createExpression(jexlExp);
                Object o = e.evaluate(null);
                StringBuilder ans_str = new StringBuilder(o.toString());
                result = new StringBuilder(ans_str);
                textView.append(editText.getText());
                textView.append("\n");
                editText.setText(ans_str.insert(0, "= "));
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
                result.append(op_eval);
                textView.append(editText.getText());
                textView.append("\n");
                editText.setText(op);
                editText.append(" ");
                break;
        }

        //textView.setText(buff);
        mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }
    public void OnNumberClick(View view){
        Button button = (Button)view;
        String num = button.getText().toString();
        if (result.charAt(0) == '0'){
            textView.append(PADDING);
            result.setLength(0);
            editText.setText(num);
        }
        else{
            editText.append(num);
        }
        result.append(num);
    }
}
