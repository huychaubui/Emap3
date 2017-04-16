package vn.com.hcb.layouts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.com.hcb.adapter.CommentAdapter;
import vn.com.hcb.datn.R;
import vn.com.hcb.model.Comments;

public class CommentTab extends Fragment {
    private ImageButton btnSend;
    private EditText edtComment;
    private List<Comments> cmtList;
    private RecyclerView recyclerView;
    private CommentAdapter cmtAdapter;
    DatabaseReference refCmt = FirebaseDatabase.getInstance().getReference("Comments");
    String key;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        key =  getArguments().getString("key");
        addControls(rootView);
        addEvents();
        return rootView;
    }

    private void addEvents() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtComment.getText().toString();
                if (auth.getCurrentUser() != null) {
                    String uid = auth.getCurrentUser().getUid();


                    vn.com.hcb.model.Comment cmt = new vn.com.hcb.model.Comment(uid, key, content);
                    refCmt.push().setValue(cmt, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {

                                Toast.makeText(getContext(), "Commented", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Fails", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "Dang nhap moi co the cmt", Toast.LENGTH_SHORT).show();
                }
                edtComment.setText("");
                edtComment.setHint("Bình luận");
            }
        });
    }

    private void addControls(View rootView) {
        edtComment = (EditText) rootView.findViewById(R.id.edtComment);
        btnSend = (ImageButton) rootView.findViewById(R.id.btnSend);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        cmtList = new ArrayList<>();
        cmtAdapter = new CommentAdapter(getActivity(), cmtList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(cmtAdapter);
        readData();
    }
    private void readData(){
        Query query = refCmt.orderByChild("key").equalTo( key);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot != null) {
                    final String key_cmt = dataSnapshot.getKey();
                    vn.com.hcb.model.Comment comment = dataSnapshot.getValue(vn.com.hcb.model.Comment.class);
//                    Toast.makeText(getContext(), comment.getContent(), Toast.LENGTH_SHORT).show();

                    DatabaseReference mUser = FirebaseDatabase.getInstance().getReference("User");
                    final Comments cmt = new Comments(null, null, comment.getContent());
                    cmt.setUid(comment.getUid());
                    mUser.child(comment.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("name").getValue().toString();
                            if(dataSnapshot.child("avatar").getValue() != null){
                                String avatar = dataSnapshot.child("avatar").getValue().toString();
                                cmt.setAvatar(avatar);
                            }
                            cmt.setName(name);
                            cmt.setKey(key_cmt);
                            cmtList.add(cmt);
                            cmtAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    final String key_cmt = dataSnapshot.getKey();
                    vn.com.hcb.model.Comment comment = dataSnapshot.getValue(vn.com.hcb.model.Comment.class);
//                    Toast.makeText(getContext(), comment.getContent(), Toast.LENGTH_SHORT).show();

                    DatabaseReference mUser = FirebaseDatabase.getInstance().getReference("User");
                    final Comments cmt = new Comments(null, null, comment.getContent());
                    cmt.setUid(comment.getUid());
                    mUser.child(comment.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("name").getValue().toString();
                            if(dataSnapshot.child("avatar").getValue() != null){
                                String avatar = dataSnapshot.child("avatar").getValue().toString();
                                cmt.setAvatar(avatar);
                            }
                            cmt.setName(name);
                            cmt.setKey(key_cmt);
                            for(int i = 0;i<cmtList.size(); i++){
                                if(cmtList.get(i).getKey().equals(cmt.getKey())){
                                    cmtList.remove(i);
                                    cmtAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    }

