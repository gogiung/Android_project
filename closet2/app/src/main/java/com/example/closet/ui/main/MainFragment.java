package com.example.closet.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.closet.R;
import com.example.closet.Sharedpreference;
import com.example.closet.ui.web.GetRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class MainFragment extends Fragment {

    Button imageBtn;
    Gallery imgTop, imgBottom, imgOnepiece, imgOuter;
    Switch weatherswitch;
    private String[][][] resultArray = new String[4][20][3]; //옷 목록 파싱해 3차원 배열로 작성
    //[1,2,3] [i] {updown, type, thickness}, [4] [i] {type, thickness}
    private int[][] imgArray2 = new int[4][20];
    private String[][] coordiArray = new String[3][3]; //코디 저장하는 배열

    ImageView diaIV1, diaIV2, diaIV3;

    private RequestQueue queue;

    private ImageView iv_weather_main;
    private TextView tv_temp_main;
    private Button imageButton, cfBtn;

    View dialogView;

    private LinearLayout layout_detail;
    private ImageView iv_weather_detail;
    private TextView tv_loc_detail;
    private TextView tv_temp_detail;
    private TextView tv_temp_min;
    private TextView tv_temp_max;

    //시간 내용
    private TextView[] tv_time =  new TextView[5];
    private TextView[] tv_time_temp=  new TextView[5];
    private ImageView[] iv_weather=  new ImageView[5];

    StorageReference ref=FirebaseStorage.getInstance().getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private MainViewModel mainViewModel;
    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainViewModel =
                ViewModelProviders.of(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        imageBtn = (Button)root.findViewById(R.id.imageButton);
        cfBtn = (Button)root.findViewById(R.id.confirmBtn);
        weatherswitch = (Switch)root.findViewById(R.id.weatherSwitch);

        imgTop = (Gallery) root.findViewById(R.id.ivTOP);
        imgBottom = (Gallery) root.findViewById(R.id.ivBOTTOM);
        imgOnepiece = (Gallery)root.findViewById(R.id.ivONEPIECE);
        imgOuter = (Gallery)root.findViewById(R.id.ivOUTER);

        iv_weather_main = root.findViewById(R.id.iv_weather_main);
        tv_temp_main = root.findViewById(R.id.tv_temp_main);
        tv_temp_min = root.findViewById(R.id.tv_min);
        tv_temp_max = root.findViewById(R.id.tv_max);

        imageButton = root.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == imageButton){
                    layout_detail.setVisibility(layout_detail.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                }
            }
        });


        layout_detail = root.findViewById(R.id.layout_detail);
        iv_weather_detail = root.findViewById(R.id.iv_weather_detail);;
        tv_loc_detail = root.findViewById(R.id.tv_loc_detail);;
        tv_temp_detail = root.findViewById(R.id.tv_temp_detail);;

        tv_time[0] = root.findViewById(R.id.tv_time1);
        tv_time[1] = root.findViewById(R.id.tv_time2);
        tv_time[2] = root.findViewById(R.id.tv_time3);
        tv_time[3] = root.findViewById(R.id.tv_time4);
        tv_time[4] = root.findViewById(R.id.tv_time5);

        tv_time_temp[0] = root.findViewById(R.id.tv_time_temp1);
        tv_time_temp[1] = root.findViewById(R.id.tv_time_temp2);
        tv_time_temp[2] = root.findViewById(R.id.tv_time_temp3);
        tv_time_temp[3] = root.findViewById(R.id.tv_time_temp4);
        tv_time_temp[4] = root.findViewById(R.id.tv_time_temp5);

        iv_weather[0] =root.findViewById(R.id.iv_time_weather1);
        iv_weather[1] =root.findViewById(R.id.iv_time_weather2);
        iv_weather[2] =root.findViewById(R.id.iv_time_weather3);
        iv_weather[3] =root.findViewById(R.id.iv_time_weather4);
        iv_weather[4] =root.findViewById(R.id.iv_time_weather5);

        weatherswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Sharedpreference.setSharedPrefWeather(getActivity(),b);
                if(b){
                    getGPS();
                }

                for(int a = 0; a < resultArray.length; a++) {
                    for(int i = 0; i < resultArray[a].length; i++) {
                        for(int c = 0; c < resultArray[a][i].length; c++) {
                            resultArray[a][i][c] = "";
                        }
                    }
                }
                getcloth();
            }
        });

        weatherswitch.setChecked(Sharedpreference.getSharedPrefWeather(getContext()));

        cfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                if(resultArray[0][0] != null && resultArray[1][0] != null) {
                    Log.i("001101", "true");
                    if(resultArray[3][0] != null) {
                        Log.i("301", "true");
                        if(coordiArray[0][1] == null) { //상의가 선택 안된 경우
                            Toast.makeText(getActivity(), "상의를 선택해 주십시오", Toast.LENGTH_SHORT).show();
                        } else if(coordiArray[1][1] == null) { //하의가 선택 안된 경우
                            Toast.makeText(getActivity(), "하의를 선택해 주십시오", Toast.LENGTH_SHORT).show();
                        } else if(coordiArray[2][1] == null) {
                            Toast.makeText(getActivity(), "겉옷을 선택해 주십시오", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "3모두 선택", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.i("301", "false");
                        if(coordiArray[0][1] == null) { //상의가 선택 안된 경우
                            Toast.makeText(getActivity(), "상의를 선택해 주십시오", Toast.LENGTH_SHORT).show();
                        } else if(coordiArray[1][1] == null) { //하의가 선택 안된 경우
                            Toast.makeText(getActivity(), "하의를 선택해 주십시오", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "모두 선택", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else Log.i("fuck", "you");
                if(resultArray[2] != null) {

                }
                if(resultArray[2] != null && resultArray[3] != null) {

                }
                */
                dialogView = (View) View.inflate(getActivity(), R.layout.dialog1, null);
                diaIV1 = (ImageView)dialogView.findViewById(R.id.diaIV1);
                diaIV2 = (ImageView)dialogView.findViewById(R.id.diaIV2);
                diaIV3 = (ImageView)dialogView.findViewById(R.id.diaIV3);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());


                diaIV1.setImageResource(R.drawable.tshirts);
                diaIV2.setImageResource(R.drawable.slacks);
                diaIV3.setVisibility(View.INVISIBLE);
                //diaIV3.setImageResource(R.drawable.hoodzipup);
                dlg.setTitle("코디 추천");
                dlg.setIcon(R.drawable.archive);
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", null);
                dlg.show();
            }
        });

        return root;
    }

    public void getcloth() {
        String season, output;
        MainFragment.getfromSQL task = new MainFragment.getfromSQL();
        String jsonlog = "";

            if(weatherswitch.isChecked()) { //체크 된 경우
                int mintemp = (int) Math.round(Double.parseDouble(tv_temp_min.getText().toString()));
                int maxtemp = (int) Math.round(Double.parseDouble(tv_temp_max.getText().toString()));
                if (mintemp >= 17) {
                    //여름
                    season = "5";
                    output = "false";
                    try{
                        jsonlog = task.execute(season, output).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Log.i("json5", ""+jsonlog);
                    if(resultArray[0] != null) {
                        imgTop.setAdapter(new topAdapter(getActivity()));
                    }
                    if(resultArray[1] != null) {
                        imgBottom.setAdapter(new botAdapter(getActivity()));
                    }
                    if(resultArray[2] != null) {

                    }
                    if(resultArray[3] != null) {
                        imgOuter.setAdapter(new outAdapter(getActivity()));
                    }

                } else {
                    if (maxtemp >= 12) {
                        //봄, 가을
                        int standardtemp = ((maxtemp + mintemp) / 2) + 1; //기준 온도
                        int crossing = maxtemp - mintemp; //일교차
                        if (standardtemp < 12) {
                            //두꺼운 겉옷 + 긴 팔 or 중간 겉옷 + 긴 팔
                            season = "2";
                            output = "true";
                            try{
                                jsonlog = task.execute(season, output).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            Log.i("json2", ""+jsonlog);
                            if(resultArray[0] != null) {
                                imgTop.setAdapter(new topAdapter(getActivity()));
                            }
                            if(resultArray[1] != null) {
                                imgBottom.setAdapter(new botAdapter(getActivity()));
                            }
                            if(resultArray[2] != null) {

                            }
                            if(resultArray[3] != null) {
                                imgOuter.setAdapter(new outAdapter(getActivity()));
                            }

                        } else if (standardtemp < 16) {
                            //중간 겉옷 + 얇은 긴팔 or 얇은 겉옷 + 긴 팔 or 두꺼운 긴 팔
                            if (crossing >= 10) { //일교차가 클 경우
                                season = "3";
                                output = "true";
                                try{
                                    jsonlog = task.execute(season, output).get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                Log.i("json3", ""+jsonlog);
                                if(resultArray[0] != null) {
                                    imgTop.setAdapter(new topAdapter(getActivity()));
                                }
                                if(resultArray[1] != null) {
                                    imgBottom.setAdapter(new botAdapter(getActivity()));
                                }
                                if(resultArray[2] != null) {

                                }
                                if(resultArray[3] != null) {
                                    imgOuter.setAdapter(new outAdapter(getActivity()));
                                }

                            }
                            else { //일교차가 낮을 경우
                                season = "3";
                                output = "false";
                                try{
                                    jsonlog = task.execute(season, output).get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                Log.i("json33", ""+jsonlog);
                                if(resultArray[0] != null) {
                                    imgTop.setAdapter(new topAdapter(getActivity()));
                                }
                                if(resultArray[1] != null) {
                                    imgBottom.setAdapter(new botAdapter(getActivity()));
                                }
                                if(resultArray[2] != null) {

                                }
                                if(resultArray[3] != null) {
                                    imgOuter.setAdapter(new outAdapter(getActivity()));
                                }

                            }
                        } else {
                            //얇은 긴 팔 or 얇은 겉옷 + 반 팔
                            if (crossing >= 10) { //일교차가 클 경우
                                season = "4";
                                output = "true";
                                try{
                                    jsonlog = task.execute(season, output).get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                Log.i("json4", ""+jsonlog);
                                if(resultArray[0] != null) {
                                    imgTop.setAdapter(new topAdapter(getActivity()));
                                }
                                if(resultArray[1] != null) {
                                    imgBottom.setAdapter(new botAdapter(getActivity()));
                                }
                                if(resultArray[2] != null) {

                                }
                                if(resultArray[3] != null) {
                                    imgOuter.setAdapter(new outAdapter(getActivity()));
                                }

                            }
                            else { //일교차가 낮을 경우
                                season = "4";
                                output = "false";
                                try{
                                    jsonlog = task.execute(season, output).get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                Log.i("json44", ""+jsonlog);
                                if(resultArray[0] != null) {
                                    imgTop.setAdapter(new topAdapter(getActivity()));
                                }
                                if(resultArray[1] != null) {
                                    imgBottom.setAdapter(new botAdapter(getActivity()));
                                }
                                if(resultArray[2] != null) {

                                }
                                if(resultArray[3] != null) {
                                    imgOuter.setAdapter(new outAdapter(getActivity()));
                                }

                            }
                        }

                    } else {
                        //겨율
                        season = "1";
                        output = "true";
                        try{
                            jsonlog = task.execute(season, output).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        Log.i("json1", ""+jsonlog);
                        if(resultArray[0] != null) {
                            imgTop.setAdapter(new topAdapter(getActivity()));
                        }
                        if(resultArray[1] != null) {
                            imgBottom.setAdapter(new botAdapter(getActivity()));
                        }
                        if(resultArray[2] != null) {

                        }
                        if(resultArray[3] != null) {
                            imgOuter.setAdapter(new outAdapter(getActivity()));
                        }

                    }
                }

            } else { //체크 해제된 경우
                season = "0";
                output = "true";
                //task.execute(season, output);
                try{
                    jsonlog = task.execute(season, output).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.i("json", ""+jsonlog);
                if(resultArray[0] != null) {
                    imgTop.setAdapter(new topAdapter(getActivity()));
                }
                if(resultArray[1] != null) {
                    imgBottom.setAdapter(new botAdapter(getActivity()));
                }
                if(resultArray[2] != null) {

                }
                if(resultArray[3] != null) {
                    imgOuter.setAdapter(new outAdapter(getActivity()));
                }

            }

    }

    //갤러리 이미지 지정
    public class topAdapter extends BaseAdapter {
        Context context;
        boolean topclicked = false;
        int selectedIndex;
        int[] topimg = {R.drawable.tshirts, R.drawable.tshirts, R.drawable.mantoman, R.drawable.mantoman, R.drawable.shirts, R.drawable.shirts};

        public topAdapter(Context c) {
            context = c;
        }

        public void setSelectedIndex(int ind)
        {
            selectedIndex = ind;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return imgArray2[0].length;
        }

        @Override
        public Object getItem(int i) { return imgArray2[0]; }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ImageButton imgBtn = new ImageButton(context);
            imgBtn.setLayoutParams(new Gallery.LayoutParams(220,220));
            imgBtn.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            imgBtn.setBackgroundColor(Color.LTGRAY);
            if(weatherswitch.isChecked()) {
                imgBtn.setImageResource(topimg[i]);
            } else imgBtn.setImageResource(imgArray2[0][i]);

            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedIndex!= -1 && i == selectedIndex) {
                        if(!topclicked) {
                            imgBtn.setBackgroundColor(Color.YELLOW);
                            topclicked = true;
                            Log.i("top", "selected, false");

                            //코디 배열에 값 추가
                            for(int a = 0; a < 3; a++) {
                                coordiArray[0][a] = resultArray[0][i][a];
                            }

                        }else {
                            imgBtn.setBackgroundColor(Color.LTGRAY);
                            topclicked = false;
                            Log.i("top", "selected, true");

                            //코디 배열에서 값 삭제
                            for(int a = 0; a < 3; a++) {
                                coordiArray[0][a] = "";
                            }
                        }
                    } else {
                        //아무것도 선택이 안됐을 때
                        if(!topclicked) {
                            imgBtn.setBackgroundColor(Color.YELLOW);
                            topclicked = true;
                            Log.i("top", "notselected, false");
                            setSelectedIndex(i);
                        }
                        //선택된 항목과 다른 항목일 때

                    }
                }
            });

            return imgBtn;
        }
    }
    public class botAdapter extends BaseAdapter {
        Context context;
        boolean botclicked = false;
        int selectedIndex;
        int[] botimg = {R.drawable.slacks, R.drawable.slacks, R.drawable.jean, R.drawable.jean, R.drawable.shortpants, R.drawable.shortpants};
        int[] botimg2 = {R.drawable.shortpants, R.drawable.shortpants, R.drawable.slacks, R.drawable.slacks, R.drawable.jean, R.drawable.jean};

        public botAdapter(Context c) {
            context = c;
        }

        public void setSelectedIndex(int ind)
        {
            selectedIndex = ind;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return botimg.length;
        }

        @Override
        public Object getItem(int i) {
            if(weatherswitch.isChecked()) {
                return botimg2;
            } else return botimg;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ImageButton imgBtn = new ImageButton(context);
            imgBtn.setLayoutParams(new Gallery.LayoutParams(220,220));
            imgBtn.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            if(weatherswitch.isChecked()) {
                imgBtn.setImageResource(botimg2[i]);
            } else imgBtn.setImageResource(botimg[i]);

            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedIndex!= -1 && i == selectedIndex) {
                        if(!botclicked) {
                            imgBtn.setBackgroundColor(Color.YELLOW);
                            botclicked = true;

                            /*
                            //코디 배열에 값 추가
                            for(int a = 0; a < 3; a++) {
                                coordiArray[1][a] = resultArray[1][i][a];
                            }
                             */
                        }else {
                            imgBtn.setBackgroundColor(Color.LTGRAY);
                            botclicked = false;

                            /*
                            //코디 배열에서 값 삭제
                            for(int a = 0; a < 3; a++) {
                                coordiArray[1][a] = "";
                            }
                             */
                        }
                    } else {
                        //아무것도 선택이 안됐을 때
                        if(!botclicked) {
                            imgBtn.setBackgroundColor(Color.YELLOW);
                            botclicked = true;
                            setSelectedIndex(i);
                        }
                        //선택된 항목과 다른 항목일 때

                    }
                }
            });

            return imgBtn;
        }
    }

    public class outAdapter extends BaseAdapter {
        Context context;
        boolean outclicked = false;
        int selectedIndex;
        int[] outimg = {R.drawable.coat, R.drawable.gardigan, R.drawable.hoodzipup};
        int[] outimg2 = {};

        public outAdapter(Context c) {
            context = c;
        }

        public void setSelectedIndex(int ind)
        {
            selectedIndex = ind;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            //return imgArray2[3].length;
            if(weatherswitch.isChecked()) {
                return outimg2.length;
            } else return outimg.length;
        }

        @Override
        public Object getItem(int i) {
            //return imgArray2[3];
            if(weatherswitch.isChecked()) {
                return outimg2;
            } else return outimg;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ImageButton imgBtn = new ImageButton(context);
            imgBtn.setLayoutParams(new Gallery.LayoutParams(220,220));
            imgBtn.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            imgBtn.setImageResource(outimg[i]);

            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedIndex!= -1 && i == selectedIndex) {
                        if(!outclicked) {
                            imgBtn.setBackgroundColor(Color.YELLOW);
                            outclicked = true;
                            /*
                            //코디 배열에 값 추가
                            for(int a = 0; a < 3; a++) {
                                coordiArray[2][a] = resultArray[3][i][a];
                            }
                             */
                        }else {
                            imgBtn.setBackgroundColor(Color.LTGRAY);
                            outclicked = false;

                            /*
                            //코디 배열에서 값 삭제
                            for(int a = 0; a < 3; a++) {
                                coordiArray[2][a] = "";
                            }
                             */
                        }
                    } else {
                        //아무것도 선택이 안됐을 때
                        if(!outclicked) {
                            imgBtn.setBackgroundColor(Color.YELLOW);
                            outclicked = true;
                            setSelectedIndex(i);
                        }
                        //선택된 항목과 다른 항목일 때

                    }
                }
            });

            return imgBtn;
        }
    }

    class getfromSQL extends AsyncTask<String, Void, String> {
        String sendMsg = "";
        String receiveMsg = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str = "";
                URL url = new URL("http://192.168.0.103:8080/WebContent/coordiDB.jsp");//보낼 jsp 주소를 ""안에 작성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "season=" + strings[0] + "&output=" + strings[1]; //보낼 정보, GET방식으로 작성 ex) "id=rain483&pwd=1234";
                //보낼 데이터가 여러 개일 경우 &로 구분하여 작성
                osw.write(sendMsg);//OutputStreamWriter에 담아 전송
                osw.flush();

                //jsp와 통신이 정상적으로 되었을 때 실행될 코드
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    Log.i("통신 결과", conn.getResponseCode() + "결과"); //200일 경우 OK
                    conn.connect();
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    //jsp에서 보낸 값을 받음
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    tmp.close();
                    reader.close();
                    conn.disconnect();
                    receiveMsg = buffer.toString();
                    Log.i("jsonMsg", receiveMsg);

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러코드");
                    InputStream is = conn.getErrorStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
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

            //json 파싱
            Log.i("Parsing", "start");
            try{
                JSONObject receiveobject = new JSONObject(receiveMsg);
                JSONArray jarray = new JSONArray(receiveobject.getString("ListfromSQL"));
                String[] clothArray = new String[]{"updown", "type", "thickness"};

                if(jarray != null) {
                    JSONObject jobject = new JSONObject();
                    for(int i = 0; i < jarray.length(); i++ ) {
                        jobject = jarray.getJSONObject(i);
                        int updown = Integer.parseInt(jobject.getString("updown"));
                        if(updown == 1) { //상의
                            for(int j = 0; j < clothArray.length; j++) {
                                resultArray[0][i][j] = jobject.getString(clothArray[j]); //값 입력
                            }
                        } else if (updown == 2) { //하의
                            for(int j = 0; j < clothArray.length; j++) {
                                resultArray[1][i][j] = jobject.getString(clothArray[j]); //값 입력
                            }
                        } else if (updown == 3) { //원피스
                            for(int j = 0; j < clothArray.length; j++) {
                                resultArray[2][i][j] = jobject.getString(clothArray[j]); //값 입력
                            }
                        } else if (updown == 4) { //겉옷
                            for(int j = 0; j < clothArray.length; j++) {
                                resultArray[3][i][j] = jobject.getString(clothArray[j]); //값 입력
                            }
                        }
                    }


                    //임시 이미지 등록
                    if (resultArray[0] != null) {
                        for(int i = 0; i < resultArray[0].length; i++) {
                            if(Objects.equals(resultArray[0][i][1], "mantoman")) {
                                imgArray2[0][i] = R.drawable.mantoman;
                            }
                            if(Objects.equals(resultArray[0][i][1], "shirts")) {
                                imgArray2[0][i] = R.drawable.shirts;
                            }
                            if(Objects.equals(resultArray[0][i][1], "tshirts")) {
                                imgArray2[0][i] = R.drawable.tshirts;
                            }
                        }
                    }
                    if (resultArray[1] != null) {
                        for(int i = 0; i < resultArray[1].length; i++) {
                            if(Objects.equals(resultArray[1][i][1], "jean")) {
                                imgArray2[1][i] = R.drawable.jean;
                            }
                            if(Objects.equals(resultArray[1][i][1], "slacks")) {
                                imgArray2[1][i] = R.drawable.slacks;
                            }
                            if(Objects.equals(resultArray[1][i][1], "shortpants")) {
                                imgArray2[1][i] = R.drawable.shortpants;
                            }
                        }
                    }

                    if (resultArray[2] != null) {
                        for(int i = 0; i < resultArray[2].length; i++) {
                            if(Objects.equals(resultArray[2][i][1], "mantoman")) {

                            }
                            if(Objects.equals(resultArray[2][i][1], "shirts")) {

                            }
                            if(Objects.equals(resultArray[2][i][1], "tshirts")) {

                            }
                        }
                    }

                    if (resultArray[3] != null) {
                        for(int i = 0; i < resultArray[3].length; i++) {
                            if(Objects.equals(resultArray[3][i][0], "coat")) {
                                imgArray2[3][i] = R.drawable.coat;
                            }
                            if(Objects.equals(resultArray[3][i][0], "padding")) {

                            }
                            if(Objects.equals(resultArray[3][i][0], "longpadding")) {

                            }
                        }
                    }

                } else Log.i("jarray", "is null");
            }catch (Exception e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }

    //웹 업데이트 요청
    public void getWeather() {
        Log.e("get", "getWeather");
        Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Response 에서 리스너를 만들어서 결과를 받아올 수 있도록 함
                Log.e("response", response);

                boolean parsing = false;
                String value = response;
                try {
                    //{"coord":{"lon":127.5,"lat":37.5},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],
                    // "base":"stations","main":{"temp":299.8,"feels_like":301.3,"temp_min":299.15,"temp_max":300.15,"pressure":1016,
                    // "humidity":57},"visibility":10000,"wind":{"speed":1.5,"deg":290},"clouds":{"all":1},"dt":1591665989,
                    // "sys":{"type":1,"id":5513,"country":"KR","sunrise":1591646924,"sunset":1591699800},"timezone":32400,"id":1832830,"name":"Yangp'yŏng","cod":200}
                    JSONObject json = new JSONObject(value);
                    JSONObject current = json.getJSONObject("current");

                    getJsonData2(current);

                    JSONArray weathers = json.getJSONArray("hourly");
                    int k =0;
                    for (int i = 0; i < weathers.length(); i++) {
                        if(k==5)break;
                        if(i%3==0) {
                            getJsonData(weathers.getJSONObject(i),k++);
                        }
                    }

                    JSONArray daily=json.getJSONArray("daily");
                    getJsonData3(daily.getJSONObject(0));

                    //download(certcode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        };
        //HashMap

        GetRequest getRequest = new GetRequest(""+lat, ""+lng, responseListner, errorListener);

        // 실제 서버 응답 할 수 있는 tfRequest 생성
        if (queue == null) {
            queue = Volley.newRequestQueue( getContext());
        }
        // loginRequest를 queue에 담아 실행
        queue.add(getRequest);


    }

    public void getJsonData3(JSONObject jsonObject) {
        try {
            JSONObject dailytemp=jsonObject.getJSONObject("temp");
            double min=dailytemp.getDouble("min");
            double max=dailytemp.getDouble("max");
            tv_temp_min.setText(String.format("%.1f",min));
            tv_temp_max.setText(String.format("%.1f",max));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //현재 날씨 정보 주황색 바에 셋
    public void getJsonData2(JSONObject jsonObject) {
        try {
            long dt = jsonObject.getLong("dt");
            double temp = jsonObject.getDouble("temp");
            JSONArray weather = jsonObject.getJSONArray("weather");
            String icon = weather.getJSONObject(0).getString("icon").substring(0,2);


            tv_temp_detail.setText(String.format("%.1f",temp));

            switch (icon){
                case "01":
                    iv_weather_main.setBackgroundResource(R.drawable.w_01d);
                    iv_weather_detail.setBackgroundResource(R.drawable.w_01d);
                    tv_temp_main.setText(String.format("%.1f°/맑음",temp));
                    break;
                case "02":
                    iv_weather_main.setBackgroundResource(R.drawable.w_02d);
                    iv_weather_detail.setBackgroundResource(R.drawable.w_02d);
                    tv_temp_main.setText(String.format("%.1f°/구름낀해",temp));

                    break;
                case "03":
                    iv_weather_main.setBackgroundResource(R.drawable.w_03d);
                    iv_weather_detail.setBackgroundResource(R.drawable.w_03d);
                    tv_temp_main.setText(String.format("%.1f°/구름",temp));
                    break;
                case "04":
                    iv_weather_main.setBackgroundResource(R.drawable.w_04d);
                    iv_weather_detail.setBackgroundResource(R.drawable.w_04d);
                    tv_temp_main.setText(String.format("%.1f°/바람",temp));
                    break;
                case "05":
                    iv_weather_main.setBackgroundResource(R.drawable.w_05d);
                    iv_weather_detail.setBackgroundResource(R.drawable.w_05d);
                    tv_temp_main.setText(String.format("%.1f°/맑은/비",temp));
                    break;
                case "09":
                    iv_weather_main.setBackgroundResource(R.drawable.w_09d);
                    iv_weather_detail.setBackgroundResource(R.drawable.w_09d);
                    tv_temp_main.setText(String.format("%.1f°/비",temp));
                    break;
                case "11":
                    iv_weather_main.setBackgroundResource(R.drawable.w_11d);
                    iv_weather_detail.setBackgroundResource(R.drawable.w_11d);
                    tv_temp_main.setText(String.format("%.1f°/천둥번개",temp));
                    break;
                case "13":
                    iv_weather_main.setBackgroundResource(R.drawable.w_13d);
                    iv_weather_detail.setBackgroundResource(R.drawable.w_13d);
                    tv_temp_main.setText(String.format("%.1f°/눈",temp));
                    break;
                default:
                    iv_weather_main.setBackgroundResource(R.drawable.w_01d);
                    iv_weather_detail.setBackgroundResource(R.drawable.w_01d);
                    break;
            }


            //tv_time_temp[k]


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //날씨 정보 json 추출
    public void getJsonData(JSONObject jsonObject,int k) {
        try {
            long dt = jsonObject.getLong("dt");
            double temp = jsonObject.getDouble("temp");
            JSONArray weather = jsonObject.getJSONArray("weather");
            String icon = weather.getJSONObject(0).getString("icon").substring(0,2);
            Date d = new Date(dt * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("HH");


            tv_time[k].setText(sdf.format(d)+"시");
            getImage(icon,k);
            tv_time_temp[k].setText(String.format("%.1f°",temp));
            //tv_time_temp[k]

            System.out.println("datax" + sdf.format(d) + " " + temp+" "+icon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getImage(String icon, int k) {
        switch (icon){
            case "01": iv_weather[k].setBackgroundResource(R.drawable.w_01d);break;
            case "02": iv_weather[k].setBackgroundResource(R.drawable.w_02d);break;
            case "03": iv_weather[k].setBackgroundResource(R.drawable.w_03d);break;
            case "04": iv_weather[k].setBackgroundResource(R.drawable.w_04d);break;
            case "05": iv_weather[k].setBackgroundResource(R.drawable.w_05d);break;
            case "09": iv_weather[k].setBackgroundResource(R.drawable.w_09d);break;
            case "11": iv_weather[k].setBackgroundResource(R.drawable.w_11d);break;
            case "13": iv_weather[k].setBackgroundResource(R.drawable.w_13d);break;
        }
    }

    double lng;
    double lat;

    //현재 위치정보 받아오기
    public void getGPS() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            lng = lastKnownLocation.getLongitude();
            lat = lastKnownLocation.getLatitude();
            getCurrentAddress(lat,lng);
            Log.d("location", "longtitude=" + lng + ", latitude=" + lat);
            getWeather();

        }
    }
    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제

            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {

            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {

            return "주소 미발견";

        }

        Address address = addresses.get(0);
        tv_loc_detail.setText(address.getLocality());
        return address.getAddressLine(0).toString()+"\n";
    }

    public void onStart() { //fragment가 시작될 때
        weatherswitch.setChecked(false);
        getcloth();

        super.onStart();
    }
}