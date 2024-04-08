import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;


public class Api_connector {
    final String url = "http://api.arefaste.ru/";
    private final String secret_key = "52053e49-d268-4467-9a7b-6013ba82c966";
    private final String user_log, user_pass;
    private String auth_token, user_token;
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    public Api_connector(String login, String pass) {
        this.user_log = login;
        this.user_pass = pass;

//        boolean flag = testConnection();
//        System.out.println("connection is " + flag);
        if (true) {
            getAuthToken();
            getUserToken();
        }else{
//            Log.i("ERRORINFO_SERVER", "SERVER ISN'T RESPONDING");
            System.out.println("SERVER ISN'T RESPONDING");
        }
    }

    /*-------------- SERVER METHODS --------------*/
    private void getAuthToken() {
        System.out.println("Ввкдите код подтверждения: ");
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        String secret_key = System.in.toString();
        String id_app = "2";
        String url = this.url + "system.auth?login=" + this.user_log
                + "&password=" + this.user_pass
                + "&id_app=" + id_app
                + "&secret_key=" + this.secret_key;
        System.out.println("getAuthToken url: " + url);

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        System.out.println("request created");
        this.client.newCall(request).enqueue(call);
        System.out.println("call created");

        JSONObject data = new JSONObject();
        System.out.println("data created");
        try {
            data = call.response;
            this.auth_token = data.getString("auth_token");
            System.out.println("auth_token: " + this.auth_token);
        } catch (JSONException e) {
//            Log.i("ERRORINFO_JSON", e.toString());
            System.out.println("ERRORINFO_JSON: " + e.toString());
        }
    }

    private void getUserToken() {
        String url = this.url + "system.getToken?auth_token=" + this.auth_token
                + "&secret_key=" + this.secret_key;
        System.out.println(url);

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        this.client.newCall(request).enqueue(call);

        try {
            JSONObject data = call.response;
            this.user_token = data.getString("user_token");
        } catch (JSONException e) {
//            Log.i("ERRORINFO_JSON", e.toString());
            System.out.println("ERRORINFO_JSON: " + e.toString());
        }
    }

    private boolean testConnection(){
        String url = this.url + "system.ping";
        boolean status = false;
        String resp = "";

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        this.client.newCall(request).enqueue(call);
        return status;

//        System.out.println("response is " + call.response);
//        System.out.println("response status is " + call.status);
//        return call.status;
//        Log.i("DEBUGINFO", call.response);
    }

    /*-------------- USER METHODS --------------*/
    public JSONObject getUserInfo() {
        JSONObject obj = new JSONObject();
        String url = this.url + "user.getInfo?user_token=" + this.user_token;

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        this.client.newCall(request).enqueue(call);

        try{
            obj = call.response;
        } catch (Exception e) {
//            Log.i("ERRORINFO_JSON", e.toString());
            System.out.println("ERRORINFO_JSON: " + e.toString());
        }
        return obj;
    }

    public JSONObject getUserGroup() {
        String url = this.url + "user.getGroup?user_token=" + this.user_token;

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        this.client.newCall(request).enqueue(call);

        return call.response;
    }

    public String changePassword(String old_pass, String new_pass) {
        String resp = "";
        String url = this.url + "user.getInfo?user_token=" + this.user_token
                + "&old_password=" + old_pass
                + "&password=" + new_pass;

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        this.client.newCall(request).enqueue(call);

        try{
            JSONObject data = call.response;
            resp = data.getString("time_token");
        } catch (JSONException e) {
//            Log.i("ERRORINFO_JSON", e.toString());
            System.out.println("ERRORINFO_JSON: " + e.toString());
        }
        return resp;
    }

    public String changeLogin(String new_login) {
        String resp = "";
        String url = this.url + "user.getInfo?user_token=" + this.user_token
                + "&new_login=" + new_login;

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        this.client.newCall(request).enqueue(call);

        try{
            JSONObject data = call.response;
            resp = data.getString("time_token");
        } catch (JSONException e) {
//            Log.i("ERRORINFO_JSON", e.toString());
            System.out.println("ERRORINFO_JSON: " + e.toString());
        }
        return resp;
    }


    /*-------------- TIMETABLE METHODS --------------*/
    public JSONObject getTimetableDay(String date) {
        return getTimetableDay(date, "", -1);
    }
    public JSONObject getTimetableDay(String date, String teacher) {
        return getTimetableDay(date, teacher, -1);
    }
    public JSONObject getTimetableDay(String date, int lesson) {
        return getTimetableDay(date, "", lesson);
    }
    public JSONObject getTimetableDay(String date, String teacher, int lesson) {
        if (Objects.equals(teacher, "") && lesson == -1) {
            String url = this.url + "user.getInfo?user_token=" + this.user_token
                    + "&date=" + date;
        } else
        if (Objects.equals(teacher, "")) {
            String url = this.url + "user.getInfo?user_token=" + this.user_token
                    + "&date=" + date
                    + "&id_lesson=" + lesson;
        } else
        if (lesson == -1) {
            String url = this.url + "user.getInfo?user_token=" + this.user_token
                    + "&date=" + date
                    + "&id_teacher=" + teacher;
        }
        else {
            String url = this.url + "user.getInfo?user_token=" + this.user_token
                    + "&date=" + date
                    + "&id_teacher=" + teacher
                    + "&id_lesson=" + lesson;
        }

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        this.client.newCall(request).enqueue(call);

        JSONObject data = new JSONObject();
        try{
            data = call.response;
        } catch (Exception e) {
//            Log.i("ERRORINFO_JSON", e.toString());
            System.out.println("ERRORINFO_JSON: " + e.toString());
        }

        return data;
    }

    public JSONObject getLesson(int lesson) {
        String url = this.url + "user.getInfo?user_token=" + this.user_token
                + "&id_lesson=" + lesson;

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        this.client.newCall(request).enqueue(call);

        JSONObject data = new JSONObject();
        try{
            data = call.response;
        } catch (Exception e) {
//            Log.i("ERRORINFO_JSON", e.toString());
            System.out.println("ERRORINFO_JSON: " + e.toString());
        }

        return data;
    }

    public JSONObject getTeacher(int teacher) {
        String url = this.url + "user.getInfo?user_token=" + this.user_token
                + "&id_teacher=" + teacher;

        Callb call = new Callb();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "TiboxMobileApp")
                .url(url).get().build();
        this.client.newCall(request).enqueue(call);

        JSONObject data = new JSONObject();
        try{
            data = call.response;
        } catch (Exception e) {
//            Log.i("ERRORINFO_JSON", e.toString());
            System.out.println("ERRORINFO_JSON: " + e.toString());
        }

        return data;
    }


    /*-------------- GET METHODS --------------*/
    public String get_user_token(){
        return this.user_token;
    }
}

class Callb implements Callback {
    public boolean status;
    String resp;
    public JSONObject response;

    @Override
    public void onFailure(Call call, IOException e) {
        this.status = false;
//        Log.i("ERRORINFO_CALLBACK_CONNECTION", e.toString());
        System.out.println("ERRORINFO_JSON: " + e.toString());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            if (!response.isSuccessful() && response.code()!=418) {
//                Log.i("ERRORINFO_RESPONSE","Запрос к серверу не был успешен: " + response.code() + " " + response.message());
                System.out.println("Запрос к серверу не был успешен: " + response.code() + " " + response.message());
                this.status = false;
                this.resp = response.body().string();
                System.out.println(this.resp);
            } else {
                this.resp = response.body().string();
                System.out.println("response: " + this.resp);
                try{
                    JSONObject data = new JSONObject(this.resp);

                    String status1 = String.valueOf(data.get("status"));
                    this.status = Boolean.parseBoolean(status1);

                    data = (JSONObject) data.get("response");
                    this.response = data;

                    System.out.println(this.status);
                    System.out.println(this.response);
                } catch (JSONException e) {
//                    Log.i("ERRORINFO_JSON", e.toString());
                    System.out.println("ERRORINFO_JSON: " + e.toString());
                }
            }
        } catch (Exception e){
            System.out.println("response error: " + e.toString());
        }
    }
}