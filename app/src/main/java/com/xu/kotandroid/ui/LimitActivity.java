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

import java.util.LinkedList;
import java.util.function.Consumer;

public class LimitActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * 一般来说： 当天数字  <= 月的数字
     */
    private int daySize = 2;
    private int monthSize = 5;

    LimitedQueue<Long> queue = new LimitedQueue<>(monthSize);
    LimitedQueue<Long> dayQueue = new LimitedQueue<>(daySize);

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

            long nowStart = DateUtil.dayStartTime(now);

            Log.d("here", "0点:" + nowStart);
            Log.d("here", "下一天0点:" + DateUtil.addDay(nowStart, 1));

            /**
             * 1. 先判断当天的数量,
             * 当天数量存在sp或者数据库中
             * key = TODAY_SHOW_COUNT
             * value = 当前时间戳｜当天弹出次数
             *
             * 2. 再判断当月的数量
             */

            if (MMKV.defaultMMKV() != null) {


                MMKV.defaultMMKV().encode("TODAY_SHOW_COUNT", now + "|" + 2);


            }


            if (dayQueue.size() >= daySize) {
                return;
            }

            dayQueue.add(nowStart);


            if (queue.size() >= monthSize) {
                // 队首时间
                Long first = queue.getFirst();
                // 判断时间是否在30天之外
                if (DateUtil.addDay(first, 30) < now) {
                    queue.add(nowStart);
                    Toast.makeText(this, "30天之外，继续弹", Toast.LENGTH_SHORT).show();
                }
                return;
            }


            Toast.makeText(this, "小于3次，所以弹", Toast.LENGTH_SHORT).show();
            queue.add(nowStart);

            Log.d("here", "size:" + queue.size());

            queue.forEach(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) {
                    Log.d("here", "longN:" + aLong);
                }
            });

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