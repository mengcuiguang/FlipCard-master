package com.meng.flipcard;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.meng.flipcard.wcl_flip_anim_demo.R;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_fl_container)
    FrameLayout mFlContainer;
    @Bind(R.id.main_fl_card_back)
    FrameLayout mFlCardBack;
    @Bind(R.id.main_fl_card_front)
    FrameLayout mFlCardFront;

    private boolean mIsShowBack;
    private AnimatorSet mLeftInSet;
    private AnimatorSet mRightOutSet;

    private int TIME = 1000;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setAnimators(MainActivity.this, mFlContainer);
        setCameraDistance(MainActivity.this, mFlCardFront, mFlCardBack);
        setFlipCard();

        handler.postDelayed(runnable, TIME); //每隔1s执行

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, TIME);
                setAnimators(MainActivity.this, mFlContainer);
                setCameraDistance(MainActivity.this, mFlCardFront, mFlCardBack);
                setFlipCard();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 设置动画
    public void setAnimators(Context context, final FrameLayout mFlContainer) {
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.anim_in);
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.anim_out);

        // 设置点击事件
        mRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFlContainer.setClickable(false);
            }
        });
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFlContainer.setClickable(true);
            }
        });
    }

    // 改变视角距离, 贴近屏幕
    public static void setCameraDistance(Context context, FrameLayout mFlCardFront_, FrameLayout mFlCardBack_) {
        int distance = 16000;
        float scale = context.getResources().getDisplayMetrics().density * distance;
        mFlCardFront_.setCameraDistance(scale);
        mFlCardBack_.setCameraDistance(scale);
    }

    // 翻转卡片
    public void flipCard(View view) {
        setFlipCard();
    }

    public void setFlipCard() {
        // 正面朝上
        if (!mIsShowBack) {
            mRightOutSet.setTarget(mFlCardFront);
            mLeftInSet.setTarget(mFlCardBack);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = true;
        } else { // 背面朝上
            mRightOutSet.setTarget(mFlCardBack);
            mLeftInSet.setTarget(mFlCardFront);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
