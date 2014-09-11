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
  RisePay risepay = new RisePay("gatewayApiUser","userPassword");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
  data.put("TipAmt", 1);
  data.put("Customer", "JR");
        
  System.out.println(risepay.sale(data)); 	
  ```
	
Object API:
  ```java	
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
  data.put("TipAmt", 1);
  data.put("Customer", "JR");
        
  System.out.println(risepay.sale(data));    
  .......
  ```	
        
### Authorization Transaction
To make an authorization using a credit card:
	
	Functional API:	
  ```java	
  RisePay risepay = new RisePay("gatewayApiUser","userPassword");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
  data.put("TipAmt", 1);
  data.put("Customer", "JR");
        
  System.out.println(risepay.auth(data)); 	
  ```
### Void Transaction

To void a transaction:
  ```java	
  Functional API:	
	
  RisePay risepay = new RisePay("gatewayApiUser","userPassword");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
  data.put("TipAmt", 1);
  data.put("Customer", "JR");
        
  System.out.println(risepay.Void(data)); 
  ```
### Capture Transaction

To capture a previously Authorized transaction:
  	
	Functional API:	
  ```java	
  RisePay risepay = new RisePay("gatewayApiUser","userPassword");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
  data.put("TipAmt", 1);
  data.put("Customer", "JR");
        
  System.out.println(risepay.capture(data)); 
  ```

### Return Transaction

To return a payment for already batched transaction:
	
Functional API:	
  ```java	
  RisePay risepay = new RisePay("gatewayApiUser","userPassword");
        
  Map<String, Object> data = new HashMap<String, Object>();
        
  data.put("NameOnCard", "Jhonn");
  data.put("CardNum", "5149612222222229");
  data.put("ExpDate", "1214");
  data.put("Amount", 10);
  data.put("CVNum", "678");
  data.put("TipAmt", 1);
  data.put("Customer", "JR");
        
  System.out.println(risepay.Return(data)); 
  ```

To see complete list of RisePay API variables, review our <a href='https://gateway1.risepay.com/vt/nethelp/Documents/processcreditcard.htm'>online documentation</a>. </br> You can request developer credentials from our <a href='http://sales.risepay.com/rise-dev-access.html'>Dev Portal</a>.  If you would like to certify your application, then submit a <a href='http://sales.risepay.com/rise-cert-lab-access.html'>Cert Lab request</a>.	

