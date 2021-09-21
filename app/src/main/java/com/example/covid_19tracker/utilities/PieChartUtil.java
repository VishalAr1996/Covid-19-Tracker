package com.example.covid_19tracker.utilities;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
public class PieChartUtil {
    public static void getPieChart(PieChart pieChart,int active , int confirmed , int deceased , int recovered,String place){
        ArrayList<PieEntry> charData=new ArrayList<>();
        charData.add(new PieEntry(active,"Active"));
        charData.add(new PieEntry(confirmed,"Confirmed"));
        charData.add(new PieEntry(deceased,"Deaths"));
        charData.add(new PieEntry(recovered,"Recovered"));

        PieDataSet pieDataSet=new PieDataSet(charData,"*Covid-19 "+place);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);

        PieData pieData=new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Covid-19 Spread in "+place);
        pieChart.animateXY(800,500);
    }

}
