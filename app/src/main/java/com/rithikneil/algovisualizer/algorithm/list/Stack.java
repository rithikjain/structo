package com.rithikneil.algovisualizer.algorithm.list;

import android.app.Activity;

import com.rithikneil.algovisualizer.DataUtils;
import com.rithikneil.algovisualizer.LogFragment;
import com.rithikneil.algovisualizer.algorithm.Algorithm;
import com.rithikneil.algovisualizer.algorithm.DataHandler;
import com.rithikneil.algovisualizer.visualizer.StackVisualizer;

public class Stack extends Algorithm implements DataHandler {

    public static final String PUSH = "push";
    public static final String POP = "pop";
    public static final String PEEK = "peek";

    private StackVisualizer visualizer;
    private Stack stack;

    public int maxSize;
    private int[] stackArray;
    private int top;

    public Stack(int s) {
        maxSize = s;
        stackArray = new int[maxSize];
        top = -1;
    }

    public Stack(int s, StackVisualizer visualizer, Activity activity, LogFragment logFragment) {
        this(s);
        this.visualizer = visualizer;
        this.activity = activity;
        this.logFragment = logFragment;
    }


    public void push(int j) {
        stackArray[++top] = j;
    }

    public int pop() {
        return stackArray[top--];
    }

    public int peek() {
        return stackArray[top];
    }

    public boolean isEmpty() {
        return (top == -1);
    }

    public boolean isFull() {
        return (top == maxSize - 1);
    }

    public int[] getStackArray() {
        return stackArray;
    }


    private void visualizePush(int data) {
        addLog("Pushing data:" + data + " into the stack");
        if (stack.top == stack.maxSize - 1) {
            addLog("Stack is full, unable to push");
            addLog("max sixe of stack is " + stack.maxSize);
            return;
        }
        stack.stackArray[++stack.top] = data;
        addLog("Pushed:" + data + " into the stack, the new head is: " + data);
        updateData(stack);
        highlightNode(data);
        sleep();
        highlightNode(-1);
    }

    private void visualizePeek() {
        if (stack.isEmpty()) {
            addLog("Stack is empty, unable to peek");
            return;
        }
        addLog("Peeking into the stack");
        int top = stack.stackArray[stack.top];
        addLog("The head is: " + top);
        updateData(stack);
        highlightNode(top);
        sleep();
        highlightNode(-1);
    }

    private void visualizePop() {
        if (stack.isEmpty()) {
            addLog("Stack is empty, unable to pop");
            return;
        }
        addLog("Popping the stack");
        int pop = stack.pop();
        addLog("Popped the stack, the data popped is " + pop);
        int top = stack.top;
        stack.stackArray[top + 1] = 0;
        highlightNode(pop);
        sleep();
        updateData(stack);
        highlightNode(-1);
    }

    private void highlightNode(final int node) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.highlightNode(node);
            }
        });
    }

    @Override
    public void onMessageReceived(String message) {
        switch (message) {
            case PEEK:
                clearLog();
                visualizePeek();
                break;
            case PUSH:
                clearLog();
                visualizePush(DataUtils.getRandomInt(50) + 15);
                break;
            case POP:
                clearLog();
                visualizePop();
                break;
        }
    }

    @Override
    public void onDataRecieved(Object data) {
        this.stack = (Stack) data;
    }

    public void setData(final Stack stack) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.setData(stack);
            }
        });
        start();
        prepareHandler(this);
        sendData(stack);
    }

    private void updateData(final Stack stack) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                visualizer.setData(stack);
            }
        });

    }
}