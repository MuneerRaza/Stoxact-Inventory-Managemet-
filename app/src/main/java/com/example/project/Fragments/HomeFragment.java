package com.example.project.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.project.Dashboard;
import com.example.project.Helper.Log;
import com.example.project.R;
import com.example.project.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Run the graph() method on a separate thread
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                graph(view);
            }
        }, 100);

        // Get the current FirebaseUser and update UI with display name
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String displayName = user.getDisplayName();
            if (displayName != null) {
                // Update UI with user's display name on the main/UI thread
                displayName = displayName.split(" ")[0];
                binding.name.setText("Hi, " + displayName);
            }

            Uri url = user.getPhotoUrl();
            if (url != null) {
                // Load user's photo URL using Glide on the main/UI thread
                Glide.with(requireContext()).load(url).into(binding.avatar);
            } else {
                // Set default profile image on the main/UI thread
                binding.avatar.setImageResource(R.drawable.profile);
            }
        }

        return view;
    }

    private void graph(View view) {
        LineChart lineChart = view.findViewById(R.id.lineChart);

// Set line width, color, and rounded edges
        lineChart.getAxisLeft().setDrawGridLines(true); // Enable horizontal grid lines
        lineChart.getAxisLeft().setGridLineWidth(0.5f); // Set width of horizontal grid lines
        lineChart.getAxisLeft().setGridColor(Color.parseColor("#FFE1E1E1"));
        lineChart.getAxisLeft().setAxisLineWidth(2f);
        lineChart.getAxisLeft().setAxisLineColor(Color.parseColor("#FF00E1EF"));
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisLeft().setDrawZeroLine(false);
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisLeft().setAxisMaximum(50000f);
        lineChart.setDrawBorders(false);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setBorderColor(Color.parseColor("#FFD1D1D1"));
        lineChart.setBorderWidth(1f);
        lineChart.setDrawMarkers(true);
        lineChart.setNoDataText("No data available");
        lineChart.getXAxis().setAxisLineColor(Color.WHITE);

        ArrayList<Entry> entries = new ArrayList<>();
// Add your data entries here, e.g.:
        ArrayList<Log> logs = Dashboard.userData.logs;
        long price = 0;


        ArrayList<String> daysOfWeek = getDaysOfWeek();
        for (int i = 0; i < daysOfWeek.size(); i++) {
            String day = daysOfWeek.get(i);
            price = 0;
            for (int j = 0; j < logs.size(); j++) {
                Log log = logs.get(j);
                System.out.println(log.getDate() + " == " + day);
                if (log.getDate().equals(day)) {
                    price += log.getItem().getSelling_price();
                }
            }
            if(i==6){
                entries.add(new Entry(7, price));
                continue;
            }
            entries.add(new Entry(i, price));
        }


// ...

        LineDataSet dataSet = new LineDataSet(entries,"");
        dataSet.setLineWidth(1f);
        dataSet.setColor(Color.parseColor("#FF00E1EF"));
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.parseColor("#FF00E1EF"));
        dataSet.setCircleRadius(6f);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setCubicIntensity(0.2f);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.gradient_fill));
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

// Disable description label
        lineChart.getDescription().setEnabled(false);

// Customize left Y-axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawLabels(true); // Show labels on the left Y-axis
        leftAxis.setAxisLineColor(Color.LTGRAY); // Set color of Y-axis line
        leftAxis.setTextColor(Color.DKGRAY); // Set color of Y-axis labels
        leftAxis.setSpaceMin(0.75f);

        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Format the Y-axis labels
                return String.format("%.0fk", value / 1000f);
            }
        });

// Customize right Y-axis
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false); // Disable right Y-axis
//        rightAxis.setSpaceMin(0.75f);

// Customize X-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "", "Sun"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Show X-axis labels on bottom only
        xAxis.setDrawGridLines(false); // Hide vertical grid lines
        xAxis.setTextColor(Color.DKGRAY); // Set color of X-axis labels
        xAxis.setLabelCount(entries.size() + 1); // Increase the number of X-axis labels by 1
        xAxis.setSpaceMin(0.75f);

// Customize grid lines
        lineChart.getAxisLeft().setGridLineWidth(0.5f); // Set width of horizontal grid lines
        lineChart.getAxisLeft().setGridColor(Color.parseColor("#FFE1E1E1")); // Set color of horizontal grid lines
        lineChart.getXAxis().setGridLineWidth(0.5f); // Set width of vertical grid lines
        lineChart.getXAxis().setGridColor(Color.parseColor("#FFE1E1E1")); // Set color of vertical grid lines

// Customize marker view
        CustomMarkerView markerView = new CustomMarkerView(view.getContext(), R.layout.custom_marker_view);
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);

// Set legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false); // Disable legend

// Set data and update chart
        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();


    }
    private static class CustomMarkerView extends MarkerView {

        private TextView tvValue;

        public CustomMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvValue = findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry entry, Highlight highlight) {
            // Update marker view content with Y-axis value
            float value = entry.getY();
            tvValue.setText(String.format(Locale.getDefault(), "%.0fk", value / 1000f));
            super.refreshContent(entry, highlight);
        }

        @Override
        public MPPointF getOffset() {
            // Center the marker view above the data point
            return new MPPointF(-getWidth() / 2f, -getHeight() - 10);
        }
    }
    public ArrayList<String> getDaysOfWeek() {
        // Get the current date
        LocalDate today = LocalDate.now();

        // Find the first day of the current week (Monday)
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        // Create an array list to store the days of the week
        ArrayList<String> daysOfWeek = new ArrayList<>();

        // Loop through the week, adding each day to the array list
        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            daysOfWeek.add(day.format(DateTimeFormatter.ofPattern("E, MMM dd yyyy")));
        }

        // Return the list of days
        return daysOfWeek;
    }
}