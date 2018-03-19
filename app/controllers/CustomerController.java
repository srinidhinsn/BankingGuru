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
public class CustomerController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result home(String bank, String accountno, String ifsc, String page) {
        DbOperations db = new DbOperations();
        Customer customer = db.getCustomer(accountno, ifsc);
        List<TransactionDetails> transactionDetailsList = null;
        customer.setPage(page);
        transactionDetailsList = db.getTransactions(customer);

        if ("fundtransferAction".equalsIgnoreCase(page)){

            TransactionDetails payeeAc = new TransactionDetails();
            TransactionDetails fromAc = new TransactionDetails();

            String from = request().body().asFormUrlEncoded().get("from")[0];
            String method = request().body().asFormUrlEncoded().get("method")[0];
            Double amount = Double.parseDouble(request().body().asFormUrlEncoded().get("amount")[0]);
            String customerIfsccode = request().body().asFormUrlEncoded().get("customerIfsccode")[0];
            String account = request().body().asFormUrlEncoded().get("account")[0];
            String payeeIfsccode = request().body().asFormUrlEncoded().get("payeeIfsccode")[0];

            payeeAc.setIfsccode(payeeIfsccode);
            payeeAc.setMaterial(method+"-"+customerIfsccode);
            payeeAc.setType("Credit");
            payeeAc.setFrom(from);
            payeeAc.setAccountNo(account);
            payeeAc.setAmount(amount);
            Customer payee = db.getCustomer(payeeAc.getAccountNo(), payeeAc.getIfsccode());
            List<TransactionDetails> payeeTd = null;
            if (null != payee) {
                payeeTd = db.getTransactions(payee);
                payeeAc.setBalance(payeeTd.get(0).getBalance()+payeeAc.getAmount());
            } else {
                payeeAc.setBalance(payeeAc.getAmount());
            }
            db.insertTransaction(payeeAc);

            fromAc.setIfsccode(customerIfsccode);
            fromAc.setMaterial(method+"-"+payeeIfsccode);
            fromAc.setType("Debit");
            if (null != payee){
                fromAc.setFrom(payee.getFirstName() + " "+ payee.getLastName()+"-ac#"+ account);
            } else {
                fromAc.setFrom(account);
            }

            fromAc.setAccountNo(accountno);
            fromAc.setAmount(amount);


            Customer customerAccount = db.getCustomer(fromAc.getAccountNo(), fromAc.getIfsccode());
            List<TransactionDetails> fromTd = db.getTransactions(customerAccount);
            if (fromAc.getAmount() > fromTd.get(0).getBalance()){
                customer.setErrorMessage("Money Transfer failed : Insufficient fund");
            } else {
                fromAc.setBalance(fromTd.get(0).getBalance()-fromAc.getAmount());
                fromTd.get(0).setBalance(fromAc.getBalance());
            }
            db.insertTransaction(fromAc);
            customer.setSuccessMessage("Money Transfer Successful");

            transactionDetailsList = db.getTransactions(customer);
        } else if("viewtransactionchart".equalsIgnoreCase(page)){
            transactionDetailsList = db.getTransactionChart(customer);
        } else if ("adhar".equalsIgnoreCase(page)){
            Customer c = db.getCustomer(accountno, ifsc);
            transactionDetailsList = db.getAdharLinks(c);
        }

        if (bank.equalsIgnoreCase("sbi")){
            return ok(sbihome.render(customer, transactionDetailsList));
        } else if (bank.equalsIgnoreCase("sc")){
            return ok(schome.render(customer, transactionDetailsList));
        }

        return ok(sclogin.render(""));
    }

}