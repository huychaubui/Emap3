package vn.com.hcb.datn;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import vn.com.hcb.model.Address;

/**
 * Created by HUYCHAU on 3/12/2017.
 */

public class ReadJson {
    // Đọc file company.json và chuyển thành đối tượng java.
    public static Address[] readCompanyJSONFile(Context context) throws IOException,JSONException {
        Address[] address;

        // Đọc nội dung text của file dataaddress.json
        String jsonText = readText(context, R.raw.dataaddress);

        // Đối tượng JSONObject gốc mô tả toàn bộ tài liệu JSON.
        JSONArray jsonRoot = new JSONArray(jsonText);
        address = new Address[jsonRoot.length()];
        for(int i =0; i< jsonRoot.length(); i++){
            JSONObject jsonObject = jsonRoot.getJSONObject(i);
            String name = jsonObject.getString("name");
            JSONArray jsonArray_dis = jsonObject.getJSONArray("districts");
            String[] dis = new String[jsonArray_dis.length()];

            for(int ii=0;ii < jsonArray_dis.length();ii++) {
                dis[ii] = jsonArray_dis.getString(ii);

            }
            Address address_ = new Address();
            address_.setName(name);
            address_.setDistricts(dis);
            address[i] = address_;

        }
//        Toast.makeText(context, "Thành coong", Toast.LENGTH_SHORT).show();
        return address;
    }


    // Đọc nội dung text của một file nguồn.
    private static String readText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        StringBuilder sb= new StringBuilder();
        String s= null;
        while((  s = br.readLine())!=null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

}