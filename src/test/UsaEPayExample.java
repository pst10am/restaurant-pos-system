package test;

/*
 * Example.java
 *
 *  Copyright (c) 2009 USAePay
 *
 * Example usage of the usaepay jaxws library.  See
 * http://help.usaepay.com/developer/soap/howto/javajaxws  for more
 * information on using the java library.
 *
 */

import com.usaepay.api.jaxws.*;

/**
 *
 */
public class UsaEPayExample {

	static void testRunSale() {
		try {
			// instantiate client connection object, select www.usaepay.com
			// as the endpoint. Use sandbox.usaepay.com if connecting to
			// the sandbox server.
			UeSoapServerPortType client = usaepay
					.getClient("sandbox.usaepay.com");

			// Instantiate security token object (need by all soap methods)
			UeSecurityToken token = usaepay.getToken(
					"YOUR_SOURCE_KEY", // source key
					"1234", // source pin (if assigned by merchant)
					"127.0.0.1" // IP address of end client (if applicable)
			);

			// instantiate TransactionRequestObject
			TransactionRequestObject params = new TransactionRequestObject();

			// set card holder name
			params.setAccountHolder("Test Joe");

			// instantiate and populate transaction details
			TransactionDetail details = new TransactionDetail();
			details.setAmount(22.34);
			details.setDescription("My Test Sale");
			details.setInvoice("119891");
			params.setDetails(details);

			// populate credit card data
			CreditCardData ccdata = new CreditCardData();
			ccdata.setCardNumber("4000100011112224");
			ccdata.setCardExpiration("0919");
			ccdata.setCardCode("123");
			params.setCreditCardData(ccdata);

			// Create request object
			RunSale request = new RunSale();

			// Add security token and params to request
			request.setToken(token);
			request.setParams(params);

			// Create response object
			TransactionResponse response;

			// run sale
			response = client.runSale(token, params);

			// Display response
			System.out.println("Response: " + response.getResult()
					+ " RefNum: " + response.getRefNum());
		} catch (Exception e) {
			System.out.println("Soap Exception: " + e.getMessage());
		}
	}

	static void testRunAuthOnly() {

		try {
			// instantiate client connection object, select www.usaepay.com
			// as the endpoint. Use sandbox.usaepay.com if connecting to
			// the sandbox server.
			UeSoapServerPortType client = usaepay.getClient("sandbox.usaepay.com");

			// Instantiate security token object (need by all soap methods)
			UeSecurityToken token = usaepay.getToken(
					"YOUR_SOURCE_KEY", // source key
					"1234", // source pin (if assigned by merchant)
					"127.0.0.1" // IP address of end client (if applicable)
			);
			TransactionRequestObject params = new TransactionRequestObject();

			// set card holder name
			params.setAccountHolder("Test Joe");

			// populate transaction details
			TransactionDetail details = new TransactionDetail();
			details.setAmount(8.55);
			details.setDescription("My Test Sale");
			details.setInvoice("119891");
			params.setDetails(details);

			// populate credit card data
			CreditCardData ccdata = new CreditCardData();
			//ccdata.setMagStripe("");
			ccdata.setCardNumber("4444555566667779");
			ccdata.setCardExpiration("0912");
			ccdata.setCardCode("999");
			params.setCreditCardData(ccdata);

			// Create request object
			RunAuthOnly request = new RunAuthOnly();
			request.setToken(token);
			request.setParams(params);

			// Create response object
			TransactionResponse response;

			// run sale
			response = client.runAuthOnly(token, params);

			System.out.println("Result: " + response.getResult());
		} catch (Exception e) {
			System.out.println("Soap Exception: " + e.getMessage());
		}

	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		testRunSale();
		//testRunAuthOnly();
	}

}
