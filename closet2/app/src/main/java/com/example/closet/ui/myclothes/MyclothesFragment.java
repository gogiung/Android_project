package com.example.closet.ui.myclothes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.closet.R;
import com.example.closet.ui.main.MainFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

public class MyclothesFragment extends Fragment {

    private MyclothesViewModel myclothesViewModel;
    ImageView ivUser;
    private final int GET_GALLERY_IMAGE = 200;
    Spinner spinner_clothes;
    Button myclothesBt;
    private StorageReference mStorageRef;
    private Uri filePath;
    private String option;
    private String filename;
    private String clothes_name;
    EditText ETclothes;
    //storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final StorageReference storageRef = storage.getReference();
    final DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String top_sequence;
    private String bottom_sequence;
    private String onepiece_sequence;
    private String shoes_sequence;
    private String outer_sequence;
    private String sequence;
    private String coordi_sequence;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myclothesViewModel =
                ViewModelProviders.of(this).get(MyclothesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myclothes, container, false);
        final FragmentManager fragmentManager = getFragmentManager();


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }


        ivUser = (ImageView) root.findViewById(R.id.ivUser);
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);

            }
        });

        myclothesBt=(Button)root.findViewById(R.id.button);
        myclothesBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vies) {
               uploadFile();
               fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new MainFragment()).commit();
            }
        });


        spinner_clothes = (Spinner)root.findViewById(R.id.spinner_clothes);
        ArrayAdapter clothesAdapter = ArrayAdapter.createFromResource(inflater.getContext(),
                R.array.clothes, android.R.layout.simple_spinner_item);
        clothesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_clothes.setAdapter(clothesAdapter);
        ETclothes=(EditText)root.findViewById(R.id.editText7);
        getAllSeq();

        return root;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
        if (data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                ivUser.setImageBitmap(bitmap);
           } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    private void getAllSeq() {

        firebaseDatabase.getReference("closet").child("closet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                top_sequence=(String.valueOf(dataSnapshot.child("TOP").getChildrenCount()+1));
                bottom_sequence=(String.valueOf(dataSnapshot.child("BOTTOM").getChildrenCount()+1));
                onepiece_sequence=(String.valueOf(dataSnapshot.child("ONEPIECE").getChildrenCount()+1));
                shoes_sequence=(String.valueOf(dataSnapshot.child("SHOES").getChildrenCount()+1));
                outer_sequence=(String.valueOf(dataSnapshot.child("OUTER").getChildrenCount()+1));
                coordi_sequence=(String.valueOf(dataSnapshot.child("COORDI").getChildrenCount()+1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getOptionSeq(String option){
        switch(option){
            case "TOP":
                sequence=top_sequence;
                break;
            case "BOTTOM":
                sequence=bottom_sequence;
                break;
            case "ONEPIECE":
                sequence=onepiece_sequence;
                break;
            case "SHOES":
                sequence=shoes_sequence;
                break;
            case "OUTER":
                sequence=outer_sequence;
                break;

        }
    }
    private void uploadFile() {
        if (filePath != null) {

            //옵션값,옷 이름 가져오기
            option = spinner_clothes.getSelectedItem().toString();
            clothes_name=ETclothes.getText().toString();
            getOptionSeq(option);

            //storage 주소와 폴더 파일명을 지정해 준다.
            filename = option+"/"+option+"_"+sequence+".png";

            StorageReference mstorage =  storageRef.child(filename);

            //storage에 upload
            mstorage.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Map<String,String> postValues=new HashMap<>();
                            postValues.put("id",sequence);
                            postValues.put("image",filename);
                            postValues.put("name",clothes_name);
                            databaseReference.child("closet").child("closet").child(option).push().setValue(postValues);
                            Toast.makeText(getActivity().getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }
}