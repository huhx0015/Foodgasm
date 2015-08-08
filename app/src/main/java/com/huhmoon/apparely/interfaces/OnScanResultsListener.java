package com.huhmoon.apparely.interfaces;

/**
 * Created by Michael Yoon Huh on 8/7/2015.
 */
public interface OnScanResultsListener {

    // Runs when the scan barcode results are completed
    void displayResultsFragment(String upc, Boolean isDisplay);
}
