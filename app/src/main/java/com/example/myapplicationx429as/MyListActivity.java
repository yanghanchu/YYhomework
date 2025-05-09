package com.example.myapplicationx429as;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class MyListActivity extends ListActivity {

    ArrayList<HashMap<String, String>> listItems = new ArrayList<>();
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setOnItemClickListener(itemClickListener);

        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/")
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();
                Elements tds = doc.select("table").get(1).select("td");

                for (int i = 0; i + 5 < tds.size(); i += 8) {
                    String name = tds.get(i).text();
                    String rate = tds.get(i + 5).text();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("ItemTitle", name);
                    map.put("ItemDetail", rate);
                    listItems.add(map);
                }

                handler.post(() -> {
                    SimpleAdapter adapter = new SimpleAdapter(
                            MyListActivity.this,
                            listItems,
                            R.layout.list_item,
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.itemTitle, R.id.itemDetail}
                    );
                    setListAdapter(adapter);
                });

            } catch (Exception e) {
                handler.post(() -> Toast.makeText(MyListActivity.this, "网络加载失败", Toast.LENGTH_SHORT).show());
                Log.e("MyListActivity", "Jsoup Error: " + e.getMessage());
            }
        }).start();
    }

    private final AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
        HashMap<String, String> item = (HashMap<String, String>) getListView().getItemAtPosition(position);
        String name = item.get("ItemTitle");
        String rate = item.get("ItemDetail");

        Intent intent = new Intent(MyListActivity.this, Money.class);
        intent.putExtra("currency_name", name);
        intent.putExtra("exchange_rate", rate);
        startActivity(intent);
    };
}
