/**
 * Risepay API helper
 *
 * @category  API helper
 * @package   Risepay
 * @author    support@risepay.com
 * @copyright Copyright (c) 2014
 * @version   1.0

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
**/

package com.risepay;

import com.risepay.json.JSONObject;
import com.risepay.json.XML;
import com.risepay.net.http.Http;
import com.risepay.net.http.Post;


import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;







/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author Jhonn
 */
public class RisePay {
    
    private String username;
    private String password;
    private String url = "https://gateway1.risepay.com/ws/transact.asmx/ProcessCreditCard?";
    private String [] whitelist = {"TransType", "NameOnCard", "ExtData", "CardNum", "ExpDate", "CVNum", "Amount", "InvNum", "Zip", "Street","City", "MagData","PNRef", "UserName", "Password"};
    private String [] amountFields = {"Amount","TipAmt","TaxAmt"};
    
    public RisePay(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public void getGatewayUrl(String url){
        this.url = url;
    }
    
    public Map<String, Object> auth(Map<String, Object> data){
        data.put("TransType", "Auth");
        return prepareData(data);
    }
    
    public Map<String, Object> sale(Map<String, Object> data){
        data.put("TransType", "Sale");
        return prepareData(data);
    }
    
    //void lowercase keyword reserved
    public Map<String, Object> Void(Map<String, Object> data){
        data.put("TransType", "Void");
        return prepareData(data);
    }
    
    //return lowercase keyword reserved
    public Map<String, Object> Return(Map<String, Object> data){
        data.put("TransType", "Return");
        return prepareData(data);
    }
    
    public Map<String, Object> capture(Map<String, Object> data){
        data.put("TransType", "Force");
        return prepareData(data);
    }
    
    private Map<String, Object> prepareData(Map<String, Object> data) {
        data.put("UserName", username);
        data.put("Password", password);
        data.put("ExtData", "");
        
        // fix amounts
        double n ;
        for(Map.Entry<String,Object> param : data.entrySet()){
            if(inArray(String.valueOf(param.getKey()), amountFields)){
                n = parseAmount((int)param.getValue());
                data.put(param.getKey(), n);
            }
        }
        
        // Construct ExtData
        String next = "";
        for(Map.Entry<String,Object> param : data.entrySet()){
            if(!inArray(String.valueOf(param.getKey()), whitelist)){
                next += "<"+param.getKey()+">"+param.getValue()+"</"+param.getKey()+">";
                data.put("ExtData", next);
            }
        }
        //iterator HashMap remove data values !whitelist
        for(Iterator<Map.Entry<String,Object>>it=data.entrySet().iterator();it.hasNext();){
            Map.Entry<String, Object> entry = it.next();
            if(!inArray(String.valueOf(entry.getKey()), whitelist)){
                it.remove();
            }
        }
        
        // set defaults fields
        for(String w: whitelist){
            if(data.get(w)==null){
                data.put(w, "");
            }
        }
        
        return post(data);
    }
    private Map<String, Object> post(Map<String, Object> data) {
        Map<String, Object> resp = new HashMap<String, Object>();
        String content = "";
        
        try {
        StringBuilder postData = new StringBuilder();
        
        for (Map.Entry<String,Object> param : data.entrySet()) {     
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                 content = String.valueOf(postData);
            }
                Http.CONNECTION_TIMEOUT = 120000;
                Http.READ_TIMEOUT = 120000;
                Post post = Http.post(url, content).header("Content-Type", "application/x-www-form-urlencoded");
                String response = post.text();
                
                if(response.startsWith("<?xml")){
                    resp = convertResponse(response);
                }else{
                    resp.put("RespMSG", "Gateway error: " + response);
                    resp.put("Result", -999);
                    resp.put("Approved", false);
                    
                }
                
        } catch (Exception e) {
                resp.put("RespMSG", "Gateway error: " + e.getMessage());
                resp.put("Result", -999);
                resp.put("Approved", false);
            }
        
        return resp;
        
    }
    
    private Map<String, Object>  convertResponse(String xml) {
        JSONObject json = XML.toJSONObject(xml);
        
        //creating array HashMap
        // Cleanup array
        Map<String, Object> data = new HashMap<String, Object>();
        JSONObject j = new JSONObject();
        j = json.getJSONObject("Response");
        for (String fieldName : j.keySet())
        {
            data.put(fieldName, j.get(fieldName));
        }
        
        data.put("Approved",false);
        
        // Convert ExtData
        // Split plain data and XML into $matches array
        if(Integer.parseInt(data.get("Result").toString())==0){
            data.put("Approved",true);
            
            String extData = (String)data.get("ExtData");
            String match = "([,=0-9a-zA-Z]*)(\\<.*\\>)";
            String[] s = pregMatch(extData, match);
            if(s.length==1){
                String str = s[1];
                for(String f : str.split(",")){
                    String[] a = f.split("=");
                    data.put(a[0], a[1]);
                }
            }
            // Process XML part
            if(s.length==2){
                JSONObject xmldata = XML.toJSONObject(s[2]);
                for (String fieldName : xmldata.keySet())
                {
                    data.put(fieldName, xmldata.get(fieldName));
                }
                
            }
        }
        String[] jsonlist = {"xmlns:xsd", "xmlns:xsi", "xmlns", "ExtData"};
        for(String d : jsonlist){
            data.remove(d);
        }
        if(data.get("Message")==null)
            data.put("Message", "");
        
        if(data.get("RespMSG")!=null)
        data.put("Message", data.get("Message") +" "+ data.get("RespMSG")); 
        
        return data;
    }
    
    private String[] pregMatch(String str, String match) {
        
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(str);
        String[] ma = new String[3];
        
        if (matcher.find()) {
            ma[0] = matcher.group(0);
            ma[1] = matcher.group(1);
            ma[2] = matcher.group(2);
            
        } else {
            ma[0] = "0";
        }
        
        return ma;
    }
    
    private double parseAmount(int amount){
        String val = amount+"";
        BigDecimal big = new BigDecimal(val);
        big = big.setScale(2, RoundingMode.HALF_UP);
        Double amt = Double.parseDouble(big.toString());
        
        return amt;
    }
    
    
    
    private boolean inArray(String needle, String[] haystack) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return true;
            }
        }
        return false;
    }
    
    
}