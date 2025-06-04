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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Money extends AppCompatActivity {
    private EditText editTextRMB;
    private TextView textViewResult;
    private Button buttonDollar, buttonEuro, buttonWon, buttonConfig;
    private Handler handler;
    private RateManager mgr;
    private String today;

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

        mgr = new RateManager(this);
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        Intent intent = getIntent();
        String currencyName = intent.getStringExtra("currency_name");
        String exchangeRate = intent.getStringExtra("exchange_rate");
        if (currencyName != null && exchangeRate != null) {
            buttonDollar.setVisibility(View.GONE);
            buttonEuro.setVisibility(View.GONE);
            buttonWon.setVisibility(View.GONE);
            textViewResult.setText("当前汇率：" + exchangeRate);
            buttonConfig.setText("计算");
            buttonConfig.setOnClickListener(v ->
                    convertAndShow(currencyName, Float.parseFloat(exchangeRate))
            );
        } else {
            buttonDollar.setOnClickListener(v -> fetchAndConvert("美元"));
            buttonEuro.setOnClickListener(v -> fetchAndConvert("欧元"));
            buttonWon.setOnClickListener(v -> fetchAndConvert("韩国元"));
            buttonConfig.setOnClickListener(v ->
                    startActivity(new Intent(Money.this, MyListActivity.class))
            );
        }
    }

    private void convertAndShow(String currencyName, float rate) {
        String str = editTextRMB.getText().toString().trim();
        if (str.isEmpty()) {
            textViewResult.setText("请输入人民币金额");
            return;
        }
        try {
            float rmb = Float.parseFloat(str);
            float res = rmb * rate / 100f;
            String out = String.format(Locale.getDefault(),
                    "当前汇率：%.2f\n%.2f RMB ≈ %.2f %s",
                    rate, rmb, res, currencyName);
            textViewResult.setText(out);
        } catch (NumberFormatException e) {
            textViewResult.setText("金额格式错误");
        }
    }

    private void fetchAndConvert(String currencyName) {
        RateItem cached = mgr.find(currencyName, today);
        if (cached != null) {
            convertAndShow(currencyName, cached.getRate());
            return;
        }
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
                    handler.post(() ->
                            textViewResult.setText("未找到 " + currencyName + " 汇率")
                    );
                    return;
                }
                float rate = Float.parseFloat(rateStr);
                RateItem item = new RateItem();
                item.setCurrencyName(currencyName);
                item.setRate(rate);
                item.setDate(today);
                mgr.add(item);

                handler.post(() -> convertAndShow(currencyName, rate));
            } catch (Exception e) {
                handler.post(() ->
                        textViewResult.setText("访问失败：" + e.getMessage())
                );
            }
        }).start();
    }
}
