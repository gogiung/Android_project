package com.example.closet.ui.register;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.closet.R;
import com.example.closet.ui.main.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;
    private ImageView imageview;
    ImageView TOP_1,TOP_2,TOP_3,TOP_4,TOP_5,TOP_6,TOP_7;
    ImageView BOTTOM_1, BOTTOM_2,BOTTOM_3,BOTTOM_4, BOTTOM_5, BOTTOM_6,BOTTOM_7;
    ImageView ONEPIECE_1,ONEPIECE_2, ONEPIECE_3,ONEPIECE_4,ONEPIECE_5,ONEPIECE_6,ONEPIECE_7;
    ImageView SHOES_1,SHOES_2,SHOES_3,SHOES_4,SHOES_5,SHOES_6, SHOES_7;
    ImageView OUTER_1, OUTER_2, OUTER_3, OUTER_4,OUTER_5,OUTER_6, OUTER_7;
    ImageView FL_TOP,FL_BOTTOM,FL_OUTER,FL_SHOES,FL_ONEPIECE;
    ImageView[] TOP_IV=new ImageView[7];
    ImageView[] BOTTOM_IV=new ImageView[7];
    ImageView[] ONEPIECE_IV=new ImageView[7];
    ImageView[] SHOES_IV=new ImageView[7];
    ImageView[] OUTER_IV=new ImageView[7];
    Button bt_register;
    FrameLayout ivregister;
    StorageReference ref= FirebaseStorage.getInstance().getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private String coordi_sequence;
    private String filename;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel =
                ViewModelProviders.of(this).get(RegisterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        final FragmentManager fragmentManager = getFragmentManager();

        FL_TOP=root.findViewById(R.id.FL_TOP);
        FL_BOTTOM=root.findViewById(R.id.FL_BOTTOM);
        FL_OUTER=root.findViewById(R.id.FL_OUTER);
        FL_ONEPIECE=root.findViewById(R.id.FL_ONEPIECE);
        FL_SHOES=root.findViewById(R.id.FL_SHOES);

        TOP_1=root.findViewById(R.id.TOP_1);
        TOP_2=root.findViewById(R.id.TOP_2);
        TOP_3=root.findViewById(R.id.TOP_3);
        TOP_4=root.findViewById(R.id.TOP_4);
        TOP_5=root.findViewById(R.id.TOP_5);
        TOP_6=root.findViewById(R.id.TOP_6);
        TOP_7=root.findViewById(R.id.TOP_7);

        BOTTOM_1=root.findViewById(R.id.BOTTOM_1);
        BOTTOM_2=root.findViewById(R.id.BOTTOM_2);
        BOTTOM_3=root.findViewById(R.id.BOTTOM_3);
        BOTTOM_4=root.findViewById(R.id.BOTTOM_4);
        BOTTOM_5=root.findViewById(R.id.BOTTOM_5);
        BOTTOM_6=root.findViewById(R.id.BOTTOM_6);
        BOTTOM_7=root.findViewById(R.id.BOTTOM_7);

        OUTER_1=root.findViewById(R.id.OUTER_1);
        OUTER_2=root.findViewById(R.id.OUTER_2);
        OUTER_3=root.findViewById(R.id.OUTER_3);
        OUTER_4=root.findViewById(R.id.OUTER_4);
        OUTER_5=root.findViewById(R.id.OUTER_5);
        OUTER_6=root.findViewById(R.id.OUTER_6);
        OUTER_7=root.findViewById(R.id.OUTER_7);

        ONEPIECE_1=root.findViewById(R.id.ONEPIECE_1);
        ONEPIECE_2=root.findViewById(R.id.ONEPIECE_2);
        ONEPIECE_3=root.findViewById(R.id.ONEPIECE_3);
        ONEPIECE_4=root.findViewById(R.id.ONEPIECE_4);
        ONEPIECE_5=root.findViewById(R.id.ONEPIECE_5);
        ONEPIECE_6=root.findViewById(R.id.ONEPIECE_6);
        ONEPIECE_7=root.findViewById(R.id.ONEPIECE_7);

        SHOES_1=root.findViewById(R.id.SHOES_1);
        SHOES_2=root.findViewById(R.id.SHOES_2);
        SHOES_3=root.findViewById(R.id.SHOES_3);
        SHOES_4=root.findViewById(R.id.SHOES_4);
        SHOES_5=root.findViewById(R.id.SHOES_5);
        SHOES_6=root.findViewById(R.id.SHOES_6);
        SHOES_7=root.findViewById(R.id.SHOES_7);


        TOP_IV[0]=TOP_1;
        TOP_IV[1]=TOP_2;
        TOP_IV[2]=TOP_3;
        TOP_IV[3]=TOP_4;
        TOP_IV[4]=TOP_5;
        TOP_IV[5]=TOP_6;
        TOP_IV[6]=TOP_7;

        BOTTOM_IV[0]=BOTTOM_1;
        BOTTOM_IV[1]=BOTTOM_2;
        BOTTOM_IV[2]=BOTTOM_3;
        BOTTOM_IV[3]=BOTTOM_4;
        BOTTOM_IV[4]=BOTTOM_5;
        BOTTOM_IV[5]=BOTTOM_6;
        BOTTOM_IV[6]=BOTTOM_7;

        OUTER_IV[0]=OUTER_1;
        OUTER_IV[1]=OUTER_2;
        OUTER_IV[2]=OUTER_3;
        OUTER_IV[3]=OUTER_4;
        OUTER_IV[4]=OUTER_5;
        OUTER_IV[5]=OUTER_6;
        OUTER_IV[6]=OUTER_7;

        SHOES_IV[0]=SHOES_1;
        SHOES_IV[1]=SHOES_2;
        SHOES_IV[2]=SHOES_3;
        SHOES_IV[3]=SHOES_4;
        SHOES_IV[4]=SHOES_5;
        SHOES_IV[5]=SHOES_6;
        SHOES_IV[6]=SHOES_7;

        ONEPIECE_IV[0]=ONEPIECE_1;
        ONEPIECE_IV[1]=ONEPIECE_2;
        ONEPIECE_IV[2]=ONEPIECE_3;
        ONEPIECE_IV[3]=ONEPIECE_4;
        ONEPIECE_IV[4]=ONEPIECE_5;
        ONEPIECE_IV[5]=ONEPIECE_6;
        ONEPIECE_IV[6]=ONEPIECE_7;

        bt_register=root.findViewById(R.id.bt_register);
        ivregister=root.findViewById(R.id.ivregister);
        getAllSeq();

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                FrameLayout savedImage = null;
                savedImage = ivregister;
                savedImage.setDrawingCacheEnabled(true);
                savedImage.buildDrawingCache();
                bitmap = savedImage.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                filename = "COORDI/COORDI_"+coordi_sequence+".png";
                StorageReference imagesRef = ref.child(filename);
                UploadTask uploadTask = imagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getActivity().getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Map<String,String> postValues=new HashMap<>();
                        postValues.put("id",coordi_sequence);
                        postValues.put("image",filename);
                        firebaseDatabase.getReference().child("closet").child("closet").child("COORDI").push().setValue(postValues);
                        Toast.makeText(getActivity().getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new MainFragment()).commit();
                    }
                });

            }
        });


        firebaseDatabase.getReference("closet").child("closet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String itgk=item.getKey();

                    for(DataSnapshot subitem: item.getChildren()){
                        String id=subitem.child("id").getValue().toString();
                        String image=subitem.child("image").getValue().toString();
                        StorageReference imgurl=ref.child(image);
                        getImage(imgurl, Integer.parseInt(id)-1 ,itgk,image);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        return root;

    }

    private void getAllSeq() {

        firebaseDatabase.getReference("closet").child("closet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coordi_sequence=(String.valueOf(dataSnapshot.child("COORDI").getChildrenCount()+1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getImage(StorageReference imgurl, final int ii,final String op2,final String im1){

        imgurl.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    switch(op2){
                        case "TOP": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(TOP_IV[ii]);
                            TOP_IV[ii].setOnClickListener(IVClickListener);
                            break;
                        case "BOTTOM": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(BOTTOM_IV[ii]);
                            BOTTOM_IV[ii].setOnClickListener(IVClickListener);
                            break;
                        case "OUTER": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(OUTER_IV[ii]);
                            OUTER_IV[ii].setOnClickListener(IVClickListener);
                            break;
                        case "ONEPIECE": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(ONEPIECE_IV[ii]);
                            ONEPIECE_IV[ii].setOnClickListener(IVClickListener);
                            break;
                        case "SHOES": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(SHOES_IV[ii]);
                            SHOES_IV[ii].setOnClickListener(IVClickListener);
                            break;

                    }

                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(getActivity().getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ImageView.OnClickListener IVClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch(v.getId()){
                case R.id.TOP_1:
                    FL_TOP.setImageDrawable(TOP_1.getDrawable());
                    break;
                case R.id.TOP_2:
                    FL_TOP.setImageDrawable(TOP_2.getDrawable());
                    break;
                case R.id.TOP_3:
                    FL_TOP.setImageDrawable(TOP_3.getDrawable());
                    break;
                case R.id.TOP_4:
                    FL_TOP.setImageDrawable(TOP_4.getDrawable());
                    break;
                case R.id.TOP_5:
                    FL_TOP.setImageDrawable(TOP_5.getDrawable());
                    break;
                case R.id.TOP_6:
                    FL_TOP.setImageDrawable(TOP_6.getDrawable());
                    break;
                case R.id.TOP_7:
                    FL_TOP.setImageDrawable(TOP_7.getDrawable());
                    break;
                case R.id.BOTTOM_1:
                    FL_BOTTOM.setImageDrawable(BOTTOM_1.getDrawable());
                    break;
                case R.id.BOTTOM_2:
                    FL_BOTTOM.setImageDrawable(BOTTOM_2.getDrawable());
                    break;
                case R.id.BOTTOM_3:
                    FL_BOTTOM.setImageDrawable(BOTTOM_3.getDrawable());
                    break;
                case R.id.BOTTOM_4:
                    FL_BOTTOM.setImageDrawable(BOTTOM_4.getDrawable());
                    break;
                case R.id.BOTTOM_5:
                    FL_BOTTOM.setImageDrawable(BOTTOM_5.getDrawable());
                    break;
                case R.id.BOTTOM_6:
                    FL_BOTTOM.setImageDrawable(BOTTOM_6.getDrawable());
                    break;
                case R.id.BOTTOM_7:
                    FL_BOTTOM.setImageDrawable(BOTTOM_7.getDrawable());
                    break;
                case R.id.OUTER_1:
                    FL_OUTER.setImageDrawable(OUTER_1.getDrawable());
                    break;
                case R.id.OUTER_2:
                    FL_OUTER.setImageDrawable(OUTER_2.getDrawable());
                    break;
                case R.id.OUTER_3:
                    FL_OUTER.setImageDrawable(OUTER_3.getDrawable());
                    break;
                case R.id.OUTER_4:
                    FL_OUTER.setImageDrawable(OUTER_4.getDrawable());
                    break;
                case R.id.OUTER_5:
                    FL_OUTER.setImageDrawable(OUTER_5.getDrawable());
                    break;
                case R.id.OUTER_6:
                    FL_OUTER.setImageDrawable(OUTER_6.getDrawable());
                    break;
                case R.id.OUTER_7:
                    FL_OUTER.setImageDrawable(OUTER_7.getDrawable());
                    break;
                case R.id.SHOES_1:
                    FL_SHOES.setImageDrawable(SHOES_1.getDrawable());
                    break;
                case R.id.SHOES_2:
                    FL_SHOES.setImageDrawable(SHOES_2.getDrawable());
                    break;
                case R.id.SHOES_3:
                    FL_SHOES.setImageDrawable(SHOES_3.getDrawable());
                    break;
                case R.id.SHOES_4:
                    FL_SHOES.setImageDrawable(SHOES_4.getDrawable());
                    break;
                case R.id.SHOES_5:
                    FL_SHOES.setImageDrawable(SHOES_5.getDrawable());
                    break;
                case R.id.SHOES_6:
                    FL_SHOES.setImageDrawable(SHOES_6.getDrawable());
                    break;
                case R.id.SHOES_7:
                    FL_SHOES.setImageDrawable(SHOES_7.getDrawable());
                    break;
                case R.id.ONEPIECE_1:
                    FL_ONEPIECE.setImageDrawable(ONEPIECE_1.getDrawable());
                    break;
                case R.id.ONEPIECE_2:
                    FL_ONEPIECE.setImageDrawable(ONEPIECE_2.getDrawable());
                    break;
                case R.id.ONEPIECE_3:
                    FL_ONEPIECE.setImageDrawable(ONEPIECE_3.getDrawable());
                    break;
                case R.id.ONEPIECE_4:
                    FL_ONEPIECE.setImageDrawable(ONEPIECE_4.getDrawable());
                    break;
                case R.id.ONEPIECE_5:
                    FL_ONEPIECE.setImageDrawable(ONEPIECE_5.getDrawable());
                    break;
                case R.id.ONEPIECE_6:
                    FL_ONEPIECE.setImageDrawable(ONEPIECE_6.getDrawable());
                    break;
                case R.id.ONEPIECE_7:
                    FL_ONEPIECE.setImageDrawable(ONEPIECE_7.getDrawable());
                    break;
            }
        }
    };

}