package com.nikita.lab.calculator;
import org.mariuszgromada.math.mxparser.Expression;


class Calculator {
    static double exec(String expr){
        Expression expres = new Expression(expr);
        return expres.calculate();
    }
}
