package vn.com.hcb.layouts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import vn.com.hcb.adapter.NewItemAdapter;
import vn.com.hcb.datn.R;
import vn.com.hcb.model.Service;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HUYCHAU on 3/9/2017.
 */
public class TabHome extends Fragment {
    private List<Service> serviceList;
    private RecyclerView recyclerView;
    private NewItemAdapter newItemAdapter;
    DatabaseReference mDataService = FirebaseDatabase.getInstance().getReference("Service/Quán ăn");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3seach, container, false);
        addControls(rootView);
        addEvents();
        return rootView;
    }

    private void addEvents() {
    }

    private void addControls(View rootView) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rcv_new_item);
        serviceList = new ArrayList<>();
        newItemAdapter = new NewItemAdapter(getActivity(), serviceList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newItemAdapter);
        readData();
    }

    private void readData() {

        Query query = mDataService.limitToLast(15);
        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot != null) {
                    Service service_q = dataSnapshot.getValue(Service.class);
                    serviceList.add(service_q);
                    newItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    Service service_q = dataSnapshot.getValue(Service.class);
                    serviceList.add(service_q);
                    newItemAdapter.notifyDataSetChanged();
                for(int i = 0;i<serviceList.size(); i++){
                    if(serviceList.get(i).getName().equals(service_q.getName())){
                        serviceList.remove(i);
                        newItemAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //end Event of query

    }


}
