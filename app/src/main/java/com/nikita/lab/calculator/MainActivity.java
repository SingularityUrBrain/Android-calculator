package com.nikita.lab.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;


public class MainActivity extends AppCompatActivity {

    TextView textView;
    ScrollView mainScrollView;
    EditText editText;

    String PADDING = "\n\n\n\n\n";
    StringBuilder buff = new StringBuilder(PADDING);
    Double result = 0.;

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
//
//    public void Calc(View view) {
//        JexlEngine jexl = new JexlEngine();
//        String jexlExp = "3+4*(3+4)";
//        Expression e = jexl.createExpression( jexlExp );
//        JexlContext jctx = new MapContext();
//        Object o = e.evaluate(jctx);
//        Toast.makeText(this, o.toString(), Toast.LENGTH_LONG).show();
//    }
    public void OnOpClick(View view){
        Button button = (Button)view;
        String op = button.getText().toString();
        if (op.equals("C")){
            buff.setLength(0);
            buff.append(PADDING);
            editText.setText("0");
            result = 0.;
        }
        else {
            buff.append(editText.getText());
            buff.append('\n');
            editText.setText(op);
        }

        textView.setText(buff);
        mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }
}
