/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab1;

/**
 *
 * @author MehmetFirat
 */
public class YazLab1 {

    /**
     * @param args the command line arguments
     */
    private String host,uName,uPass;
    public YazLab1() throws ClassNotFoundException {
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        Class.forName("com.mysql.jdbc.Driver");
        host = "jdbc:mysql://localhost:3301/yazlab1?useLegacyDatetimeCode=false&serverTimezone=America/New_York";
        uName = "root";
        uPass = "";
    }
    public String getHost(){
        return host;
    }
    public String getUName(){
        return uName;
    }
    public String getUPass(){
        return uPass;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
