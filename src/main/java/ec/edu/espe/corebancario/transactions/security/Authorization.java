package ec.edu.espe.corebancario.transactions.security;

import com.mashape.unirest.http.Unirest;
import ec.edu.espe.corebancario.transactions.constants.DomainConstant;
import org.json.JSONObject;

public class Authorization {

    private String username;
    private String pwd;

    public Authorization(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public String tokenAuthorizate() throws Exception {
        try {
            JSONObject userAuthorizate = new JSONObject();
            userAuthorizate.put("username", this.username);
            userAuthorizate.put("pwd", this.pwd);
            String authorization = Unirest.post(DomainConstant.DOMAINLOGIN)
                    .header("Content-Type", "application/json")
                    .body(userAuthorizate).asJson().getBody().getObject().getString("token");
            return authorization;
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

}
