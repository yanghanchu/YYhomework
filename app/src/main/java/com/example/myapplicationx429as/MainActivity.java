package com.example.myapplicationx429as;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private EditText edtWeight;
    private EditText edtHeight;
    private TextView tvResult;
    private TextView tvAdvice;
    private TextView tvTime;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtWeight = findViewById(R.id.edtWeight);
        edtHeight = findViewById(R.id.edtHeight);
        tvResult = findViewById(R.id.tvResult);
        tvAdvice = findViewById(R.id.tvAdvice);
        tvTime = findViewById(R.id.tvTime);

        startUpdatingTime();
    }

    private void startUpdatingTime() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // 获取当前时间
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                tvTime.setText("时间：" + currentTime);

                // 每秒更新一次
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void calculateBMI(View view) {
        String weightInput = edtWeight.getText().toString();
        String heightInput = edtHeight.getText().toString();

        if (!weightInput.isEmpty() && !heightInput.isEmpty()) {
            double weight = Double.parseDouble(weightInput);
            double height = Double.parseDouble(heightInput);

            if (height > 0) {
                double bmi = weight / (height * height);

                // 保留两位小数
                tvResult.setText(String.format(Locale.getDefault(), "结果为：%.2f", bmi));

                // 健康建议
                tvAdvice.setText("健康建议：" + getHealthAdvice(bmi));
            } else {
                tvResult.setText("身高必须大于0");
            }
        } else {
            tvResult.setText("请输入身高和体重");
        }
    }

    private String getHealthAdvice(double bmi) {
        if (bmi < 18.5) {
            return "体重过轻，多吃点。";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "体重正常，很棒，继续保持。";
        } else if (bmi >= 24.9 && bmi < 29.9) {
            return "超重，少吃点，多运动。";
        } else {
            return "肥胖，医院看看去。";
        }
    }
}
