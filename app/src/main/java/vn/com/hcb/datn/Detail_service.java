package vn.com.hcb.datn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import vn.com.hcb.layouts.CommentTab;
import vn.com.hcb.layouts.DetailTab;
import vn.com.hcb.model.Service;

public class Detail_service extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    ImageView imgHinhDetail;
    TextView tvNameDetail, tvAddressDetail, tvCateDetail, tvPriceDetail;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsDetail);
        tabLayout.setupWithViewPager(mViewPager);
        Intent intent = getIntent();
        service = (Service) intent.getSerializableExtra("Service");





        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {
        imgHinhDetail = (ImageView) findViewById(R.id.imgHinhDetail);
        tvNameDetail = (TextView) findViewById(R.id.tvNameDetail);
        tvNameDetail.setText(service.getName());
        String url_hinh = service.getImage();
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth() -20;
        int height = display.getHeight();
        int ori = display.getOrientation();
        if(url_hinh != null && url_hinh!=""){
            Glide.with(this)
                    .load(url_hinh)
                    .asBitmap()
                    .override(width, 180)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT )
                    .error(R.drawable.icon_app)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            if(!isFromMemoryCache)
//                                marker.showInfoWindow();
                            return false;
                        }
                    })
                    .into(imgHinhDetail);
        }else{
            imgHinhDetail.setImageResource(R.drawable.loading_spinner);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position){
                case 0:
                    DetailTab tab1 = new DetailTab();
                    Bundle b =new Bundle();
                    b.putSerializable("data", service);
                    tab1.setArguments(b);
                    return tab1;
                case 1:
                    CommentTab tab2 = new CommentTab();

                    String key = getIntent().getStringExtra("key");
                    Bundle b1 =new Bundle();
                    b1.putString("key", key);
                    tab2.setArguments(b1);
                    return  tab2;

//                case 2:
//                    Tab1User tab3 = new Tab1User();
//                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Chi tiết";
                case 1:
                    return "Bình luận";
//                case 2:
//                    return "User";
            }
            return null;

//            Drawable image = ContextCompat.getDrawable(getBaseContext(), imageResId[position]);
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            SpannableString sb = new SpannableString(" ");
//            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;
        }
    }
}
