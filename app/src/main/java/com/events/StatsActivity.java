package com.events;

import android.graphics.Color;
import android.os.Bundle;

import com.events.Model.Event;
import com.events.Model.EventsList;
import com.events.database.Constants;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun.
 */

public class StatsActivity extends BaseActivity {

    PieChart mChart;
    EventsList eventsList;
    String HIRING = "HIRING";
    String HACKATHON = "HACKATHON";
    String BOT = "BOT";
    ArrayList<Event> events = new ArrayList<>();
    // we're going to display pie chart for school attendance
    private int[] yValues = new int[3];
    private String[] xValues = {HIRING, HACKATHON, BOT};

    // colors for different sections in pieChart
    public static final int[] MY_COLORS = {
            Color.rgb(0,131,143), Color.rgb(239,108,0), Color.rgb(93,64,55)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        eventsList = SharedPreferenceUtils.readEventList(this, Constants.EVENTLIST, null);
        if (eventsList != null) {
            events = eventsList.getEvents();
        }
        mChart = (PieChart) findViewById(R.id.chart);

        //   mChart.setUsePercentValues(true);
        mChart.setDescription("");

        mChart.setRotationEnabled(true);


        // setting sample Data for Pie Chart
        setDataForPieChart();

    }

    public void setyValues() {
        int hire = 0;
        int hack = 0;
        int bot = 0;
        if (events != null) {
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                if (event.getCategory().equalsIgnoreCase(HIRING)) {
                    hire++;
                }
                if (event.getCategory().equalsIgnoreCase(HACKATHON)) {
                    hack++;
                }
                if (event.getCategory().equalsIgnoreCase(BOT)) {
                    bot++;
                }
            }
        }
        yValues[0] = hire;
        yValues[1] = hack;
        yValues[2] = bot;
    }

    public void setDataForPieChart() {
        List<PieEntry> yVals1 = new ArrayList<PieEntry>();
        setyValues();
        for (int i = 0; i < yValues.length; i++)
            yVals1.add(new PieEntry(yValues[i], i));


        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);
        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateXY(1000, 1000);


        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(10);
    }


    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }
}
