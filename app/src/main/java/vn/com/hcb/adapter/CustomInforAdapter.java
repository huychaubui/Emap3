package vn.com.hcb.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import vn.com.hcb.datn.R;
import vn.com.hcb.model.Service;

/**
 * Created by HUYCHAU on 3/31/2017.
 */

public class CustomInforAdapter implements GoogleMap.InfoWindowAdapter {
    Activity context;
    Service service;
//    vn.com.hcb.model.Service service;
    public  CustomInforAdapter(Activity context, Service service){
        this.context = context;
        this.service = service;
//        this.service = service;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(R.layout.itemmarker, null);
        ImageView imgHinh = (ImageView) row.findViewById(R.id.imgHinh);
        final TextView tvName = (TextView) row.findViewById(R.id.tvName);
        TextView tvAddress = (TextView) row.findViewById(R.id.tvAddress);
        //Set Text, src hinh cho các thành phần
        tvName.setText(this.service.getName());
        tvAddress.setText(this.service.getAddress());
        String url_hinh = service.getImage();
        if(url_hinh != null && url_hinh!=""){
            Glide.with(context)
                    .load(url_hinh)
                    .asBitmap()
                    .override(60,70)
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
                            if(!isFromMemoryCache)
                                marker.showInfoWindow();
                            return false;
                        }
                    })
                    .into(imgHinh);
        }else{
            imgHinh.setImageResource(R.drawable.loading_spinner);
        }
        return row;
    }
}
