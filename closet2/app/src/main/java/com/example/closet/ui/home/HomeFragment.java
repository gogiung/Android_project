package com.example.closet.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.closet.R;
import com.example.closet.ui.join.JoinFragment;
import com.example.closet.ui.login.LoginFragment;
import com.example.closet.ui.main.MainFragment;
import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment{
    private HomeViewModel homeViewModel;
    Button btn_login, btn_join, btn3;

    private FirebaseAuth mAuth;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btn3 =(Button)root.findViewById(R.id.button3);
        btn_login = (Button)root.findViewById(R.id.btn_login);
        btn_join = (Button)root.findViewById(R.id.btn_join);

        mAuth = FirebaseAuth.getInstance(); //파이어베이스 Auth 초기화

        final FragmentManager fragmentManager = getFragmentManager();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new LoginFragment())
                        .commit();
            }
        });
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new JoinFragment())
                        .commit();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new MainFragment())
                        .commit();
            }
        });

        return root;
    }
}