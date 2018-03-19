package dboperations;

public class DBConstants {
    public static String getBanks = "SELECT * FROM public.bank;";
    public static String getUIDetails = "SELECT * FROM public.bank;";
    public static String getPageDetails = "SELECT * FROM public.bank;";
    public static String getCustomer = "SELECT * FROM public.customerdetails " +
            "where accountno = ? and ifsccode = ?";
    public static String authenticateCustomer = "SELECT * FROM public.customerdetails " +
            "where userid = ? and password = ? and bankname = ?;";
    public static String getTransactionDetails = "SELECT * FROM public.transactiondetails " +
            "where accountno=? and ifsccode=? order by datetime desc;";
    public static String getTransactionChart = "select to_char(datetime, 'Mon YYYY') as mon, sum(amount), type from transactiondetails " +
            "where accountno=? and ifsccode=? group by mon, type;";
    public static String insertTransactionDetails = "INSERT INTO public.transactiondetails" +
            "(accountno, type, material, datetime, \"from\", ifsccode, amount, balance) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    public static String getAdharLinks = "SELECT c.accountno, b.name, b.branch, b.ifsc FROM public.bank b, public.customerdetails c " +
            " where b.id = c.bankname and b.ifsc = c.ifsccode and c.adharno = ?;";

}
