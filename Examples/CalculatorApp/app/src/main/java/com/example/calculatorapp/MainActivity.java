package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int operand1;
    private int operand2;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private boolean checkOperands(View view){

        this.result = (TextView) findViewById(R.id.risultato);

        // prendiamo il testo dalla view
        String firstOperandText = ((EditText) findViewById(R.id.primoOperando)).getText().toString();
        String secondOperandText = ((EditText) findViewById(R.id.secondoOperando)).getText().toString();

        if (! (firstOperandText.equals(null) && secondOperandText.equals(null) || firstOperandText.equals("") || secondOperandText.equals("")) ){
            this.operand1 = Integer.parseInt(firstOperandText);
            this.operand2 = Integer.parseInt(secondOperandText);

            return true;
        }

        String err = "please enter both values";
        result.setText(err);
        return  false;
    }



    public void doSum(View view){
        if (this.checkOperands(view)){
             this.result.setText(Integer.toString(this.operand1 + this.operand2));
        }
    }

    public void doSub(View view) {
        if (this.checkOperands(view)){
            this.result.setText(Integer.toString(this.operand1 - this.operand2));
        }
    }

    public void clearResult(View view) {
        this.result.setText("");
        EditText text1 = (EditText) findViewById(R.id.secondoOperando);
        text1.setText("");
        text1 = (EditText) findViewById(R.id.primoOperando);
        text1.setText("");
    }
}