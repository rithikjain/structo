package com.rithikneil.algovisualizer.algorithm.search;

import android.app.Activity;

import com.rithikneil.algovisualizer.LogFragment;
import com.rithikneil.algovisualizer.algorithm.Algorithm;
import com.rithikneil.algovisualizer.algorithm.DataHandler;
import com.rithikneil.algovisualizer.visualizer.BinarySearchVisualizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BinarySearch extends Algorithm implements DataHandler {

    private BinarySearchVisualizer visualizer;
    private int[] array;

    private List<Integer> positions = new ArrayList<>();

    public BinarySearch(BinarySearchVisualizer visualizer, Activity activity, LogFragment logFragment) {
        this.visualizer = visualizer;
        this.activity = activity;
        this.logFragment = logFragment;
    }

    public void setData(final int[] array) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.setData(array);
            }
        });
        start();
        prepareHandler(this);
        sendData(array);
    }

    private void search() {

        logArray("Array - ", array);

        int rnd = new Random().nextInt(array.length);
        int data = array[rnd];
        addLog("Searching for " + data);

        int low = 0;
        int high = array.length - 1;

        highlight(low, high);
        highlightTrace((int) (low + high) / 2);
        sleep();

        while (high >= low) {

            int middle = (int) (low + high) / 2;
            addLog("Searching at index: " + middle);
            highlightTrace(middle);
            sleep();

            if (array[middle] == data) {
                //found
                addLog(data + " is found at position " + (middle));
                break;
            }
            if (array[middle] < data) {
                low = middle + 1;
                addLog("Going right");
                highlight(low, high);
                sleep();
            }
            if (array[middle] > data) {
                high = middle - 1;
                addLog("Going left");
                highlight(low, high);
                sleepFor(800);
            }
        }
        completed();
    }

    private void highlight(int minIndex, int maxIndex) {
        positions.clear();
        for (int i = minIndex; i <= maxIndex; i++) {
            positions.add(i);
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.highlight(positions);
            }
        });
    }

    private void highlightTrace(final int pos) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.highlightTrace(pos);
            }
        });
    }

    @Override
    public void onMessageReceived(String message) {
        if (message.equals(Algorithm.COMMAND_START_ALGORITHM)) {
            startExecution();
            search();
        }
    }

    @Override
    public void onDataRecieved(Object data) {
        this.array = (int[]) data;
    }
}
