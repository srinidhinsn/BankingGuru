package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dboperations.DBConstants;
import dboperations.DbOperations;
import play.mvc.*;
import views.html.*;
import vo.Customer;
import play.mvc.Http.RequestBody;
import vo.TransactionDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class BankingController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result home(String bank, String page) {
        DbOperations db = new DbOperations();
        if ("login".equalsIgnoreCase(page)){
            if (bank.equalsIgnoreCase("sbi")){
                return ok(sbilogin.render(""));
            } else if (bank.equalsIgnoreCase("sc")){
                return ok(sclogin.render(""));
            }
        } else if ("loginAction".equalsIgnoreCase(page)){
                try {
                    String errorMessage = "Error : Invalid username/password";
                    String username = request().body().asFormUrlEncoded().get("username")[0];
                    String password = request().body().asFormUrlEncoded().get("password")[0];
                    Customer customer = db.authenticateCustomer(username, password, bank);
                    List<TransactionDetails> transactionDetailsList = db.getTransactions(customer);

                    switch (bank){
                        case "sc": if (null != customer){
                            return ok(schome.render(customer,transactionDetailsList));
                        } else return ok(sclogin.render(errorMessage));

                        case "sbi": if (null != customer){
                            return ok(sbihome.render(customer,transactionDetailsList));
                        } else return ok(sbilogin.render(errorMessage));
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }

        } else if ("".equalsIgnoreCase(page)){

        }
        else {
            List<Map<String, Object>> banks = db.getBanks(DBConstants.getBanks, null);
            return ok(index.render("Banking Guru",banks));
        }

        /*Person person = new Person();
        person.firstName = "Foo";
        person.lastName = "Bar";
        person.age = "30";
        Gson gson = new Gson();
        JsonObject personJson = new JsonObject();
        List<Map<String, Object>> person2 = new ArrayList<>();
        Map map = new HashMap();
        map.put("name","Srinidhi");
        map.put("age","30");
        person2.add(map);*/
/*

        DbOperations db = new DbOperations();
        Map<String, String> params = new HashMap<>();
        params.put("bankname",bank);
        params.put("pagename", page);
        List<Map<String, Object>> uiDetails = db.getBanks(DBConstants.getUIDetails, params);
        List<Map<String, Object>> pageDetails = db.getBanks(DBConstants.getUIDetails, params);
        List<Map<String, Object>> gridDetails = db.getBanks(DBConstants.getUIDetails, params);
*/
        return ok(sclogin.render(""));
    }
/*
    public Result bankHome(String bank, String page, List<Map<String, Object>> pageValues) {

        DbOperations db = new DbOperations();
        Map<String, String> params = new HashMap<>();
        params.put("bankname",bank);
        params.put("pagename", page);
        List<Map<String, Object>> uiDetails = db.getBanks(DBConstants.getUIDetails, params);
        List<Map<String, Object>> pageDetails = db.getBanks(DBConstants.getUIDetails, params);
        List<Map<String, Object>> gridDetails = db.getBanks(DBConstants.getUIDetails, params);
        return ok(index.render("Banking Guru"));
    }*/
}