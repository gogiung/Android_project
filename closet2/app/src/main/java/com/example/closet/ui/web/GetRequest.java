package com.example.closet.ui.web;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;


public class GetRequest extends StringRequest { //StringRequest를 상속받아 사용

    final static private String URL = "http://api.openweathermap.org/data/2.5/onecall";
   // onecall?lat=37.441792&lon=127.037689&%20exclude=hourly&appid=d661d60607b30f027c3afc53ced35aca&units=metric


    private Map<String, String> parameters;


    public GetRequest(String lat, String lon, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        // LoginRequest는 유저 아이디, 비밀번호, 응답을 받을 수 있는 리스너 생성자구문
        super(Method.GET,URL+"?lat="+lat+"&lon="+lon+"&APPID="+"d661d60607b30f027c3afc53ced35aca&units=metric", listener, errorListener);

    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
