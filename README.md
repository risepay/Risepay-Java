RisePay-Java -- Simple Risepay Payment API wrapper 

<hr>
You can request developer credentials from our <a href='http://sales.risepay.com/rise-dev-access.html'>Dev Portal</a>.</br> If you would like to certify your application, then submit a <a href='http://sales.risepay.com/rise-cert-lab-access.html'>Cert Lab request</a>.
<hr>

### Table of Contents
**[Initialization](#initialization)**

**[Sale Transaction](#sale-transaction)**

**[Auth Transaction](#authorization-transaction)**

**[Void Transaction](#void-transaction)**

**[Return Transaction](#return-transaction)**

**[Capture Transaction](#capture-transaction)**



### Initialization
To utilize this class, Risepay.java into your project, and require it.
create a new instance of the class.

  ```java	
  RisePay risepay = new RisePay("gatewayApiUser","userPassword");
  ```


### Sale Transaction
To make a purchase using a credit card:

Functional API:	
  ```java	
  RisePay risepay = new RisePay("demo","demo");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
        
  Map<String, Object> resp = risepay.sale(data);
  if((boolean)resp.get("Approved")){
    System.out.println("Approved. Transaction ID = " + resp.get("PNRef"));
    System.out.println("AuthCode = " + resp.get("AuthCode"));
  }else{
    System.out.println("Declined: "+ resp.get("Message"));
        }
  ```
	
Object API:
  ```java	
  RisePay risepay = new RisePay("demo","demo");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
        
  Map<String, Object> resp = risepay.sale(data);   
  .......
  ```	
        
### Authorization Transaction
To make an authorization using a credit card:
	
Functional API:	
  ```java	
  RisePay risepay = new RisePay("demo","demo");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
        
  Map<String, Object> resp = risepay.auth(data);
  if((boolean)resp.get("Approved")){
    System.out.println("Approved. Transaction ID = " + resp.get("PNRef"));
    System.out.println("AuthCode = " + resp.get("AuthCode"));
  }else{
    System.out.println("Declined: "+ resp.get("Message"));
        }	
  ```
### Void Transaction

To void a transaction:
  ```java	
  Functional API:	
	
   RisePay risepay = new RisePay("demo","demo");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
  data.put("PNRef", 1106169);
        
  Map<String, Object> resp = risepay.Void(data);
  if((boolean)resp.get("Approved")){
    System.out.println("Approved. Transaction ID = " + resp.get("PNRef"));
    System.out.println("AuthCode = " + resp.get("AuthCode"));
  }else{
    System.out.println("Declined: "+ resp.get("Message"));
        } 
  ```
### Capture Transaction

To capture a previously Authorized transaction:
  	
	Functional API:	
  ```java	
   RisePay risepay = new RisePay("demo","demo");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
  data.put("PNRef", 1106169);
        
  Map<String, Object> resp = risepay.capture(data);
  if((boolean)resp.get("Approved")){
    System.out.println("Approved. Transaction ID = " + resp.get("PNRef"));
    System.out.println("AuthCode = " + resp.get("AuthCode"));
  }else{
    System.out.println("Declined: "+ resp.get("Message"));
        }
  ```

### Return Transaction

To return a payment for already batched transaction:
	
Functional API:	
  ```java	
   RisePay risepay = new RisePay("demo","demo");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
  data.put("PNRef", 1106169);
        
  Map<String, Object> resp = risepay.Return(data);
  if((boolean)resp.get("Approved")){
    System.out.println("Approved. Transaction ID = " + resp.get("PNRef"));
    System.out.println("AuthCode = " + resp.get("AuthCode"));
  }else{
    System.out.println("Declined: "+ resp.get("Message"));
        }
  ```

To see complete list of RisePay API variables, review our <a href='https://gateway1.risepay.com/vt/nethelp/Documents/processcreditcard.htm'>online documentation</a>. </br> You can request developer credentials from our <a href='http://sales.risepay.com/rise-dev-access.html'>Dev Portal</a>.  If you would like to certify your application, then submit a <a href='http://sales.risepay.com/rise-cert-lab-access.html'>Cert Lab request</a>.	

