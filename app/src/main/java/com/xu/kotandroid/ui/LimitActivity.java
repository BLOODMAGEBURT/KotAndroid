package com.xu.kotandroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.mmkv.MMKV;
import com.xu.kotandroid.R;
import com.xu.kotandroid.base.Const;
import com.xu.kotandroid.util.DateUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.function.Consumer;

public class LimitActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * 一般来说： 当天数字  <= 月的数字
     */
    private int daySize = 2;
    private int monthSize = 5;

    MMKV kv = MMKV.defaultMMKV();

    LimitedQueue<Long> queue = new LimitedQueue<>(monthSize);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit);

        findViewById(R.id.showToast).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.showToast) {
            long now = System.currentTimeMillis();
            String todayStr = DateUtil.date2Str(new Date(now), DateUtil.FORMAT_YYMD);

            /**
             * 1. 先判断当天的数量
             *
             * 当天数量存在sp或者数据库中
             *
             * key1 = 最后一次时间戳 startAdLastDate
             * key2 = 最后一天弹出次数 startAdDaily
             *
             *
             * 2. 再判断当月的数量
             */


            String lastDate = kv.decodeString("startAdLastDate", "");


            if (todayStr.equals(lastDate)) {
                // 同一天，判断当天次数是否已满
                if (kv.decodeInt("startAdDaily") >= daySize) {
                    return;
                }

            } else {
                // 非同一天，次数清零
                kv.encode("startAdDaily", 0);
            }

            // 队首时间
            Long first = queue.getFirst();
            if (queue.size() >= monthSize && DateUtil.addDay(first, 30) > now) {
                // 队列已满，而且没过30天
                return;
            }



            Toast.makeText(this, "30天之外，继续弹", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "小于monthSize次，所以弹", Toast.LENGTH_SHORT).show();


            kv.encode("startAdLastDate", todayStr);
            int old = kv.decodeInt("startAdDaily");
            kv.encode("startAdDaily", old + 1);

            queue.add(now);

            Log.d("here", "size:" + queue.size());

            queue.forEach(aLong -> Log.d("here", "longN:" + aLong));

        }
    }


    class LimitedQueue<E> extends LinkedList<E> {

        private int limit;

        public LimitedQueue(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean add(E o) {
            super.add(o);
            while (size() > limit) {
                super.remove();
            }
            return true;
        }
    }
}