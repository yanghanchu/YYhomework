package com.example.myapplicationx429as;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyListActivity extends ListActivity {
    private RateManager mgr;
    private String today;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        mgr = new RateManager(this);
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        // 1. 先加载本地缓存
        List<RateItem> cache = mgr.list(today);
        if (!cache.isEmpty()) {
            bindList(cache);
        }
        // 2. 无论如何都去网络爬取全量，拉完再刷新
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/")
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();
                Elements tds = doc.select("table").get(1).select("td");
                List<RateItem> all = new ArrayList<>();
                for (int i = 0; i + 5 < tds.size(); i += 8) {
                    String name = tds.get(i).text();
                    float rate = Float.parseFloat(tds.get(i + 5).text());
                    RateItem r = new RateItem();
                    r.setCurrencyName(name);
                    r.setRate(rate);
                    r.setDate(today);
                    mgr.add(r);
                    all.add(r);
                }
                handler.post(() -> bindList(all));
            } catch (Exception e) {
                handler.post(() ->
                        Toast.makeText(this,
                                "网络加载失败：" + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    /** 把数据绑定到 ListView **/
    private void bindList(List<RateItem> data) {
        ArrayList<HashMap<String,String>> listItems = new ArrayList<>();
        for (RateItem r : data) {
            HashMap<String,String> m = new HashMap<>();
            m.put("ItemTitle",  r.getCurrencyName());
            m.put("ItemDetail", String.format(Locale.getDefault(), "%.2f", r.getRate()));
            listItems.add(m);
        }
        SimpleAdapter adapter = new SimpleAdapter(
                this, listItems,
                R.layout.list_item,
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle, R.id.itemDetail}
        );
        setListAdapter(adapter);

        getListView().setOnItemClickListener((parent, view, pos, id) -> {
            @SuppressWarnings("unchecked")
            HashMap<String,String> it = (HashMap<String,String>) getListView()
                    .getItemAtPosition(pos);
            Intent intent = new Intent(this, Money.class);
            intent.putExtra("currency_name", it.get("ItemTitle"));
            intent.putExtra("exchange_rate", it.get("ItemDetail"));
            startActivity(intent);
        });
    }
}
