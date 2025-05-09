package com.example.myapplicationx429as;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Money extends AppCompatActivity {

    private EditText editTextRMB;
    private TextView textViewResult;
    private Button buttonDollar, buttonEuro, buttonWon, buttonConfig;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money2);

        editTextRMB    = findViewById(R.id.editTextRMB);
        textViewResult = findViewById(R.id.textViewResult);
        buttonDollar   = findViewById(R.id.buttonDollar);
        buttonEuro     = findViewById(R.id.buttonEuro);
        buttonWon      = findViewById(R.id.buttonWon);
        buttonConfig   = findViewById(R.id.buttonConfig);
        handler        = new Handler();

        Intent intent = getIntent();
        String currencyName = intent.getStringExtra("currency_name");
        String exchangeRate = intent.getStringExtra("exchange_rate");

        if (currencyName != null && exchangeRate != null) {
            
            buttonDollar.setVisibility(View.GONE);
            buttonEuro.setVisibility(View.GONE);
            buttonWon.setVisibility(View.GONE);

            textViewResult.setText(currencyName);

            buttonConfig.setText("计算");
            buttonConfig.setOnClickListener(v -> {
                String rmbStr = editTextRMB.getText().toString().trim();
                if (rmbStr.isEmpty()) {
                    textViewResult.setText("请输入人民币金额");
                    return;
                }

                try {
                    float rmb = Float.parseFloat(rmbStr);
                    float rate = Float.parseFloat(exchangeRate);
                    float result = rmb * rate / 100f;

                    String output = String.format("当前汇率：%s\n%.2f RMB ≈ %.2f %s",
                            exchangeRate, rmb, result, currencyName);
                    textViewResult.setText(output);
                } catch (NumberFormatException e) {
                    textViewResult.setText("汇率或金额格式错误");
                }
            });
        } else {
            // 原有按钮功能，点击后抓取汇率并计算
            buttonDollar.setOnClickListener(v -> fetchAndConvert("美元"));
            buttonEuro.setOnClickListener(v -> fetchAndConvert("欧元"));
            buttonWon.setOnClickListener(v -> fetchAndConvert("韩国元"));

            // 跳转到列表页
            buttonConfig.setOnClickListener(v -> {
                Intent listIntent = new Intent(Money.this, MyListActivity.class);
                startActivity(listIntent);
            });
        }
    }

    // 原始 Jsoup 获取逻辑，支持按钮汇率转换
    private void fetchAndConvert(String currencyName) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/")
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();
                Elements tds = doc.select("table").get(1).select("td");

                String rateStr = null;
                for (int i = 0; i + 5 < tds.size(); i += 8) {
                    if (tds.get(i).text().contains(currencyName)) {
                        rateStr = tds.get(i + 5).text();
                        break;
                    }
                }

                if (rateStr == null) {
                    handler.post(() -> textViewResult.setText("未找到 " + currencyName + " 汇率"));
                    return;
                }

                String rmbStr = editTextRMB.getText().toString().trim();
                if (rmbStr.isEmpty()) {
                    handler.post(() -> textViewResult.setText("请输入人民币金额"));
                    return;
                }

                float rmb = Float.parseFloat(rmbStr);
                float rate = Float.parseFloat(rateStr);
                float res = rmb * rate / 100f;
                String out = String.format("当前%s汇率：%s\n%.2f RMB ≈ %.2f %s",
                        currencyName, rateStr, rmb, res, currencyName);
                handler.post(() -> textViewResult.setText(out));

            } catch (Exception e) {
                handler.post(() -> textViewResult.setText("访问失败：" + e.getMessage()));
            }
        }).start();
    }
}
