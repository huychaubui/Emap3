package vn.com.hcb.adapter;

/**
 * Created by HUYCHAU on 4/15/2017.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import vn.com.hcb.datn.R;
import vn.com.hcb.model.Service;

/**
 * Created by HUYCHAU on 4/13/2017.
 */

public class NewItemAdapter extends RecyclerView.Adapter<NewItemAdapter.NewItemViewHolder> {

    private List<Service> serverList;
    private Activity activity;
    /**Contructor*/
    public NewItemAdapter(Activity activity,List<Service> serverList) {
        this.activity = activity;
        this.serverList = serverList;
    }
    /** Create ViewHolder*/
    public class NewItemViewHolder extends  RecyclerView.ViewHolder {
        private ImageView img_Item;
        private TextView tv_Ten_quan;
        private TextView tv_dia_chi;
        private ProgressBar progressBar;

        public NewItemViewHolder(View itemView) {
            super(itemView);
            tv_Ten_quan = (TextView) itemView.findViewById(R.id.tv_Ten_quan);
            tv_dia_chi = (TextView) itemView.findViewById(R.id.tv_dia_chi);
            img_Item = (ImageView) itemView.findViewById(R.id.img_Item);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
    @Override
    public NewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /** Get layout */
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service,parent,false);

        return new NewItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewItemViewHolder holder, int position) {
        final Service service = serverList.get(position);
        holder.tv_Ten_quan.setText(service.getName());
        holder.tv_dia_chi.setText(service.getAddress());
        String urlimage = service.getImage();
        holder.progressBar.setVisibility(View.VISIBLE);
        if(urlimage != null){
            Glide.with(activity)
                    .load(urlimage)
                    .asBitmap()
                    .override(150,180)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT )
                    .error(R.drawable.icon_app)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.img_Item);
        }else{
            holder.img_Item.setImageResource(R.drawable.loading_spinner);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return serverList.size();
    }
}