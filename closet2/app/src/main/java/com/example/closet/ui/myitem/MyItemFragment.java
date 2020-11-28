package com.example.closet.ui.myitem;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.closet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class MyItemFragment extends Fragment {

    private MyItemViewModel myitemViewModel;
    private ImageView imageview;
    ImageView TOP_1,TOP_2,TOP_3,TOP_4,TOP_5,TOP_6,TOP_7;
    ImageView BOTTOM_1, BOTTOM_2,BOTTOM_3,BOTTOM_4, BOTTOM_5, BOTTOM_6,BOTTOM_7;
    ImageView ONEPIECE_1,ONEPIECE_2, ONEPIECE_3,ONEPIECE_4,ONEPIECE_5,ONEPIECE_6,ONEPIECE_7;
    ImageView SHOES_1,SHOES_2,SHOES_3,SHOES_4,SHOES_5,SHOES_6, SHOES_7;
    ImageView OUTER_1, OUTER_2, OUTER_3, OUTER_4,OUTER_5,OUTER_6, OUTER_7;
    ImageView COORDI_1,COORDI_2,COORDI_3,COORDI_4,COORDI_5,COORDI_6,COORDI_7;
    ImageView[] TOP_IV=new ImageView[7];
    ImageView[] BOTTOM_IV=new ImageView[7];
    ImageView[] ONEPIECE_IV=new ImageView[7];
    ImageView[] SHOES_IV=new ImageView[7];
    ImageView[] OUTER_IV=new ImageView[7];
    ImageView[] COORDI_IV=new ImageView[7];
    StorageReference ref= FirebaseStorage.getInstance().getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myitemViewModel =
                ViewModelProviders.of(this).get(MyItemViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myitem, container, false);

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

        COORDI_1=root.findViewById(R.id.Coordi_1);
        COORDI_2=root.findViewById(R.id.Coordi_2);
        COORDI_3=root.findViewById(R.id.Coordi_3);
        COORDI_4=root.findViewById(R.id.Coordi_4);
        COORDI_5=root.findViewById(R.id.Coordi_5);
        COORDI_6=root.findViewById(R.id.Coordi_6);
        COORDI_7=root.findViewById(R.id.Coordi_7);

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

       COORDI_IV[0]=COORDI_1;
        COORDI_IV[1]=COORDI_2;
        COORDI_IV[2]=COORDI_3;
        COORDI_IV[3]=COORDI_4;
        COORDI_IV[4]=COORDI_5;
        COORDI_IV[5]=COORDI_6;
        COORDI_IV[6]=COORDI_7;

        firebaseDatabase.getReference("closet").child("closet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String itgk=item.getKey();

                    for(DataSnapshot subitem: item.getChildren()){
                        String id=subitem.child("id").getValue().toString();
                        String image=subitem.child("image").getValue().toString();
                        StorageReference imgurl=ref.child(image);
                        getImage(imgurl, Integer.parseInt(id)-1 ,itgk);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return root;

    }
    private void getImage(StorageReference imgurl, final int ii,final String op2){

        imgurl.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    switch(op2){
                        case "TOP": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(TOP_IV[ii]);
                            break;
                        case "BOTTOM": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(BOTTOM_IV[ii]);
                            break;
                        case "OUTER": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(OUTER_IV[ii]);
                            break;
                        case "ONEPIECE": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(ONEPIECE_IV[ii]);
                            break;
                        case "SHOES": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(SHOES_IV[ii]);
                            break;
                        case "COORDI": // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(getActivity().getApplicationContext())
                                    .load(task.getResult())
                                    .into(COORDI_IV[ii]);
                            break;

                    }

                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(getActivity().getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}