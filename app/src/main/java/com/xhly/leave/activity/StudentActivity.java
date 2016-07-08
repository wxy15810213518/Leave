package com.xhly.leave.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.xhly.leave.R;
import com.xhly.leave.activity.base.ScrollBaseActivity;
import com.xhly.leave.adapter.CommonHeaderRecyclerAdapter;
import com.xhly.leave.base.ViewHolder;
import com.xhly.leave.dao.StudentDao;
import com.xhly.leave.model.Student;
import com.xhly.leave.util.Constants;
import com.xhly.leave.util.PinYinUtils;
import com.xhly.leave.util.TimeUtil;
import com.xhly.leave.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Date;

public class StudentActivity extends ScrollBaseActivity implements ObservableScrollViewCallbacks {
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private View mImageView;
    private View mOverlayView;
    private View mRecyclerViewBackground;
    private TextView mTitleView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private ImageView iv_head;
    private ArrayList<Student> data;
    private Student student;
    private TextView tv_content;
    private int agreeCnt = 0;
    private int refuseCnt = 0;
    private int noHandCnt= 0;
    private long totalTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexiblespacewithimagerecyclerview);
        student = (Student) getIntent().getSerializableExtra("student");
        setTitle(student.getName());
        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_head.setImageResource(Constants.icon[getFirstLetter(student.getName())]);
        tv_content = (TextView) findViewById(R.id.tv_content);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = getActionBarSize();

        ObservableRecyclerView recyclerView = (ObservableRecyclerView) findViewById(R.id.recycler);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        final View headerView = LayoutInflater.from(this).inflate(R.layout.recycler_header, null);
        headerView.post(new Runnable() {
            @Override
            public void run() {
                headerView.getLayoutParams().height = mFlexibleSpaceImageHeight;
            }
        });

        initData();

        tv_content.setText("请假:" + data.size() + "次\n同意:" + agreeCnt + "次\n拒绝:" + refuseCnt + "次\n未处理:" + noHandCnt + "次\n总时长:" + TimeUtil.getHourAndMinute(totalTime));

        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        recyclerView.setAdapter(new CommonHeaderRecyclerAdapter(this, data, R.layout.person_item_layout, headerView) {
            @Override
            public void convert(ViewHolder holder, final int position) {
               if(position>0){
                   holder.setText(R.id.tv_content, data.get(position-1).getName());
                   if (data.get(position-1).getHandlerType() == 1) {
                       holder.setVisibility(R.id.iv_handType, View.VISIBLE);
                       holder.setImageResource(R.id.iv_handType, R.drawable.agree1);
                   } else if (data.get(position-1).getHandlerType() == 2) {
                       holder.setVisibility(R.id.iv_handType, View.VISIBLE);
                       holder.setImageResource(R.id.iv_handType, R.drawable.refuse);
                   } else if (data.get(position-1).getHandlerType() == 0) {
                       holder.setVisibility(R.id.iv_handType, View.INVISIBLE);
                   }
                   holder.setText(R.id.tv_content,"开始时间:"+TimeUtil.dateToLocal(new Date(data.get(position-1).getStartDate()))+"\n结束时间:"+TimeUtil.dateToLocal(new Date(data.get(position-1).getEndDate()))+"\n时长:"+TimeUtil.getHourAndMinute(data.get(position-1).getEndDate()-data.get(position-1).getStartDate())+"\n原因:"+Constants.reason[data.get(position-1).getReasonType()]+"-"+data.get(position-1).getDesc());
                   holder.setOnclickListener(R.id.tv_content, new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Toast.makeText(StudentActivity.this, position - 1 + "-" + data.get(position - 1), Toast.LENGTH_SHORT).show();
                       }
                   });
               }
            }
        });
        //setDummyDataWithHeader(data, recyclerView, null);


        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);

        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(getTitle());
        setTitle(null);

        // mRecyclerViewBackground makes RecyclerView's background except header view.
        mRecyclerViewBackground = findViewById(R.id.list_background);

        //since you cannot programmatically add a header view to a RecyclerView we added an empty view as the header
        // in the adapter and then are shifting the views OnCreateView to compensate
        final float scale = 1 + MAX_TEXT_SCALE_DELTA;
        mRecyclerViewBackground.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationY(mRecyclerViewBackground, mFlexibleSpaceImageHeight);
            }
        });
        ViewHelper.setTranslationY(mOverlayView, mFlexibleSpaceImageHeight);
        mTitleView.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationY(mTitleView, (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale));
                ViewHelper.setPivotX(mTitleView, 0);
                ViewHelper.setPivotY(mTitleView, 0);
                ViewHelper.setScaleX(mTitleView, scale);
                ViewHelper.setScaleY(mTitleView, scale);
            }
        });
    }

    private void initData() {
        data = (ArrayList<Student>) new StudentDao().getAllByName(student.getName());
        for (int i = 0; i < data.size(); i++) {
            switch (data.get(i).getHandlerType()){
                case 0:
                    noHandCnt++;
                    break;
                case 1:
                    agreeCnt++;
                    break;
                case 2:
                    refuseCnt++;
                    break;
            }

            totalTime+=data.get(i).getEndDate()-data.get(i).getStartDate();
        }
    }

    private int getFirstLetter(String name) {
        int index = 0;
        char first = name.charAt(0);
        if(first>='A'&&first<='Z'){
            index = first -'A';
        }else if(first>='a'&&first<='z'){
            index = first -'a';
        }else {
            index = PinYinUtils.getPinYin(name).charAt(0) - 'A';
        }
        return index;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mRecyclerViewBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }
    }
    
    public void click(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+student.getNumber()));
        startActivity(intent);

    }
}
