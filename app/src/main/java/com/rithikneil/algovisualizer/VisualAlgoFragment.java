package com.rithikneil.algovisualizer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rithikneil.algovisualizer.algorithm.Algorithm;
import com.rithikneil.algovisualizer.algorithm.graph.GraphTraversalAlgorithm;
import com.rithikneil.algovisualizer.algorithm.list.LinkedList;
import com.rithikneil.algovisualizer.algorithm.list.Stack;
import com.rithikneil.algovisualizer.algorithm.search.BinarySearch;
import com.rithikneil.algovisualizer.algorithm.search.LinearSearch;
import com.rithikneil.algovisualizer.visualizer.AlgorithmVisualizer;
import com.rithikneil.algovisualizer.visualizer.BinarySearchVisualizer;
import com.rithikneil.algovisualizer.visualizer.LinkedListControls;
import com.rithikneil.algovisualizer.visualizer.LinkedListVisualizer;
import com.rithikneil.algovisualizer.visualizer.StackControls;
import com.rithikneil.algovisualizer.visualizer.StackVisualizer;
import com.rithikneil.algovisualizer.visualizer.graph.DirectedGraphVisualizer;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;

import java.util.ArrayList;
import java.util.List;

public class VisualAlgoFragment extends Fragment {

    FloatingActionButton fab;
    BottomBar bottomBar;
    AppBarLayout appBarLayout;

    LogFragment logFragment;
    CodeFragment codeFragment;
    AlgoDescriptionFragment algoFragment;
    ViewPager viewPager;

    Algorithm algorithm;

    String startCommand = Algorithm.COMMAND_START_ALGORITHM;

    public static VisualAlgoFragment newInstance(String algorithm) {
        VisualAlgoFragment fragment = new VisualAlgoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Algorithm.KEY_ALGORITHM, algorithm);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visual_algo, container, false);

        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_bar);
        bottomBar = BottomBar.attachShy((CoordinatorLayout) rootView.findViewById(R.id.coordinator), savedInstanceState);
        bottomBar.noNavBarGoodness();
        bottomBar.noTabletGoodness();

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);


        bottomBar.setItems(
                new BottomBarTab(R.drawable.ic_wb_incandescent_white_24dp, "Details"),
                new BottomBarTab(R.drawable.ic_short_text_white_24dp, "Execution"),
                new BottomBarTab(R.drawable.ic_code_white_24dp, "Code")
        );

        bottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
                if (position == 2) {
                    bottomBar.hide();
                }
            }

            @Override
            public void onTabReSelected(int position) {

            }
        });

        logFragment = LogFragment.newInstance();
        codeFragment = CodeFragment.newInstance(getArguments().getString(Algorithm.KEY_ALGORITHM));
        algoFragment = AlgoDescriptionFragment.newInstance(getArguments().getString(Algorithm.KEY_ALGORITHM));

        setupFragment(getArguments().getString(Algorithm.KEY_ALGORITHM));

        return rootView;
    }

    public void setStartCommand(String startCommand) {
        this.startCommand = startCommand;
    }

    @SuppressLint("RestrictedApi")
    public void setupFragment(String algorithmKey) {

        viewPager.setOffscreenPageLimit(3);
        bottomBar.selectTabAtPosition(0, false);
        setupViewPager(viewPager);

        codeFragment.setCode(algorithmKey);
        algoFragment.setCodeDesc(algorithmKey);

        assert algorithmKey != null;

        final AlgorithmVisualizer visualizer;

        appBarLayout.removeAllViewsInLayout();

        View toolbar = LayoutInflater.from(getActivity()).inflate(R.layout.toolbar, appBarLayout, false);
        appBarLayout.addView(toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar((androidx.appcompat.widget.Toolbar) toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert ab != null;
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        fab.setVisibility(View.VISIBLE);


        switch (algorithmKey) {
            case Algorithm.BINARY_SEARCH:
                visualizer = new BinarySearchVisualizer(getActivity());
                appBarLayout.addView(visualizer);
                algorithm = new BinarySearch((BinarySearchVisualizer) visualizer, getActivity(), logFragment);
                ((BinarySearch) algorithm).setData(DataUtils.createArray(10, true));
                break;
            case Algorithm.LINEAR_SEARCH:
                visualizer = new BinarySearchVisualizer(getActivity());
                appBarLayout.addView(visualizer);
                algorithm = new LinearSearch((BinarySearchVisualizer) visualizer, getActivity(), logFragment);
                ((LinearSearch) algorithm).setData(DataUtils.createArray(10, false));
                break;
            case Algorithm.LINKED_LIST:
                visualizer = new LinkedListVisualizer(getActivity());
                LinkedListControls controls = new LinkedListControls(getActivity(), bottomBar, fab);
                appBarLayout.addView(visualizer);
                appBarLayout.addView(controls);
                algorithm = new LinkedList((LinkedListVisualizer) visualizer, getActivity(), logFragment);
                ((LinkedList) algorithm).setData(DataUtils.createLinkedList());
                controls.setLinkedList((LinkedList) algorithm);
                break;
            case Algorithm.STACK:
                visualizer = new StackVisualizer(getActivity());
                StackControls stackcontrols = new StackControls(getActivity(), bottomBar, fab);
                appBarLayout.addView(visualizer);
                appBarLayout.addView(stackcontrols);
                algorithm = new Stack(5, (StackVisualizer) visualizer, getActivity(), logFragment);
                ((Stack) algorithm).setData(DataUtils.createStack());
                stackcontrols.setStack((Stack) algorithm);
                fab.setVisibility(View.GONE);
                break;
            case Algorithm.BFS:
            case Algorithm.DFS:
                visualizer = new DirectedGraphVisualizer(getActivity());
                appBarLayout.addView(visualizer);
                algorithm = new GraphTraversalAlgorithm((DirectedGraphVisualizer) visualizer, getActivity(), logFragment);
                ((GraphTraversalAlgorithm) algorithm).setData(DataUtils.createDirectedGraph());
                break;
            default:
                visualizer = null;
        }

        Algorithm.setInterval(1000);
        algorithm.setStarted(false);
        fab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        logFragment.clearLog();

        algorithm.setCompletionListener(new AlgoCompletionListener() {
            @Override
            public void onAlgoCompleted() {
                fab.setImageResource(R.drawable.ic_settings_backup_restore_white_24dp);
                if (visualizer != null)
                    visualizer.onCompleted();

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!algorithm.isStarted()) {
                    algorithm.sendMessage(startCommand);
                    fab.setImageResource(R.drawable.ic_pause_white_24dp);
                    logFragment.clearLog();
                    bottomBar.selectTabAtPosition(1, true);//move to log fragment
                } else {
                    if (algorithm.isPaused()) {
                        algorithm.setPaused(false);
                        fab.setImageResource(R.drawable.ic_pause_white_24dp);
                    } else {
                        algorithm.setPaused(true);
                        fab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                    }
                }
            }
        });

        View shadow = LayoutInflater.from(getActivity()).inflate(R.layout.shadow, appBarLayout, false);
        appBarLayout.addView(shadow);

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(algoFragment, "Algo");
        adapter.addFragment(logFragment, "Log");
        adapter.addFragment(codeFragment, "Code");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position, false);
                bottomBar.hide();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        private Adapter(FragmentManager fm) {
            super(fm);
        }

        private void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
