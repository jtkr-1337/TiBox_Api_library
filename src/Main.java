public class Main {
    public static void main(String[] args) {
        String login = "karas";
        String pass = "EgorKaras";

        Api_connector api_con = new Api_connector(login, pass);
        System.out.println(api_con.get_user_token());
    }
}
