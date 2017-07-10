package cn.gxh.paralleldemo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GXH on 2017/7/7.
 */
public class WelcomePagerTransformer implements ViewPager.PageTransformer
        , ViewPager.OnPageChangeListener {
    private int pageIndex;
    private boolean pageChanged = true;
    private static final float ROT_MOD = -15f;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("----onPageSelected---:", position + "");
        pageIndex = position;
        pageChanged = true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 每一个页面都会回调这个方法
     * 就算我们滑动第一个页面，其它页面也会回调这个方法
     *
     * @param view
     * @param position
     */
    @Override
    public void transformPage(View view, float position) {
        Log.d("----position---:", position + "");
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.rl);
        final MyScrollView myScrollView = (MyScrollView) viewGroup.findViewById(R.id.mscv);
        View bg_container = view.findViewById(R.id.bg_container);
        View iv01 = viewGroup.findViewById(R.id.imageView0);
        View iv02 = viewGroup.findViewById(R.id.imageView0_2);

        int bg1_green = view.getResources().getColor(R.color.bg1_green);
        int bg2_blue = view.getResources().getColor(R.color.bg2_blue);

        Integer tag = (Integer) view.getTag();
        View parent = (View) view.getParent();

        //颜色估值器
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int color = bg1_green;
        //每个页面都会回调这个方法，并且每次滑动就会回调，并且每个页面都会回调
        //滑动之后显示的页面是pageIndex
        if (tag.intValue() == pageIndex) {
            switch (pageIndex) {
                case 0:
                    color = (int) evaluator.evaluate(Math.abs(position), bg1_green, bg2_blue);
                    break;
                case 1:
                    color = (int) evaluator.evaluate(Math.abs(position), bg2_blue, bg1_green);
                    break;
                case 2:
                    color = (int) evaluator.evaluate(Math.abs(position), bg1_green, bg2_blue);
                    break;
                default:
                    break;
            }
            //设置整个viewpager的背景颜色
            parent.setBackgroundColor(color);

            //动画 变色
//		    ObjectAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", bg1_green, bg2_blue);
////		    ObjectAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", CYAN, BLUE, RED);
//		    colorAnim.setTarget(parent);
//		    colorAnim.setEvaluator(new ArgbEvaluator());
//		    colorAnim.setRepeatCount(ValueAnimator.INFINITE);
//		    colorAnim.setRepeatMode(ValueAnimator.REVERSE);
//		    colorAnim.setDuration(3000);
//		    colorAnim.start();
        }

        //当前View滑到了中间
        if (position == 0) {
            System.out.println("position==0");
            //pageChanged作用--解决问题：只有在切换页面的时候才展示平移动画，如果不判断则会只是移动一点点当前页面松开也会执行一次平移动画
            if (pageChanged) {
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);

                ObjectAnimator animator_bg1 = ObjectAnimator.ofFloat(iv01, "translationX", 0, -iv01.getWidth());
                animator_bg1.setDuration(400);
                animator_bg1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        myScrollView.smoothScrollTo((int) (myScrollView.getWidth() * animation.getAnimatedFraction()), 0);
                    }
                });
                animator_bg1.start();

                ObjectAnimator animator_bg2 = ObjectAnimator.ofFloat(iv02, "translationX", iv02.getWidth(), 0);
                animator_bg2.setDuration(400);
                animator_bg2.start();
                pageChanged = false;
            }
        } else if (position == -1 || position == 1) {//所有效果复原
            iv02.setTranslationX(0);
            iv01.setTranslationX(0);
            myScrollView.smoothScrollTo(0, 0);
        } else if (position < 1 && position > -1) {

            final float width = iv01.getWidth();
            final float height = iv01.getHeight();
            final float rotation = ROT_MOD * position * -1.25f;


//			bg1.setPivotX(width * 0.5f);
//			bg1.setPivotY(height);
//			bg1.setRotation(rotation);
//			bg2.setPivotX(width * 0.5f);
//			bg2.setPivotY(height);
//			bg2.setRotation(rotation);
            //这里不去分别处理bg1、bg2，而是用包裹的父容器执行动画，目的是避免难以处理两个bg的属性位置恢复。
            bg_container.setPivotX(width * 0.5f);
            bg_container.setPivotY(height);
            bg_container.setRotation(rotation);
        }
    }
}
