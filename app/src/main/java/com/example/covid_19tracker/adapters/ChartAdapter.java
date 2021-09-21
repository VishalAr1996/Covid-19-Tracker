package com.example.covid_19tracker.adapters;

import android.graphics.RectF;

import com.example.covid_19tracker.ModelClass.DailyData;
import com.example.covid_19tracker.enums.CaseTypes;
import com.example.covid_19tracker.enums.TimePeriod;
import com.robinhood.spark.SparkAdapter;

import java.util.List;

public class ChartAdapter extends SparkAdapter {
    public CaseTypes caseTypes = CaseTypes.RECOVERED;
    public TimePeriod timePeriod = TimePeriod.WEEK;
    private List<DailyData> dailyData;

    public ChartAdapter(List<DailyData> dailyData) {
        this.dailyData = dailyData;
    }

    @Override
    public int getCount() {
        return dailyData.size();
    }

    @Override
    public Object getItem(int index) {
        return dailyData.get(index);
    }

    @Override
    public float getY(int index) {
        if (caseTypes == CaseTypes.CONFIRMED) {
            return Float.parseFloat(dailyData.get(index).getDailyConfirmed());
        } else {
            return Float.parseFloat(dailyData.get(index).getDailyRecovered());
        }
    }

    @Override
    public RectF getDataBounds() {
        RectF bound = super.getDataBounds();
        if (timePeriod != TimePeriod.ALLTIME) {
            bound.left = getCount() - timePeriod.getNumberDays();
        }
        return bound;
    }
}
