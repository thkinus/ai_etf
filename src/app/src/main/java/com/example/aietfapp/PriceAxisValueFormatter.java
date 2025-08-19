package com.example.aietfapp;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.text.DecimalFormat;

public class PriceAxisValueFormatter extends ValueFormatter {
    private DecimalFormat decimalFormat;

    public PriceAxisValueFormatter() {
        decimalFormat = new DecimalFormat("$#.##");
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return decimalFormat.format(value);
    }
}