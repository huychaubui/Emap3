package vn.com.hcb.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vn.com.hcb.datn.R;
import vn.com.hcb.model.Comments;

/**
 * Created by HUYCHAU on 4/13/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

        private List<Comments> cmtList;
        private Activity activity;
        /**Contructor*/
        public CommentAdapter(Activity activity,List<Comments> cmtList) {
            this.activity = activity;
            this.cmtList = cmtList;
        }
        /** Create ViewHolder*/
        public class CommentViewHolder extends  RecyclerView.ViewHolder {
            private ImageView imgAvatar;
            private TextView tvDisplayName;
            private TextView tvContentCmt;

            public CommentViewHolder(View itemView) {
                super(itemView);
                tvContentCmt = (TextView) itemView.findViewById(R.id.tvContentCmt);
                tvDisplayName = (TextView) itemView.findViewById(R.id.tvDisplayName);
                imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            }
        }
        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            /** Get layout */
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment,parent,false);

            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            /** Set Value*/
            final Comments cmt = cmtList.get(position);
            holder.tvDisplayName.setText(cmt.getName());
            holder.tvContentCmt.setText(cmt.getContent());
            String avatar = cmt.getAvatar();
            if(avatar != null){
                Glide.with(activity)
                        .load(avatar)
                        .asBitmap()
                        .override(60,60)
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
                                return false;
                            }
                        })
                        .into(holder.imgAvatar);
            }else{
                holder.imgAvatar.setImageResource(R.drawable.loading_spinner);
            }
       /*Sự kiện click vào item*/
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //tim uid dung thi moi hien
                    final DatabaseReference refCmt = FirebaseDatabase.getInstance().getReference("Comments");
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    if (auth.getCurrentUser() != null) {
                        if(auth.getCurrentUser().getUid().equals(cmt.getUid())){
                            AlertDialog.Builder b = new AlertDialog.Builder(activity);
//                            b.setTitle("Xác nhận");
                            b.setMessage("Bạn có muốn xóa bình luận này?");
                            // Add the buttons
                            b.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final ProgressDialog progressDialog;
                                    progressDialog = new ProgressDialog(activity);
                                    progressDialog.setMessage("Đang xóa");
                                    progressDialog.show();
                                    refCmt.child(cmt.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                        }
                                    });

                                }
                            });
                            b.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            b.show();

                        }
                    }



                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return cmtList.size();
        }
    }