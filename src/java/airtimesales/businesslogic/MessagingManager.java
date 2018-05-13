/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package airtimesales.businesslogic;


import airtimesales.entities.utils.CommonLibrary;
import static java.lang.System.out;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author manzi
 */
@Stateless
public class MessagingManager {
    
    public  String sendSMSNotificationSms(String sender,String receiver, String textMessage) {
        try
        {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://10.171.1.51:8080/SmsEngine/SmsHandler");
            Response response = target
                    .queryParam("receiver", getValidMsisdn(receiver))
                    .queryParam("textMessage", textMessage)
                    .queryParam("from", sender).request().buildGet().invoke();
            out.print("SMS NOTIFICATION : To:" + receiver + "|sender:" + sender + "MESSAGE:"+textMessage );
            System.out.println("SMS is sent" + response.readEntity(String.class));
            return response.readEntity(String.class);
        } catch (Exception ex)
        {
            return "error";
        }
    }
    
    public static  String ussdPush(String destMSISDN, String textMessage,Integer SPID) {
        try
        {
            String url="http://10.171.1.51/MTNRwUssdPushAgent/push/"+destMSISDN+"/"+textMessage;
            if(SPID==2484)
                url="http://10.171.1.51/MTNRwUssdPushAgent/push/"+destMSISDN+"/"+textMessage;
            if(SPID==3382)
                url="http://10.171.1.51/TIGORwUssdPushAgent/push/"+destMSISDN+"/"+textMessage;
            Response resp=CommonLibrary.sendRESTRequest(url,"", MediaType.APPLICATION_XHTML_XML, "GET");
            String Body=resp.readEntity(String.class);
            out.print("USSDPUSH: Response : HEADER"+resp +" | BODY: "+Body);
            return Body;
        } catch (Exception ex)
        {
            return "error";
        }
    }
    
    private   String getValidMsisdn(String msisdn) {
        return "250" + msisdn.substring(msisdn.length() - 9);
    }
    
}
