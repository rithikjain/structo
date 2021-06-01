/*
 * Copyright (C) 2016 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.rithikneil.algovisualizer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.rithikneil.algovisualizer.algorithm.Algorithm;
import com.rithikneil.algovisualizer.algorithm.graph.GraphTraversalAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by naman on 03/06/16.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        final VisualAlgoFragment algoFragment = VisualAlgoFragment.newInstance(Algorithm.BINARY_SEARCH);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, algoFragment).commit();

        prepareListData();
        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);

        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                switch (groupPosition) {
                    case 0:
                        switch (childPosition) {
                            case 0:
                                algoFragment.setupFragment(Algorithm.BINARY_SEARCH);
                                break;
                            case 1:
                                algoFragment.setupFragment(Algorithm.LINEAR_SEARCH);
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition) {
                            case 0:
                                algoFragment.setupFragment(Algorithm.LINKED_LIST);
                                break;
                            case 1:
                                algoFragment.setupFragment(Algorithm.STACK);
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition) {
                            case 0:
                                algoFragment.setStartCommand(GraphTraversalAlgorithm.TRAVERSE_BFS);
                                algoFragment.setupFragment(Algorithm.BFS);
                                break;
                            case 1:
                                algoFragment.setStartCommand(GraphTraversalAlgorithm.TRAVERSE_DFS);
                                algoFragment.setupFragment(Algorithm.DFS);
                                break;
                        }
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setName("Search");
        listDataHeader.add(item1);

        ExpandedMenuModel item4 = new ExpandedMenuModel();
        item4.setName("List");
        listDataHeader.add(item4);

        ExpandedMenuModel item5 = new ExpandedMenuModel();
        item5.setName("Graph");
        listDataHeader.add(item5);

        List<String> heading1 = new ArrayList<>();
        heading1.add("Binary search");
        heading1.add("Linear Search");


        List<String> heading2 = new ArrayList<String>();
        heading2.add("Linked List");
        heading2.add("Stack");

        List<String> heading3 = new ArrayList<String>();
        heading3.add("BFS Traversal");
        heading3.add("DFS Travsersal");

        listDataChild.put(listDataHeader.get(0), heading1);
        listDataChild.put(listDataHeader.get(1), heading2);
        listDataChild.put(listDataHeader.get(2), heading3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}