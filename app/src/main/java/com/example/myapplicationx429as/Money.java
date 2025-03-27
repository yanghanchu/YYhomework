package com.example.myapplicationx429as;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Money extends AppCompatActivity {

    private EditText editTextRMB;
    private TextView textViewResult;
    private Button buttonDollar, buttonEuro, buttonWon, buttonConfig;

    // 定义汇率
    private float dollarRate = 0.15f;  // 默认：1 RMB = 0.15 USD
    private float euroRate = 0.13f;    // 默认：1 RMB = 0.13 EUR
    private float wonRate = 165f;      // 默认：1 RMB = 165 WON

    private static final int REQUEST_CODE_CONFIG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加载布局：activity_money2.xml
        setContentView(R.layout.activity_money2);

        editTextRMB = findViewById(R.id.editTextRMB);
        textViewResult = findViewById(R.id.textViewResult);

        buttonDollar = findViewById(R.id.buttonDollar);
        buttonEuro = findViewById(R.id.buttonEuro);
        buttonWon = findViewById(R.id.buttonWon);

        buttonConfig = findViewById(R.id.buttonConfig);

        buttonDollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertCurrency(dollarRate);
            }
        });

        buttonEuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertCurrency(euroRate);
            }
        });

        buttonWon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertCurrency(wonRate);
            }
        });

        // 跳转到配置页面
        buttonConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent configIntent = new Intent(Money.this, MainActivity2.class);
                // 把当前汇率传给配置页面
                configIntent.putExtra("dollar_rate", dollarRate);
                configIntent.putExtra("euro_rate", euroRate);
                configIntent.putExtra("won_rate", wonRate);

                // 启动配置页面，并等待其返回结果
                startActivityForResult(configIntent, REQUEST_CODE_CONFIG);
            }
        });
    }

    /**
     * 根据传入的汇率进行换算
     */
    private void convertCurrency(float rate) {
        String rmbStr = editTextRMB.getText().toString().trim();
        if (rmbStr.isEmpty()) {
            textViewResult.setText("请输入金额");
            return;
        }

        float rmb = Float.parseFloat(rmbStr);
        float result = rmb * rate;
        textViewResult.setText(String.format("%.2f", result));
    }

    /**
     * 接收配置页面返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CONFIG && resultCode == RESULT_OK) {
            // 取出最新汇率
            dollarRate = data.getFloatExtra("key_dollar_rate", 0.15f);
            euroRate = data.getFloatExtra("key_euro_rate", 0.13f);
            wonRate = data.getFloatExtra("key_won_rate", 165f);
        }
    }
}
