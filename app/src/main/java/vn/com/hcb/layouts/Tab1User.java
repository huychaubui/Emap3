package vn.com.hcb.layouts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import vn.com.hcb.datn.AddService;
import vn.com.hcb.datn.LoginActivity;
import vn.com.hcb.datn.R;
import vn.com.hcb.model.Service;

/**
 * Created by HUYCHAU on 3/9/2017.
 */
public class Tab1User extends Fragment {
    TextView tvDangnhap,tvaddService, tvSaveadd, tvAboutme, tvDangxuat;
    ImageView imgDangxuat, imgAvatar_tab1;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1user, container, false);
        auth = FirebaseAuth.getInstance();
        addControls(rootView);
        checkUser();

        //Events for textView
        tvDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        tvDangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                LoginManager.getInstance().logOut();
                tvDangnhap.setText("Đăng nhập");
                checkUser();
                imgDangxuat.setVisibility(View.GONE);
                tvDangxuat.setVisibility(View.GONE);
                tvDangnhap.setClickable(true);
            }
        });
        tvaddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), AddService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        FindService(rootView);
        return rootView;
    }
    private void addControls(View rootView) {
        tvDangnhap = (TextView) rootView.findViewById(R.id.tvDangnhap);
        tvaddService = (TextView) rootView.findViewById(R.id.tvaddService);
        tvSaveadd = (TextView) rootView.findViewById(R.id.tvSaveadd);
        tvAboutme = (TextView) rootView.findViewById(R.id.tvAboutme);
        tvDangxuat = (TextView) rootView.findViewById(R.id.tvDangxuat);
        imgDangxuat = (ImageView) rootView.findViewById(R.id.imgDangxuat);
        imgAvatar_tab1 = (ImageView) rootView.findViewById(R.id.imgAvatar_tab1);
    }
private void checkUser(){
    if (auth.getCurrentUser() != null) {
        if(auth.getCurrentUser().getDisplayName()!= null){
            String txt = auth.getCurrentUser().getDisplayName().toString();
            tvDangnhap.setText(txt);
        }else{
            String txt = auth.getCurrentUser().getEmail().toString();
            tvDangnhap.setText(txt);
        }
    if(auth.getCurrentUser().getPhotoUrl() != null){
        String avatar = auth.getCurrentUser().getPhotoUrl().toString();
        Glide.with(this)
                .load(avatar)
                .asBitmap()
                .override(50,50)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESULT )
                .error(R.drawable.user40)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imgAvatar_tab1);
            }else{
                imgAvatar_tab1.setImageResource(R.drawable.user40);
             }


        tvaddService.setEnabled(true);
        tvSaveadd.setEnabled(true);
        imgDangxuat.setVisibility(View.VISIBLE);
        tvDangxuat.setVisibility(View.VISIBLE);
        tvDangnhap.setClickable(false);
    }else{
        tvaddService.setEnabled(false);
        tvSaveadd.setEnabled(false);
        imgDangxuat.setVisibility(View.GONE);
        tvDangxuat.setVisibility(View.GONE);
        tvDangnhap.setClickable(true);
    }
}
    // Test Query
    public void FindService(final View rootView){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("Quán ăn");
        String child = "name";
        String name = "Quán ăn A15";
        Query query = mDatabase.orderByChild(child).equalTo(name);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Service service = dataSnapshot.getValue(Service.class);
                Toast.makeText(rootView.getContext(), service.getAddress().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Service service = dataSnapshot.getValue(Service.class);
                Toast.makeText(rootView.getContext(), service.getName().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (auth.getCurrentUser() != null) {
//            String txt = auth.getCurrentUser().getEmail().toString();
//            tvDangnhap.setText(txt);
//            tvaddService.setEnabled(true);
//            tvSaveadd.setEnabled(true);
//            imgDangxuat.setVisibility(View.VISIBLE);
//            tvDangxuat.setVisibility(View.VISIBLE);
//        }else{
//            tvaddService.setEnabled(false);
//            tvSaveadd.setEnabled(false);
//            imgDangxuat.setVisibility(View.GONE);
//            tvDangxuat.setVisibility(View.GONE);
//        }
    }
}
