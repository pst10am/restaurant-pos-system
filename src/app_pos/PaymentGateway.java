package app_pos;

import java.math.BigInteger;

import model.TbPayment;
import resrc.ResCfg;

import com.usaepay.api.jaxws.BatchStatus;
import com.usaepay.api.jaxws.CloseBatchRequest;
import com.usaepay.api.jaxws.CloseBatchResponse;
import com.usaepay.api.jaxws.CreditCardData;
import com.usaepay.api.jaxws.RunAuthOnly;
import com.usaepay.api.jaxws.TransactionDetail;
import com.usaepay.api.jaxws.TransactionRequestObject;
import com.usaepay.api.jaxws.TransactionResponse;
import com.usaepay.api.jaxws.UeSecurityToken;
import com.usaepay.api.jaxws.UeSoapServerPortType;
import com.usaepay.api.jaxws.usaepay;

public class PaymentGateway {
	
	public static void closeBatch() 
		throws Exception {
		
		UeSoapServerPortType client = usaepay.getClient(ResCfg.getUEP_EndPoint());
		
		// Instantiate security token object (need by all soap methods)
		UeSecurityToken token = usaepay.getToken(
				ResCfg.getUEP_SourceKey(), // source key
				ResCfg.getUEP_SourcePin(), // "1234", // source pin (if assigned by merchant)
				ResCfg.getUEP_ClientIP()   // "127.0.0.1" // IP address of end client (if applicable)
		);
		
		CloseBatchRequest btreq = new CloseBatchRequest();
		
		btreq.setToken(token);
		
		//Setting batchRefNum to 0 for current batch, for a different batch set to the Batch Ref Num
		btreq.setBatchRefNum(BigInteger.ZERO);
		
		client.closeBatch(btreq);
	}
	
	public static BatchStatus getCurrentBatchStatus() throws Exception {
		UeSoapServerPortType client = usaepay.getClient(ResCfg.getUEP_EndPoint());
		UeSecurityToken token = usaepay.getToken(
			ResCfg.getUEP_SourceKey(), // source key
			ResCfg.getUEP_SourcePin(), // "1234", // source pin (if assigned by merchant)
			ResCfg.getUEP_ClientIP()   // "127.0.0.1" // IP address of end client (if applicable)
		);
		return client.getBatchStatus(token, BigInteger.ZERO);
	}
	
	public static TransactionResponse captureTrans(TbPayment _pm) 
		throws Exception {
		
		UeSoapServerPortType client = usaepay.getClient(ResCfg.getUEP_EndPoint());

		// Instantiate security token object (need by all soap methods)
		UeSecurityToken token = usaepay.getToken(
				ResCfg.getUEP_SourceKey(), // source key
				ResCfg.getUEP_SourcePin(), // "1234", // source pin (if assigned by merchant)
				ResCfg.getUEP_ClientIP()   // "127.0.0.1" // IP address of end client (if applicable)
		);
		
		TransactionResponse rspx = client.captureTransaction(token, 
			new BigInteger(_pm.getTxNo()), _pm.getPmAmt()+_pm.getPmTip());
		
		return rspx;
	}
	

	public static boolean voidTransaction(TbPayment _pm) 
		throws Exception {
		
		UeSoapServerPortType client = usaepay.getClient(ResCfg.getUEP_EndPoint());

		// Instantiate security token object (need by all soap methods)
		UeSecurityToken token = usaepay.getToken(
				ResCfg.getUEP_SourceKey(), // source key
				ResCfg.getUEP_SourcePin(), // "1234", // source pin (if assigned by merchant)
				ResCfg.getUEP_ClientIP()   // "127.0.0.1" // IP address of end client (if applicable)
		);
		
		return client.voidTransaction(token, new BigInteger(_pm.getTxNo()));
	}

	public static TransactionResponse authenOnly(
			double _amt, String _descr, String _invNo, TbPayment _pm
		) throws Exception {
		
		// instantiate client connection object, select www.usaepay.com
		// as the endpoint. Use sandbox.usaepay.com if connecting to
		// the sandbox server "sandbox.usaepay.com".;
		UeSoapServerPortType client = usaepay.getClient(ResCfg.getUEP_EndPoint());

		// Instantiate security token object (need by all soap methods)
		UeSecurityToken token = usaepay.getToken(
				ResCfg.getUEP_SourceKey(), // source key
				ResCfg.getUEP_SourcePin(), // "1234", // source pin (if assigned by merchant)
				ResCfg.getUEP_ClientIP()   // "127.0.0.1" // IP address of end client (if applicable)
		);
		TransactionRequestObject params = new TransactionRequestObject();

		// set card holder name
		params.setAccountHolder(_pm.getCCAcctName()); // "Test Joe");

		// populate transaction details
		TransactionDetail details = new TransactionDetail();
		details.setAmount(_amt);
		details.setDescription(_descr); // "My Test Sale");
		details.setInvoice(_invNo); // "119891");
		
		params.setDetails(details);

		// -----
		
		// populate credit card data
		CreditCardData ccdata = new CreditCardData();
		ccdata.setCardPresent(_pm.isCardSwiped());
		if (_pm.isCardSwiped()) {
			ccdata.setMagStripe(_pm.getCCTrk1()+_pm.getCCTrk2());
		} else {
			ccdata.setCardNumber(_pm.getCCNo());
			ccdata.setCardExpiration(_pm.getExpMMYY()); // 0912 MMYY
			ccdata.setCardCode(_pm.getCCCVV()); // CVV2/CID 
		}
		
		params.setCreditCardData(ccdata);

		// Create request object
		RunAuthOnly request = new RunAuthOnly();
		request.setToken(token);
		request.setParams(params);

		// Create response object
		TransactionResponse rspx = client.runAuthOnly(token, params);

		//System.out.println("Result: " + response.getResult());
		return rspx;
	}
}
