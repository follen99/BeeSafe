package com.example.beesafe.listeners;

import android.view.View;

import com.example.beesafe.model.Report;

public interface ReportListener {
    public void onReportLongClicked(Report report, int position, View view);
    public void onReportClicked(Report report, int position, View view);
}
