package test;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class TestSOAP {
	
	static void run1() {
		try {
            // Create SOAP Connection
			
            SOAPConnectionFactory sfact = SOAPConnectionFactory.newInstance();
            SOAPConnection sconn = sfact.createConnection();

            // Send SOAP Message to SOAP Server
            
            String url = "https://posgateway.cert.secureexchange.net/Hps.Exchange.PosGateway/POSGatewayService.asmx";
            //String url = "https://posgateway.secureexchange.net/Hps.Exchange.PosGateway/PosGatewayService.asmx?op=DoTransaction";
            
            SOAPMessage soapResponse = sconn.call(createSOAPRequest(), url);

            // Process the SOAP Response
            printSOAPResponse(soapResponse);

            sconn.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }		
	}
	
	private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://Hps.Exchange.PosGateway";

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.removeNamespaceDeclaration("SOAP-ENV");  
        envelope.setPrefix("soapenv");  
        envelope.addNamespaceDeclaration("hps", serverURI);
        
        SOAPHeader header1 = envelope.getHeader();
        header1.removeNamespaceDeclaration("SOAP-ENV");  
        header1.setPrefix("soapenv");  
        
        SOAPBody body1 = envelope.getBody();
        body1.removeNamespaceDeclaration("SOAP-ENV");  
        body1.setPrefix("soapenv");  
        
        String prfx1 = "hps";
        
        SOAPElement elm1 = body1.addChildElement("PosRequest", prfx1);
        elm1.addAttribute(new QName("clientType"), "");
        elm1.addAttribute(new QName("clientVer"), "");
        
        SOAPElement elmVer = elm1.addChildElement("Ver1.0", prfx1);
        
        SOAPElement elmHead = elmVer.addChildElement("Header", prfx1);
        String[][] headerValues = {
        		{"LicenseId","YOUR_LICENSE_ID"},
        		{"SiteId","YOUR_SITE_ID"},
        		{"DeviceId","YOUR_DEVICE_ID"},
        		{"UserName","YOUR_USERNAME"},
        		{"Password","YOUR_PASSWORD"},
        		{"SiteTrace",""},
        		{"DeveloperID","YOUR_DEVELOPER_ID"},
        		{"VersionNbr","1364"},
        		{"ClerkID","clerk2"}
        	};
        for (String[] hvals : headerValues) {
        	SOAPElement _elm = elmHead.addChildElement(hvals[0], prfx1);
        	_elm.addTextNode(hvals[1]);
    	}
        
        SOAPElement elmTrans = elmVer.addChildElement("Transaction", prfx1);
        SOAPElement elmCrdSale = elmTrans.addChildElement("CreditSale", prfx1);
        SOAPElement elmBlock1 = elmCrdSale.addChildElement("Block1", prfx1);
        SOAPElement elmCardData = elmBlock1.addChildElement("CardData", prfx1);
        
        SOAPElement elmTrackData = elmCardData.addChildElement("TrackData", prfx1);
        elmTrackData.addAttribute(new QName("method"), "swipe");
        elmTrackData.addTextNode("%B5473500000000014^MC TEST CARD^251210199998888777766665555444433332?;5473500000000014=25121019999888877776?");
        
        SOAPElement elmAmt = elmBlock1.addChildElement("Amt", prfx1);
        elmAmt.addTextNode("55");
        
        SOAPElement elmTip = elmBlock1.addChildElement("GratuityAmtInfo", prfx1);
        elmTip.addTextNode("2");
        
        SOAPElement elmCPC = elmBlock1.addChildElement("CPCReq", prfx1);
        elmCPC.addTextNode("N");
        
        SOAPElement elmCH = elmBlock1.addChildElement("CardHolderData", prfx1);
        SOAPElement elmCH_fname = elmCH.addChildElement("CardHolderFirstName", prfx1);
        elmCH_fname.addTextNode("FirstN");
        SOAPElement elmCH_lname = elmCH.addChildElement("CardHolderLastName", prfx1);
        elmCH_lname.addTextNode("LastN");
        SOAPElement elmCH_addr = elmCH.addChildElement("CardHolderAddr", prfx1);
        elmCH_addr.addTextNode("1234 N Chicago Ave");
        SOAPElement elmCH_city = elmCH.addChildElement("CardHolderCity", prfx1);
        elmCH_city.addTextNode("Chicago");
        SOAPElement elmCH_state = elmCH.addChildElement("CardHolderState", prfx1);
        elmCH_state.addTextNode("IL");
        SOAPElement elmCH_zip = elmCH.addChildElement("CardHolderZip", prfx1);
        elmCH_zip.addTextNode("00000");
        SOAPElement elmCH_phone = elmCH.addChildElement("CardHolderPhone", prfx1);
        elmCH_phone.addTextNode("5555550100");
        SOAPElement elmCH_email = elmCH.addChildElement("CardHolderEmail", prfx1);
        elmCH_email.addTextNode("first.last@example.com");
        
        SOAPElement elmDup = elmBlock1.addChildElement("AllowDup", prfx1);
        elmDup.addTextNode("Y");
        
        SOAPElement elmPar = elmBlock1.addChildElement("AllowPartialAuth", prfx1);
        elmPar.addTextNode("Y");
        
        SOAPElement elmAdd = elmBlock1.addChildElement("AdditionalTxnFields", prfx1);
        SOAPElement elmAddDesc = elmAdd.addChildElement("Description", prfx1);
        SOAPElement elmAddInv = elmAdd.addChildElement("InvoiceNbr", prfx1);
        SOAPElement elmAddCustID = elmAdd.addChildElement("CustomerID", prfx1);
        
        elmAddDesc.addTextNode("makeup");
        elmAddInv.addTextNode("987456");
        elmAddCustID.addTextNode("999");

        // Header

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "");

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }

    /**
     * Method used to print the SOAP Response
     */
    private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }	

	public static void main(String[] args) {
		run1();
	}
}
