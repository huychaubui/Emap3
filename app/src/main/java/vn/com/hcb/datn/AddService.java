package vn.com.hcb.datn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import vn.com.hcb.model.Address;
import vn.com.hcb.model.Images;
import vn.com.hcb.model.Service;

public class AddService extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference mData ;
    StorageReference storageRef;
    Service service ;

    Spinner spThanhPho, spQuan;
    ImageView imgShow;
    EditText edtName,edtAddress, edtlat, edtlng, edtprice_low, edtprice_high, edtintro, edtNameImage;
    ProgressDialog mProgress;

    public String arr[], arrQuan[];
    android.support.v7.app.ActionBar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        customActionBar();
        addControls();
        addEvents();

    }
    public void customActionBar(){
        //Setup ActionBar
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar_addservice, null);
        TextView tvGui = (TextView) mCustomView.findViewById(R.id.tvGui);
        tvGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress = new ProgressDialog(AddService.this);
                upLoadData();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }
    private void addEvents() {
        readAddress();
        imgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processChonHinh();
            }
        });

    }

    private void addControls() {
        //setup FireBaseData
        storageRef = storage.getReference();
        mData = FirebaseDatabase.getInstance().getReference();

        service = new Service();
        //Setup Views
        edtName = (EditText) findViewById(R.id.edtName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtlat = (EditText) findViewById(R.id.edtlat);
        edtlng = (EditText) findViewById(R.id.edtlng);
        edtprice_low = (EditText) findViewById(R.id.edtprice_low);
        edtprice_high = (EditText) findViewById(R.id.edtprice_high);
        edtintro = (EditText) findViewById(R.id.edtintro);
        edtNameImage = (EditText) findViewById(R.id.edtNameImage);

        spThanhPho = (Spinner) findViewById(R.id.spThanhPho);
        spQuan = (Spinner) findViewById(R.id.spQuan);
        imgShow = (ImageView) findViewById(R.id.imgShow);
        imgShow.setTag("false");
    }
    String key_Sv;
    public void upLoadData(){
        String name = edtName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        final float lat = Float.valueOf(edtlat.getText().toString().trim());
        final float lng = Float.valueOf(edtlng.getText().toString().trim());
        int price_low = Integer.valueOf(edtprice_low.getText().toString().trim());
        int price_high = Integer.valueOf(edtprice_high.getText().toString().trim());
        String intro = edtintro.getText().toString().trim();
        String nameImage = edtNameImage.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtName.setError("Nhập tên địa điểm");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Nhập địa chỉ");
            return;
        }
        if (TextUtils.isEmpty(intro)) {
            edtintro.setError("Nhập mô tả");
            return;
        }

        if (TextUtils.isEmpty(nameImage)) {
            edtNameImage.setError("Nhập tên hình");
            return;
        }

        if (TextUtils.isEmpty(edtlat.getText())) {
            edtlat.setError("Nhập lat vào");
            return;
        }
        if (TextUtils.isEmpty(edtlng.getText())) {
            edtlng.setError("Nhập lng vào");
            return;
        }
        service.setComplex(name, address, lat, lng, price_low, price_high, intro);

        Toast.makeText(this, service.toString(), Toast.LENGTH_LONG).show();
        mProgress.setMessage("Đang lưu dữ liệu!");
        mProgress.show();
        mData.child("Service").child("Quán ăn").push().setValue(service, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    key_Sv = databaseReference.getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loactions/Quán ăn");
                    GeoFire geoFire = new GeoFire(ref);

                    geoFire.setLocation(key_Sv, new GeoLocation(lat, lng));
                    if(imgShow.getTag() == "true"){
                        upLoadImage();
                        imgShow.setTag("false");
                    }else{
                        Toast.makeText(AddService.this, "Ảnh rỗng", Toast.LENGTH_SHORT).show();
                    }
                    mProgress.dismiss();
                }
            }
        });



    }
    private void readAddress(){
        try {
            final Address[] addresses = ReadJson.readCompanyJSONFile(this);
            arr = new String[addresses.length];
            for (int i=0; i<addresses.length; i++){
                arr[i] = addresses[i].getName();
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>
                    (
                            this,
                            android.R.layout.simple_spinner_item,
                            arr
                    );
            //phải gọi lệnh này để hiển thị danh sách cho Spinner
            adapter.setDropDownViewResource
                    (android.R.layout.simple_list_item_single_choice);
            //Thiết lập adapter cho Spinner
            spThanhPho.setAdapter(adapter);
            //thiết lập sự kiện chọn phần tử cho Spinner
            spThanhPho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //setup value for City
                    service.setCity(arr[position]);
                    int l = addresses[position].getDistricts().length;
                    arrQuan = new String[l];
                    for (int ii = 0; ii<l; ii++){
                        arrQuan[ii] =  addresses[position].getDistricts()[ii];

                    }

                    ArrayAdapter<String> adapterQuan=new ArrayAdapter<String>
                            (
                                    AddService.this,
                                    android.R.layout.simple_spinner_item,
                                    arrQuan
                            );
                    //phải gọi lệnh này để hiển thị danh sách cho Spinner
                    adapterQuan.setDropDownViewResource
                            (android.R.layout.simple_list_item_single_choice);
                    //Thiết lập adapter cho Spinner
                    spQuan.setAdapter(adapterQuan);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            } );
            spQuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //setup value for District
                    service.setDistrict(arrQuan[position]);
//                    Toast.makeText(AddService.this, service.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void processChonHinh()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 200);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode == RESULT_OK && data!=null){
            Uri imageUri = data.getData();
            imgShow.setImageURI(imageUri);
            imgShow.setTag("true");
        }else{
            Toast.makeText(this, "Lấy ảnh thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }
    private void upLoadImage(){
        Calendar calendar = Calendar.getInstance();
        StorageReference mountainsRef = storageRef.child("image"+ calendar.getTimeInMillis()+".png");
        // Get the data from an ImageView as bytes
        imgShow.setDrawingCacheEnabled(true);
        imgShow.buildDrawingCache();
        Bitmap bitmap = imgShow.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(AddService.this, "Lỗi upload file", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                 taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Tao node Image tren database
                Images images = new Images(edtNameImage.getText().toString(), String.valueOf(downloadUrl));

                mData.child("Images").push().setValue(images, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null){
                            String key_img =  databaseReference.getKey();
                            mData.child("Service").child("Quán ăn").child(key_Sv).child("id_hinh").setValue(key_img);
                            Toast.makeText(AddService.this, "Lưu Hình Thành công", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(AddService.this, "Lỗi lưu vào database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}

