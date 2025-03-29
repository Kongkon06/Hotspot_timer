package com.example.hotspot_control;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class ShinyTextView extends AppCompatTextView {

    private LinearGradient linearGradient;
    private Matrix gradientMatrix;
    private float gradientWidth;
    private float offset;
    private ValueAnimator animator;

    public ShinyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gradientMatrix = new Matrix();

        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setDuration(2000);
        animator.addUpdateListener(animation -> {
            offset = (float) animation.getAnimatedValue();
            updateGradient();
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Paint paint = getPaint();
        gradientWidth = getWidth();

        // Define the gradient effect
        linearGradient = new LinearGradient(
                0, 0, gradientWidth, 0,
                new int[]{
                        Color.parseColor("#CCCCCC"),  // Gray
                        Color.parseColor("#FFFFFF"),  // White (shiny part)
                        Color.parseColor("#CCCCCC")   // Gray
                },
                new float[]{0.3f, 0.5f, 0.7f},
                Shader.TileMode.CLAMP
        );

        paint.setShader(linearGradient);

        if (!animator.isRunning()) {
            animator.start();
        }
    }

    private void updateGradient() {
        gradientMatrix.setTranslate(offset * 2 * gradientWidth - gradientWidth, 0);
        linearGradient.setLocalMatrix(gradientMatrix);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
    }
}
