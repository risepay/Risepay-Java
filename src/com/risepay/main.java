/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.risepay;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Jhonn
 */
public class main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        RisePay risepay = new RisePay("demo","demo");
        
        Map<String, Object> data = new HashMap<String, Object>();
        
        data.put("NameOnCard", "Jhonn");
        data.put("CardNum", "5149612222222229");
        data.put("ExpDate", "1214");
        data.put("Amount", 1);
        data.put("CVNum", "678");
        
        Map<String, Object> resp = risepay.sale(data);
        
        if((boolean)resp.get("Approved")){
            System.out.println("Approved. Transaction ID = " + resp.get("PNRef"));
            System.out.println("AuthCode = " + resp.get("AuthCode"));
        }else{
            System.out.println("Declined: "+ resp.get("Message"));
        }
        
        
    }
    
}
