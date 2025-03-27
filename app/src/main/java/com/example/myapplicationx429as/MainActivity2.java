package com.example.myapplicationx429as;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    private EditText editDollar, editEuro, editWon;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money3); // 加载 activity_money3.xml

        // 1. 获取控件引用
        editDollar = findViewById(R.id.editDollar);
        editEuro = findViewById(R.id.editEuro);
        editWon = findViewById(R.id.editWon);
        buttonSave = findViewById(R.id.buttonSave);

        // 2. 获取从主页面传来的数据
        Intent intent = getIntent();
        float dollarRate = intent.getFloatExtra("dollar_rate", 0.15f);
        float euroRate = intent.getFloatExtra("euro_rate", 0.13f);
        float wonRate = intent.getFloatExtra("won_rate", 165f);

        // 3. 显示到对应的输入框
        editDollar.setText(String.valueOf(dollarRate));
        editEuro.setText(String.valueOf(euroRate));
        editWon.setText(String.valueOf(wonRate));

        // 4. 保存按钮
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 读取修改后的汇率
                float newDollar = Float.parseFloat(editDollar.getText().toString());
                float newEuro = Float.parseFloat(editEuro.getText().toString());
                float newWon = Float.parseFloat(editWon.getText().toString());

                // 封装到 Intent 中
                Intent backIntent = new Intent();
                backIntent.putExtra("key_dollar_rate", newDollar);
                backIntent.putExtra("key_euro_rate", newEuro);
                backIntent.putExtra("key_won_rate", newWon);

                // 设置结果并结束
                setResult(RESULT_OK, backIntent);
                finish();
            }
        });
    }
}
