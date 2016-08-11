package com.frewen.dashboard.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.frewen.dashboard.demo.view.DashboardView;

public class MainActivity extends AppCompatActivity {
    private DashboardView mDashBoardView;
    private Button mIncreaseBtn;
    private Button mDecreaseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initCallBackListener();
    }

    private void initView() {
        mDashBoardView = (DashboardView) findViewById(R.id.sport_view);
        mDashBoardView.setTargetPercent(30);

        mIncreaseBtn = (Button) findViewById(R.id.sport_btn);
        mDecreaseBtn = (Button) findViewById(R.id.sport_down_btn);

    }


    private void initCallBackListener() {
        mIncreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDashBoardView.increaseNumber(10);
            }
        });

        mDecreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDashBoardView.decreaseNumber(10);
            }
        });
    }
}
