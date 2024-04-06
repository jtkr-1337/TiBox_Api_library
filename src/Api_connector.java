import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;


public class Api_connector {
    final String url = "http://api.arefaste.ru/";
    private final String secret_key = "52053e49-d268-4467-9a7b-6013ba82c966";
    private final String user_log, user_pass;
    private String auth_token, user_token;
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    public Api_connector(String login, String pass) {
        this.user_log = login;
        this.user_pass = pass;

        if (testConnection()){
            getAuthToken();
            getUserToken();
            if (!checkUserToken()) {
//            Log.i("ERRORINFO_USERTOKEN", "USER TOKEN DOESN'T EXIST");
                System.out.println("USER TOKEN DOESN'T EXIST");
            }
        }
    }

    private boolean testConnection() {
        String url = this.url + "system.auth";
        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        return call.status;
    }


    /*-------------- SERVER METHODS --------------*/
    private void getAuthToken() {
        String id_app = "2";
        String url = this.url + "system.auth?login=" + this.user_log
                + "&password=" + this.user_pass
                + "&id_app=" + id_app
                + "&secret_key=" + this.secret_key;
        System.out.println(url);

        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        System.out.println(call.response);
        JSONObject data = new JSONObject(call.response);
        data = (JSONObject) data.get("response");
        this.auth_token = data.getString("auth_token");
    }

    private void getUserToken() {
        String url = this.url + "system.getToken?auth_token=" + this.auth_token
                + "&secret_key=" + this.secret_key;

        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        JSONObject data = new JSONObject(call.response);
        data = (JSONObject) data.get("response");
        this.user_token = data.getString("user_token");

    }

    private boolean checkUserToken() {
        String url = this.url + "system.ping?user_token=" + this.user_token;

        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        return call.status;
    }


    /*-------------- USER METHODS --------------*/
    public JSONObject getUserInfo() {
        String url = this.url + "user.getInfo?user_token=" + this.user_token;

        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        return new JSONObject(call.response);
    }

    public JSONObject getUserGroup() {
        String url = this.url + "user.getGroup?user_token=" + this.user_token;

        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        return new JSONObject(call.response);
    }

    public String changePassword(String old_pass, String new_pass) {
        String url = this.url + "user.getInfo?user_token=" + this.user_token
                + "&old_password=" + old_pass
                + "&password=" + new_pass;

        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        JSONObject data = new JSONObject(call.response);
        data = (JSONObject) data.get("response");

        return data.getString("time_token");
    }

    public String changeLogin(String new_login) {
        String url = this.url + "user.getInfo?user_token=" + this.user_token
                + "&new_login=" + new_login;

        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        JSONObject data = new JSONObject(call.response);
        data = (JSONObject) data.get("response");

        return data.getString("time_token");
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
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        JSONObject data = new JSONObject(call.response);
        data = (JSONObject) data.get("response");

        return data;
    }

    public JSONObject getLesson(int lesson) {
        String url = this.url + "user.getInfo?user_token=" + this.user_token
                + "&id_lesson=" + lesson;

        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        JSONObject data = new JSONObject(call.response);
        data = (JSONObject) data.get("response");

        return data;
    }

    public JSONObject getTeacher(int teacher) {
        String url = this.url + "user.getInfo?user_token=" + this.user_token
                + "&id_teacher=" + teacher;

        Callb call = new Callb();
        Request request = new Request.Builder().url(url).get().build();
        this.client.newCall(request).enqueue(call);

        JSONObject data = new JSONObject(call.response);
        data = (JSONObject) data.get("response");

        return data;
    }


    /*-------------- GET METHODS --------------*/
    public String get_auth_token(){
        return this.auth_token;
    }
}

class Callb implements Callback {
    public boolean status;
    public String response;

    @Override
    public void onFailure(Call call, IOException e) {
        this.status = false;
//        Log.i("ERRORINFO_CALLBACK_CONNECTION", e.toString());
        System.out.println(e.toString());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try (ResponseBody responseBody = response.body()) {
            if (!response.isSuccessful()) {
//                Log.i("ERRORINFO_RESPONSE","Запрос к серверу не был успешен: " + response.code() + " " + response.message());
                System.out.println("Запрос к серверу не был успешен: " + response.code() + " " + response.message());
                this.status = false;
                System.out.println(responseBody.string());
            } else {
                this.response = responseBody.string();
                JSONObject data = new JSONObject(this.response);
                this.status = Boolean.parseBoolean((String) data.get("status"));
            }
        }
    }
}