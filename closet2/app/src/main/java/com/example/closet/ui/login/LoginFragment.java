package com.example.closet.ui.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.closet.R;
import com.example.closet.ui.chat.chattingList;
import com.example.closet.ui.home.HomeFragment;
import com.example.closet.ui.main.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginFragment extends Fragment {

    EditText editPasswd, editId;
    ProgressBar progressBar;
    Button loginBtn, btn_back;
    TextView forgetPasswd;

    private FirebaseAuth mAuth;

    private LoginViewModel loginViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        editId = (EditText) root.findViewById(R.id.editId);
        editPasswd=(EditText) root.findViewById(R.id.editPasswd);
        progressBar=(ProgressBar) root.findViewById(R.id.progressbar);
        loginBtn=(Button) root.findViewById(R.id.loginBtn);
        btn_back = (Button) root.findViewById(R.id.btn_back);

        mAuth = FirebaseAuth.getInstance(); //파이어베이스 Auth 초기화

        final FragmentManager fragmentManager = getFragmentManager();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String stEmail = editId.getText().toString();
                    String stPasswd = editPasswd.getText().toString();

                    if (stEmail.isEmpty()) {
                        Toast.makeText(getActivity(), "아이디를 입력하세요", Toast.LENGTH_LONG).show();
                        return;
                    } else if (stPasswd.isEmpty()) {
                        Toast.makeText(getActivity(), "비밀번호를  입력하세요", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            String result;
                            CustomTask task = new CustomTask();
                            result = task.execute(stEmail, stPasswd, "login").get(); //jsp에 stEmail, stPasswd, Login을 보내고 그에대한 값 받기
                            //doInBackround에서 사용된 문자열 배열에 필요한 값을 입력
                            // jsp로 부터 리턴값을 받아야할 때 String returns = task.execute(값1,값2).get(); 처럼 사용
                            // get()에서 에러가 발생할 수 있어서 try catch문 사용
                            Log.i("jsp로 부터의 결과 값", result);

                            if (result.equals("true")) {
                                Toast.makeText(getActivity(), "로그인", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                String firebaseID = stEmail + "@email.com";
                                mAuth.signInWithEmailAndPassword(firebaseID, stPasswd)
                                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseUser user = mAuth.getCurrentUser();

                                                            FirebaseUser fbuser = mAuth.getCurrentUser();
                                                            String stUserEmail= fbuser.getEmail();
                                                            Intent in = new Intent(getActivity(),chattingList.class);
                                                            in.putExtra("email",stUserEmail);
                                                            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new MainFragment())
                                                                    .commit(); //화면전환


                                                        } else {
                                                            // If sign in fails, display a message to the user.
                                                            Log.e("firebaseError", task.getException().toString());
                                                            Toast.makeText(getActivity(), "firebase오류", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                //fragment_main.xml로 이동

                            } else if (result.equals("false")) {
                                Toast.makeText(getActivity(), "아이디 또는 비밀번호가 틀렸음", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                editId.setText("");
                                editPasswd.setText("");
                            } else if (result.equals("noId")) {
                                Toast.makeText(getActivity(), "존재하지 않는 아이디", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                editId.setText("");
                                editPasswd.setText("");
                            }
                        } catch(Exception e){
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {

                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment())
                        .commit();
            }
        });
        return root;
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.0.103:8080/WebContent/testDB.jsp");//보낼 jsp 주소를 ""안에 작성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&type="+strings[2];//보낼 정보, GET방식으로 작성 ex) "id=rain483&pwd=1234";
                //보낼 데이터가 여러 개일 경우 &로 구분하여 작성
                osw.write(sendMsg);//OutputStreamWriter에 담아 전송
                osw.flush();
                //jsp와 통신이 정상적으로 되었을 때 실행될 코드
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    Log.i("통신 결과", conn.getResponseCode()+"결과"); //200일 경우 OK
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    //jsp에서 보낸 값을 받음
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    tmp.close();
                    reader.close();
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러코드");
                    InputStream is = conn.getErrorStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    String response = new String(byteData);
                    Log.i("오류 내용", "response = " + response);
                    // 통신이 실패했을 때 실패한 이유를 알기 위해 로그
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jsp로부터 받은 리턴 값
            return receiveMsg;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}