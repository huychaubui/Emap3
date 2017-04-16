package vn.com.hcb.layouts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.hcb.datn.R;
import vn.com.hcb.model.Service;

public class DetailTab extends Fragment {
    TextView tvAddressDetail, tvCateDetail, tvPriceDetail,tvChiDuong;
    Service service;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_tab, container, false);
        service = (Service) getArguments().getSerializable("data");
        addControls(rootView);
        tvAddressDetail.setText(service.getAddress());
        //Set CAte
        String s = service.pricetoString();
        tvPriceDetail.setText(s);
        tvChiDuong.setOnClickListener(new View.OnClickListener() {
            String address = service.getLat()+","+service.getLng();
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+address));
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void addControls(View rootView) {
        tvAddressDetail = (TextView) rootView.findViewById(R.id.tvAddressDetail);
        tvCateDetail = (TextView) rootView.findViewById(R.id.tvCateDetail);
        tvPriceDetail = (TextView) rootView.findViewById(R.id.tvPriceDetail);
        tvChiDuong = (TextView) rootView.findViewById(R.id.tvChiDuong);

    }

}
