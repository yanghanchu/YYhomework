package com.example.myapplicationx429as;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Score extends AppCompatActivity {

    // 两个队伍的分数
    private int scoreTeamA = 0;
    private int scoreTeamB = 0;

    // 控件引用
    private TextView scoreTeamATextView, scoreTeamBTextView;
    private Button buttonA1, buttonA2, buttonA3;
    private Button buttonB1, buttonB2, buttonB3;
    private Button buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 系统会自动根据当前方向加载 res/layout/score.xml 或 res/layout-land/score.xml
        setContentView(R.layout.activity_score);

        // 获取控件引用（请确保两个布局文件中ID一致）
        scoreTeamATextView = findViewById(R.id.scoreTeamA);
        scoreTeamBTextView = findViewById(R.id.scoreTeamB);

        buttonA1 = findViewById(R.id.buttonA1);
        buttonA2 = findViewById(R.id.buttonA2);
        buttonA3 = findViewById(R.id.buttonA3);

        buttonB1 = findViewById(R.id.buttonB1);
        buttonB2 = findViewById(R.id.buttonB2);
        buttonB3 = findViewById(R.id.buttonB3);

        buttonReset = findViewById(R.id.buttonReset);

        // 恢复状态（例如屏幕旋转时）
        if (savedInstanceState != null) {
            scoreTeamA = savedInstanceState.getInt("scoreTeamA", 0);
            scoreTeamB = savedInstanceState.getInt("scoreTeamB", 0);
            updateScore();
        }

        // 设置 Team A 按钮监听器：分别加 1、2、3 分
        buttonA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreTeamA += 1;
                updateScore();
            }
        });
        buttonA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreTeamA += 2;
                updateScore();
            }
        });
        buttonA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreTeamA += 3;
                updateScore();
            }
        });

        // 设置 Team B 按钮监听器：分别加 1、2、3 分
        buttonB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreTeamB += 1;
                updateScore();
            }
        });
        buttonB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreTeamB += 2;
                updateScore();
            }
        });
        buttonB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreTeamB += 3;
                updateScore();
            }
        });

        // 设置 RESET 按钮监听器：重置分数
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetScores();
            }
        });
    }

    // 更新界面上显示的分数
    private void updateScore() {
        scoreTeamATextView.setText(String.valueOf(scoreTeamA));
        scoreTeamBTextView.setText(String.valueOf(scoreTeamB));
    }

    // 重置分数的方法
    private void resetScores() {
        scoreTeamA = 0;
        scoreTeamB = 0;
        updateScore();
    }

    // 保存当前状态，防止屏幕旋转时数据丢失
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("scoreTeamA", scoreTeamA);
        outState.putInt("scoreTeamB", scoreTeamB);
    }
}
