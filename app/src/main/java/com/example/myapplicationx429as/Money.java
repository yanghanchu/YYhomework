package com.example.myapplicationx429as;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Money extends AppCompatActivity {

    private EditText editTextRMB;
    private TextView textViewResult;
    private Button buttonDollar, buttonEuro, buttonWon;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money2);

        editTextRMB = findViewById(R.id.editTextRMB);
        textViewResult = findViewById(R.id.textViewResult);
        buttonDollar = findViewById(R.id.buttonDollar);
        buttonEuro = findViewById(R.id.buttonEuro);
        buttonWon = findViewById(R.id.buttonWon);

        handler = new Handler();

        // 美元
        buttonDollar.setOnClickListener(view -> fetchAndConvert("美元"));
        // 欧元
        buttonEuro.setOnClickListener(view -> fetchAndConvert("欧元"));
        // 韩元
        buttonWon.setOnClickListener(view -> fetchAndConvert("韩国元"));
    }

    /**
     * 获取网页汇率并计算兑换结果
     */
    private void fetchAndConvert(String currencyName) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
                Elements tables = doc.getElementsByTag("table");

                String rateStr = null;

                for (Element table : tables) {
                    Elements tds = table.getElementsByTag("td");
                    for (int i = 0; i + 5 < tds.size(); i += 8) {
                        String currency = tds.get(i).text();
                        String rate = tds.get(i + 5).text(); // 中行折算价，一般为第6列
                        if (currency.contains(currencyName)) {
                            rateStr = rate;
                            break;
                        }
                    }
                    if (rateStr != null) break;
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
                float result = rmb * rate / 100;

                String finalResult = String.format("当前%s汇率：%s\n%.2f 人民币 ≈ %.2f %s", currencyName, rateStr, rmb, result, currencyName);
                handler.post(() -> textViewResult.setText(finalResult));

            } catch (Exception e) {
                handler.post(() -> textViewResult.setText("访问失败：" + e.getMessage()));
            }
        }).start();
    }
}
