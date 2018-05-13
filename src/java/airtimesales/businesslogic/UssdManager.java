/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package airtimesales.businesslogic;


import airtimesales.entities.UssdRequest;
import airtimesales.entities.UssdResponse;
import airtimesales.entities.VendRequest;
import airtimesales.entities.utils.CommonLibrary;
import static airtimesales.entities.utils.Configurations.dmdProxyUrl;
import airtimesales.models.retails.AirtimeRequestUpdateRequest;
import airtimesales.models.retails.AirtimeRequestUpdateResponse;
import airtimesales.models.retails.ChangePinResponse;
import airtimesales.models.retails.IsRetailerResponse;
import airtimesales.models.retails.PendingRequestsResponse;
import airtimesales.models.retails.ResultGeneralItem;
import airtimesales.models.retails.Retailer;
import airtimesales.models.retails.VoucherGenerationRequest;
import airtimesales.models.retails.VoucherGenerationResponse;
import airtimesales.models.subscriber.SubAIrtimeVendIniRequest;
import airtimesales.models.subscriber.SubAirtimeVendInitResponse;
import airtimesales.models.subscriber.Subscriber;
import airtimesales.models.subscriber.VoucherRedeemRequest;
import airtimesales.models.subscriber.VoucherRedeemResponse;
import airtimesales.models.validations.BalanceResponse;
import airtimesales.models.validations.LoginRequest;
import airtimesales.models.validations.LoginResponse;
import airtimesales.models.validations.MiniStatement;
import airtimesales.models.validations.VendResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.System.out;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author aimable
 */
public class UssdManager {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
    private UssdResponse ussdResponse;
    private ResourceBundle resBundle;
    private Locale locale;
    private String previousUssdInput;
    
    private ConcurrentHashMap<Object, String> sessions = new ConcurrentHashMap<>();
    private String resumePreviousState;
    
    private String ussdInput;
    private String ussdSessionId;
    private String ussdSessionIdsAsString;
    private String MSISDN;
    private String lang;
    
    private String currentPin;
    private String newPin;
    private Subscriber subscriber;
    private String shortCode="726\\*001";
    private ConcurrentHashMap<String, Object> availableRequest = new ConcurrentHashMap<>();
    
    private PendingRequestsResponse pendingList=null;
    private Retailer retailer=null;
    private ResultGeneralItem selectedPendingRequest;
    private Integer loginTries=0;
    private  VendRequest vRequest=null;
    
    private String clientState2Resume2;
    
    
    
    private Integer amount;
    private LoginResponse loginResponse;
    
    public UssdManager() {
    }
    
    
    public UssdManager(String msisdn) {
        this.MSISDN=msisdn;
        this.ussdResponse=new UssdResponse();
        if(isRetailer(msisdn)){
            this.ussdResponse.setClientState("StartAsRetailer");
        }else{
            this.ussdResponse.setClientState("StartAsSubscriber");
        }
        
        this.ussdResponse.setMsisdn(msisdn);
    }
    
    
    
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * this method is used to handler the ussd request to be sent based on the
     * submitted request
     *
     * @param ussdRequest
     * @return
     */
    public UssdResponse getNextMessage(UssdRequest ussdRequest) {
        String input;
        try{
            // ussdResponse.setFreeFlow("C");
            setUssdSessionId(ussdRequest.getSessionId().toString());
            // ussdResponse.setMsisdn(getMSISDN());
            ussdResponse.setInput(getUssdInput());
            ussdResponse.setSessionId(ussdRequest.getSessionId());
            
            setUssdInput(ussdRequest.getInput());
            setMSISDN(ussdRequest.getMsisdn());
            
            setUssdSessionIdsAsString("");
            
            List<String> x=ussdResponse.getSessionIdsList();
            
            try
            {
                
                List<String> menus;
                /**
                 *
                 */
                String currentClientState= getUssdResponse().getClientState();
                
                boolean doExit=false;
                while(!doExit){
                    if(!currentClientState.equals("0")){
                        this.setResumePreviousState(currentClientState);
                        setPreviousUssdInput(getUssdInput());
                    }
                    
                    switch (currentClientState)
                    {
                        
                        case "StartAsRetailer":
                            String msisdn=ussdRequest.getMsisdn();
                            ussdResponse.setMessageTitle("Airtime");
                            menus=new ArrayList<>();
                            
                            
                            //    ussdResponse.setMessageTitle(ex+"^");
                            
                            menus.add("^1. Vend Airtime");
                            menus.add("^2. Mini Statement ");
                            menus.add("^3. Check balance");
                            menus.add("^4. Change PIN");
                            ussdResponse.setMenus(menus);
                            
                            ussdResponse.setClientState("StartAsRetailerChoice");
                            ussdResponse.setFreeFlow("C");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                            
                        case "loginRequestPin":
                            if(loginTries==0)
                                ussdResponse.setMessageTitle("Enter PIN");
                            else{
                                if(loginTries<3)
                                    ussdResponse.setMessageTitle("Wrong PIN,^ Enter your PIN again");
                                else{
                                    ussdResponse.setMessageTitle("Contact Your Admin");
                                    ussdResponse.setFreeFlow("B");
                                }
                            }
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            
                            ussdResponse.setClientState("loginReceivePin");
                            this.setUssdResponse(ussdResponse);
                            break;
                        case "loginReceivePin":
                            //build login request
                            LoginRequest loginRequest=new LoginRequest();
                            loginRequest.setPassword(this.ussdInput);
                            loginRequest.setUsername(this.getMSISDN());
                            this.loginResponse= this.login(loginRequest);
                            
                            if(!this.loginResponse.getCode().equals("200")){
                                currentClientState="loginRequestPin";
                                loginTries++;
                                continue;
                            }else{
                                currentClientState=this.clientState2Resume2;
                                continue;
                            }
                        case "StartAsSubscriber":
                            
                            String inputs[]=this.getUssdInput().replaceAll(this.shortCode,"shortcode").split("\\*");
                            String retailerCode=null;
                            
                            if(inputs.length<2){
                                this.subscriber=this.getSubscriberByMsisdn(this.MSISDN);
                                ussdResponse.setMessageTitle("Your User Number is: "+subscriber.getId());
                            }
                            else{
                                retailerCode=inputs[1];
                                
                                if(this.isValidInteger(inputs[2])){
                                    amount=Integer.parseInt(inputs[2]);
                                }
                                
                                
                                if(retailerCode==null || amount==null){
                                    ussdResponse.setMessageTitle("Invalid request !");
                                }else{
                                    Retailer retailer=this.getRetailerByCode(retailerCode);
                                    Subscriber subscriber=this.getSubscriberByMsisdn(this.MSISDN);
                                    
                                    SubAirtimeVendInitResponse SubAirtimeVendInitResponse=this.sendSubscriberAirtimeRequest(retailer,subscriber,amount);
                                    
                                    if(SubAirtimeVendInitResponse!=null){
                                        ussdResponse.setMessageTitle("Request of "+amount +" Airtime, sent successfully to Retailer "+retailer.getId());
                                    }else{
                                        ussdResponse.setMessageTitle("Issue happen during the sending of your request");
                                    }
                                }
                            }
                            
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            ussdResponse.setFreeFlow("B");
                            ussdResponse.setClientState("checkBalance");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                            
                            
                        case "StartAsRetailerChoice":
                            input=this.getUssdInput().trim();
                            if(!this.isValidInteger(input)){
                                ussdResponse.setClientState("Start");
                                currentClientState="Start";
                                continue;
                            }
                            
                            switch(input){
                                case "1":
                                    // retailer=this.getRetaileryMsisdn(this.MSISDN);
                                    this.pendingList=this.getPendingRequests(this.MSISDN);
                                    
                                    if(pendingList!=null && pendingList.getBody()!=null && pendingList.getBody().size()>0){
                                        for(Integer i=1;i<=pendingList.getBody().size();i++){
                                            availableRequest.put(""+i, pendingList.getBody().get(i-1));
                                            
                                        }
                                        currentClientState="showVendTypes";
                                    }
                                    else
                                        currentClientState="startDirectVend";
                                    
                                    continue;
                                case "2":
                                    currentClientState="miniStatementStart";
                                    continue;
                                case "3":
                                    currentClientState="checkBalanceStart";
                                    continue;
                                case "4":
                                    currentClientState="changePIN";
                                    continue;
                                default:
                                    ussdResponse.setClientState("Start");
                                    currentClientState="Start";
                                    continue;
                                    
                            }
                        case "checkBalanceStart":
                            ussdResponse.setMessageTitle("Enter PIN");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            
                            this.setResumePreviousState(ussdResponse.getClientState());
                            ussdResponse.setClientState("checkBalance");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                        case "miniStatementStart":
                            ussdResponse.setMessageTitle("Enter PIN");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            
                            this.setResumePreviousState(ussdResponse.getClientState());
                            ussdResponse.setClientState("miniStatement");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                        case "checkBalance":
                            
                            //build login request
                            LoginRequest request=new LoginRequest();
                            request.setPassword(this.ussdInput);
                            request.setUsername(this.getMSISDN());
                            this.loginResponse= this.login(request);
                            
                            if(!this.loginResponse.getCode().equals("200")){
                                ussdResponse.setMessageTitle(loginResponse.getDescription());
                                ussdResponse.setFreeFlow("B");
                                break;
                            }
                            
                            //check balance
                            BalanceResponse balance=this.getRetailerBalance();
                            ussdResponse.setMessageTitle("Balance :"+balance.getBody().getBalance());
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            ussdResponse.setFreeFlow("B");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                            
                        case "miniStatement":
                            
                            //build login request
                            request=new LoginRequest();
                            request.setPassword(this.ussdInput);
                            request.setUsername(this.getMSISDN());
                            this.loginResponse= this.login(request);
                            
                            if(!this.loginResponse.getCode().equals("200")){
                                ussdResponse.setMessageTitle(loginResponse.getDescription());
                                ussdResponse.setFreeFlow("B");
                                break;
                            }
                            
                            //check balance
                            MiniStatement ministatement=this.getRetailerMinistatement();
                            if(ministatement!=null)
                                ussdResponse.setMessageTitle(ministatement.getBody().getMinistatement().replace("\n","^"));
                            else
                                ussdResponse.setMessageTitle("There No Transaction yet [or the ministatemnent can't be retrieved at this time]");
                            
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            ussdResponse.setFreeFlow("B");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                            
                        case "showVendTypes":
                            ussdResponse.setMessageTitle("1. Direct vend"
                                    + "^2. Pending request");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            
                            this.setResumePreviousState(ussdResponse.getClientState());
                            ussdResponse.setClientState("receiveVendType");
                            this.setUssdResponse(ussdResponse);
                            break;
                            
                        case "receiveVendType":
                            switch(this.getUssdInput().trim()){
                                case "1":
                                    currentClientState="startDirectVend";
                                    continue;
                                case "2":
                                    currentClientState="listPendingRequests";
                                    continue;
                                default:
                                    ussdResponse.setClientState("showVendType");
                                    continue;
                                    
                            }
                            
                        case "listPendingRequests":
                            
                            if(pendingList!=null){
                                String userNumber;
                                Integer localAmount;
                                String message="";
                                for(Integer i=1;i<=pendingList.getBody().size();i++){
                                    userNumber=pendingList.getBody().get(i-1).getSubscriberNumber();
                                    localAmount=pendingList.getBody().get(i-1).getAmount();
                                    message+="^"+i+". "+userNumber+" "+localAmount;
                                    
                                }
                                ussdResponse.setMessageTitle(message);
                            }else{
                                ussdResponse.setMessageTitle("A issue with list of requests, strat again");
                                ussdResponse.setFreeFlow("B");
                            }
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            
                            this.setResumePreviousState(ussdResponse.getClientState());
                            ussdResponse.setClientState("receiveSelectedPendingRequest");
                            this.setUssdResponse(ussdResponse);
                            break;
                            
                        case "receiveSelectedPendingRequest":
                            this.selectedPendingRequest=(ResultGeneralItem)this.availableRequest.get(this.ussdInput.trim());
                            
                            if(this.selectedPendingRequest==null){
                                currentClientState="listPendingRequests";
                            }else{
                                currentClientState="ShowAirtimeOrVoucher";
                            }
                            
                            continue;
                            
                        case "ShowAirtimeOrVoucher":
                            ussdResponse.setMessageTitle("1. Airtime "
                                    + "^2. Voucher");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            
                            this.setResumePreviousState(ussdResponse.getClientState());
                            ussdResponse.setClientState("receiveSelectedAirtimeOrVoucher");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                        case "receiveSelectedAirtimeOrVoucher":
                            switch(this.ussdInput){
                                case "1":
                                    vRequest=new VendRequest();
                                    vRequest.setAmount(this.selectedPendingRequest.getAmount());
                                    vRequest.setSubscriberTelephoneNumber(this.selectedPendingRequest.getTelephoneNumber());
                                    vRequest.setRetailerShortCode(this.selectedPendingRequest.getShortCode());
                                    
                                    currentClientState="vendAirtimeForPendingRequest";
                                    continue;
                                case "2":
                                    currentClientState="requestVoucherGeneration";
                                    continue;
                                    
                                default:
                                    break;
                            }
                            break;
                            
                            
                        case "requestVoucherGeneration":
                            if(this.loginResponse==null){
                                this.clientState2Resume2="requestVoucherGeneration";
                                currentClientState="loginRequestPin";
                                continue;
                            }
                            VoucherGenerationRequest voucherGenerationRequest=new VoucherGenerationRequest();
                            voucherGenerationRequest.setSubscriberTelephoneNumber(this.selectedPendingRequest.getTelephoneNumber());
                            voucherGenerationRequest.setAmount(this.selectedPendingRequest.getAmount());
                            VoucherGenerationResponse voucherGenerationResponse= requestVoucherGeneration(voucherGenerationRequest);
                            
                            if(voucherGenerationResponse==null){
                                ussdResponse.setMessageTitle("Somthing went wrong !");
                            }else{
                                AirtimeRequestUpdateRequest airtimeRequestUpdateRequest=new AirtimeRequestUpdateRequest();
                                airtimeRequestUpdateRequest.setId(this.selectedPendingRequest.getId());
                                airtimeRequestUpdateRequest.setStatus("PROCESSED");
                                
                                AirtimeRequestUpdateResponse airtimeRequestUpdateResponse= updateAirtimeRequest(airtimeRequestUpdateRequest);
                                balance=this.getRetailerBalance();
                                if(airtimeRequestUpdateResponse!=null)
                                    ussdResponse.setMessageTitle("Voucher "+voucherGenerationResponse.getBody().getVouchers().get(0)+ " well generated and sent [Request status: "+airtimeRequestUpdateResponse.getBody().getStatus()+"]");
                                
                                else
                                    ussdResponse.setMessageTitle("Voucher "+voucherGenerationResponse.getBody().getVouchers().get(0)+ " well generated and sent [Request status: unknown");
                                
                            }
                            
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            ussdResponse.setFreeFlow("B");
                            this.setResumePreviousState(ussdResponse.getClientState());
                            ussdResponse.setClientState("receiveAmount");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                            
                        case "vendAirtimeForPendingRequest":
                            if(this.loginResponse==null){
                                this.clientState2Resume2="vendAirtimeForPendingRequest";
                                currentClientState="loginRequestPin";
                                continue;
                            }
                            
                            VendResponse vendResponse=this.vendAirtime(this.vRequest);
                            
                            if(vendResponse.getCode().equals("200")){
                                AirtimeRequestUpdateRequest airtimeRequestUpdateRequest=new AirtimeRequestUpdateRequest();
                                airtimeRequestUpdateRequest.setId(this.selectedPendingRequest.getId());
                                airtimeRequestUpdateRequest.setStatus("PROCESSED");
                                
                                AirtimeRequestUpdateResponse airtimeRequestUpdateResponse= updateAirtimeRequest(airtimeRequestUpdateRequest);
                                balance=this.getRetailerBalance();
                                if(airtimeRequestUpdateResponse!=null)
                                    ussdResponse.setMessageTitle("Airtime worth "+this.amount+" sent [Request status: "+airtimeRequestUpdateResponse.getBody().getStatus()+"] "
                                            + "^^ Balance :"+balance.getBody().getBalance());
                                else
                                    ussdResponse.setMessageTitle("Airtime worth "+this.amount+" sent [Request status: unknown] "
                                            + "^^ Balance :"+balance.getBody().getBalance());
                                
                            }else{
                                ussdResponse.setMessageTitle("Airtime Vend failed !");
                            }
                            break;
                            
                        case "startDirectVend":
                            ussdResponse.setMessageTitle("Enter valid User Number");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            
                            this.setResumePreviousState(ussdResponse.getClientState());
                            ussdResponse.setClientState("receiveSubscriberUserNumber");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                        case "receiveSubscriberUserNumber":
                            subscriber=this.getSubscriberByUserNumber(this.getUssdInput());
                            
                            
                            if( this.subscriber==null){
                                currentClientState="startDirectVend";
                                continue;
                            }else{
                                currentClientState="requestAmount";
                                continue;
                            }
                        case "requestAmount":
                            ussdResponse.setMessageTitle("Enter Amount");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            
                            this.setResumePreviousState(ussdResponse.getClientState());
                            ussdResponse.setClientState("receiveAmount");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                            
                        case "receiveAmount":
                            input=this.getUssdInput();
                            if(!this.isValidInteger(input)){
                                currentClientState="requestAmount";
                                continue;
                            }else{
                                this.amount=Integer.parseInt(input);
                                currentClientState="requestAirtimeSaleConfirmation";
                                continue;
                            }
                        case "requestAirtimeSaleConfirmation":
                            ussdResponse.setMessageTitle("Send Airtime for amount "+this.amount + " to customer user number "+ this.subscriber.getId()
                                    + "^^1. Confirm"
                                            + "^2. Cancel");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            ussdResponse.setFreeFlow("C");
                            ussdResponse.setClientState("ReceiveAirtimeSaleConfirmationResponse");
                            this.setUssdResponse(ussdResponse);
                            
                            break;
                        case "ReceiveAirtimeSaleConfirmationResponse":
                            switch(this.ussdInput){
                                case "1":
                                    ussdResponse.setMessageTitle("Enter your PIN ");
                                    ussdResponse.setFreeFlow("C");
                                    ussdResponse.setClientState("receivePin");
                                    break;
                                case "2":
                                    ussdResponse.setMessageTitle("Airtime Vend cancelled !");
                                    ussdResponse.setFreeFlow("B");
                                    break;
                            }
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            this.setUssdResponse(ussdResponse);
                            break;
                            
                        case "changePIN":
                            ussdResponse.setMessageTitle("Enter Current PIN ");
                            ussdResponse.setFreeFlow("C");
                            ussdResponse.setClientState("receiveCurrentPin");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            this.setUssdResponse(ussdResponse);
                            break;
                            
                            
                        case "receiveCurrentPin":
                            this.currentPin=this.ussdInput;
                            
                            //take the opportunity to login as the token is going to be needed
                            request=new LoginRequest();
                            request.setPassword(this.currentPin);
                            request.setUsername(this.getMSISDN());
                            this.loginResponse= this.login(request);
                            
                            if(!this.loginResponse.getCode().equals("200")){
                                ussdResponse.setMessageTitle(loginResponse.getDescription());
                                ussdResponse.setFreeFlow("B");
                                break;
                            }
                            
                            ussdResponse.setMessageTitle("Enter New PIN ");
                            ussdResponse.setFreeFlow("C");
                            ussdResponse.setClientState("receiveNewPin");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            this.setUssdResponse(ussdResponse);
                            break;
                            
                        case "receiveNewPin":
                            this.newPin=this.ussdInput;
                            
                            ChangePinResponse changePinResponse=this.changePin(this.currentPin, this.newPin);
                            
                            if(changePinResponse==null)
                                ussdResponse.setMessageTitle("A problem occured, please talk to OLtranz about this ");
                            else{
                                if(changePinResponse.getResponse().equals("1")){
                                    ussdResponse.setMessageTitle("New PIN successfully set");
                                }else{
                                    ussdResponse.setMessageTitle(changePinResponse.getDescription());
                                }
                            }
                            ussdResponse.setFreeFlow("B");
                            menus=new ArrayList<>();
                            ussdResponse.setMenus(menus);
                            this.setUssdResponse(ussdResponse);
                            break;
                            
                        case "receivePin":
                            
                            //build login request
                            request=new LoginRequest();
                            request.setPassword(this.ussdInput);
                            request.setUsername(this.getMSISDN());
                            this.loginResponse= this.login(request);
                            
                            if(!this.loginResponse.getCode().equals("200")){
                                ussdResponse.setMessageTitle(loginResponse.getDescription());
                                ussdResponse.setFreeFlow("B");
                                break;
                            }
                            
                            //get ratailer by msisdn and the function used is wrong because we shortcode
                            retailer =this.getRetaileryMsisdn(this.MSISDN);
                            
                            if(retailer==null){
                                ussdResponse.setMessageTitle("System could not retrieve your code, contact sys admin");
                            }else
                                if(this.loginResponse!=null && this.loginResponse.getCode().equals("200")){
                                    VendRequest vRequest=new VendRequest();
                                    vRequest.setAmount(this.amount);
                                    vRequest.setSubscriberTelephoneNumber(this.subscriber.getTelephoneNumber());
                                    vRequest.setRetailerShortCode(retailer.getId());
                                    vendResponse=this.vendAirtime(vRequest);
                                    
                                    if(vendResponse.getCode().equals("200")){
                                        balance=this.getRetailerBalance();
                                        ussdResponse.setMessageTitle("Airtime worth "+this.amount+" sent successfully"
                                                + "^^ Balance :"+balance.getBody().getBalance());
                                    }else{
                                        ussdResponse.setMessageTitle("Airtime Vend failed !");
                                    }
                                    
                                }else{
                                    ussdResponse.setMessageTitle("Wrong Pin!");
                                }
                            
                            ussdResponse.setClientState("Start");
                            ussdResponse.setFreeFlow("B");
                            
                            break;
                            
                            
                            
                    }
                    
                    out.print("MSISDN: " + ussdRequest.getMsisdn()+" | ALL SESSIONIDS: "+getUssdSessionIdsAsString()+" | LAST SESSIONID: "+getUssdSessionId()+"| USSDAPPSTATE: "+currentClientState+ " | USSDREQUEST: " + getUssdInput() + " | USSDRESPONSE: "+ussdResponse.getMessageTitle());
                    
                    doExit=true;
                }
                // } end of the client state not equal to FST ( Flow Start)
                // end the request is not new continue to the next menu
                return getUssdResponse();
            } catch (Exception ex)
            {
                input=getUssdInput();
                String msisdn=ussdRequest.getMsisdn();
                ussdResponse.setMessageTitle(getResBundle().getString("systemException"));
                List<String> menus=new ArrayList<>();
                
                
                //    ussdResponse.setMessageTitle(ex+"^");
                menus.add("^1."+getResBundle().getString("return_resume"));
                menus.add("^2."+getResBundle().getString("return_begin"));
                ussdResponse.setMenus(menus);
                
                
                this.setResumePreviousState(ussdResponse.getClientState());
                ussdResponse.setClientState("0");
                this.setUssdResponse(ussdResponse);
                
                String message="ERROR ON RECEPTION OF USSD: MESSAGE: "+ex.getMessage()+" | TRACE :";
                StringWriter errors = new StringWriter();
                ex.printStackTrace(new PrintWriter(errors));
                message+=errors.toString();
                
                out.print("LAST REQUEST INPUT:" +  input + "|MSISDN:" + msisdn + "|APP EXCEPTION TRACE: "+message);
                
                return ussdResponse;
            }
        }catch (Exception ex2){
            out.print(ex2.getMessage());
            
            return null;
        }
    }
    
    private AirtimeRequestUpdateResponse updateAirtimeRequest(AirtimeRequestUpdateRequest request){
        String jsonRequest=CommonLibrary.marshalling(request, AirtimeRequestUpdateRequest.class, "json");
        
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap();
        headers.add("Authorization", "Bearer "+this.loginResponse.getBody().getTokens().getAccess_token());
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/pendings/update", jsonRequest,headers,MediaType.APPLICATION_JSON, "POST");
        
        if(response.getStatus()==200){
            String StrResponseBody=response.readEntity(String.class);
            AirtimeRequestUpdateResponse airtimeRequestUpdateRequestResponse=(AirtimeRequestUpdateResponse)CommonLibrary.unmarshalling(StrResponseBody, AirtimeRequestUpdateResponse.class, "json");
            
            return airtimeRequestUpdateRequestResponse;
        }else{
            return null;
        }
    }
    
    private VoucherGenerationResponse requestVoucherGeneration(VoucherGenerationRequest request){
        String jsonRequest=CommonLibrary.marshalling(request, VoucherGenerationRequest.class, "json");
        
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap();
        headers.add("Authorization", "Bearer "+this.loginResponse.getBody().getTokens().getAccess_token());
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/vouchers", jsonRequest,headers,MediaType.APPLICATION_JSON, "POST");
        
        if(response.getStatus()==200){
            String StrResponseBody=response.readEntity(String.class);
            VoucherGenerationResponse voucherGenerationResponse=(VoucherGenerationResponse)CommonLibrary.unmarshalling(StrResponseBody, VoucherGenerationResponse.class, "json");
            
            return voucherGenerationResponse;
        }else{
            return null;
        }
    }
    
    private Subscriber getSubscriberByMsisdn(String msiddn){
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/subscriber/"+msiddn,"","", "GET");
        
        String StrResponseBody=response.readEntity(String.class);
        Subscriber resp=(Subscriber)CommonLibrary.unmarshalling(StrResponseBody, Subscriber.class, "json");
        
        return resp;
    }
    
    private Retailer getRetaileryMsisdn(String msiddn){
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/subscriber/"+msiddn,"","", "GET");
        
        if(response.getStatus()==200){
            String StrResponseBody=response.readEntity(String.class);
            Retailer retailer=(Retailer)CommonLibrary.unmarshalling(StrResponseBody, Retailer.class, "json");
            
            return retailer;
        }else{
            return null;
        }
    }
    
    private SubAirtimeVendInitResponse sendSubscriberAirtimeRequest(Retailer retailer, Subscriber subscriber,Integer amount){
        
        SubAIrtimeVendIniRequest request=new SubAIrtimeVendIniRequest();
        request.setAmount(amount);
        request.setRetTelephoneNumber(retailer.getTelephoneNumber());
        request.setShortCode(retailer.getId());
        request.setSubscriberNumber(subscriber.getId());
        request.setTelephoneNumber(subscriber.getTelephoneNumber());
        
        String jsonRequest=CommonLibrary.marshalling(request, SubAIrtimeVendIniRequest.class, "json");
        
        
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/pendings", jsonRequest,null,MediaType.APPLICATION_JSON, "POST");
        
        if(response.getStatus()==200){
            String StrResponseBody=response.readEntity(String.class);
            
            SubAirtimeVendInitResponse subAIrtimeVendInitResponse=(SubAirtimeVendInitResponse)CommonLibrary.unmarshalling(StrResponseBody, SubAirtimeVendInitResponse.class, "json");
            
            return subAIrtimeVendInitResponse;
        }
        return null;
    }
    
    
    private PendingRequestsResponse getPendingRequests(String retailerMsisdn){
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/pendings/retailer/telephone/"+retailerMsisdn,"","", "GET");
        if(response.getStatus()==200){
            String StrResponseBody=response.readEntity(String.class);
            PendingRequestsResponse pendingRequestsBody=(PendingRequestsResponse)CommonLibrary.unmarshalling(StrResponseBody, PendingRequestsResponse.class, "json");
            
            return pendingRequestsBody;
        }else{
            return null;
        }
        
        
    }
    
    
    private String voucherRedeem(VoucherRedeemRequest request){
        String jsonRequest=CommonLibrary.marshalling(request, VoucherRedeemRequest.class, "json");
        
        
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/vouchers/redeem", jsonRequest,null,MediaType.APPLICATION_JSON, "POST");
        
        if(response.getStatus()==200){
            String StrResponseBody=response.readEntity(String.class);
            VoucherRedeemResponse voucherRedeemResponse=(VoucherRedeemResponse)CommonLibrary.unmarshalling(StrResponseBody, VoucherRedeemResponse.class, "json");
            
            return voucherRedeemResponse.getDescription();
        }
        return null;
    }
    
    private Retailer getRetailerByCode(String code){
        
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/retailer/"+code,"","", "GET");
        
        if(response.getStatus()==200){
            String StrResponseBody=response.readEntity(String.class);
            Retailer retailer=(Retailer)CommonLibrary.unmarshalling(StrResponseBody, Retailer.class, "json");
            retailer.setId(code);
            
            return retailer;
        }else{
            return null;
        }
        
    }
    
    private Subscriber getSubscriberByUserNumber(String userNumber){
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/subscriber/number/"+userNumber,"","", "GET");
        
        String StrResponseBody=response.readEntity(String.class);
        Subscriber resp=(Subscriber)CommonLibrary.unmarshalling(StrResponseBody, Subscriber.class, "json");
        
        return resp;
    }
    
    private Boolean isRetailer(String msisdn){
        
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms//retailer/"+msisdn+"/check","","", "GET");
        
        String StrResponseBody=response.readEntity(String.class);
        IsRetailerResponse resp=(IsRetailerResponse)CommonLibrary.unmarshalling(StrResponseBody, IsRetailerResponse.class, "json");
        
        return resp.getIsRetailer();
    }
    
    private VendResponse vendAirtime(VendRequest request){
        String jsonRequest=CommonLibrary.marshalling(request, VendRequest.class, "json");
        
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap();
        headers.add("Authorization", "Bearer "+this.loginResponse.getBody().getTokens().getAccess_token());
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/vend", jsonRequest,headers,MediaType.APPLICATION_JSON, "POST");
        
        String StrResponseBody=response.readEntity(String.class);
        VendResponse vendResponse=(VendResponse)CommonLibrary.unmarshalling(StrResponseBody, VendResponse.class, "json");
        
        return vendResponse;
    }
    
    private BalanceResponse getRetailerBalance(){
        
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap();
        headers.add("Authorization", "Bearer "+this.loginResponse.getBody().getTokens().getAccess_token());
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/retailer/balance","",headers,"", "GET");
        
        String StrResponseBody=response.readEntity(String.class);
        BalanceResponse balanceResponse=(BalanceResponse)CommonLibrary.unmarshalling(StrResponseBody, BalanceResponse.class, "json");
        
        return balanceResponse;
    }
    
    private MiniStatement getRetailerMinistatement(){
        
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap();
        headers.add("Authorization", "Bearer "+this.loginResponse.getBody().getTokens().getAccess_token());
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/retailer/ministatement","",headers,"", "GET");
        
        String StrResponseBody=response.readEntity(String.class);
        
        if(response.getStatus()==200){
            MiniStatement miniStatement=(MiniStatement)CommonLibrary.unmarshalling(StrResponseBody, MiniStatement.class, "json");
            return miniStatement;
        }else{
            return null;
        }
        
        
    }
    
    private LoginResponse login(LoginRequest request){
        
        String jsonRequest=CommonLibrary.marshalling(request, LoginRequest.class, "json");
        
        Response response=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/login", jsonRequest,MediaType.APPLICATION_JSON, "POST");
        
        String StrResponseBody=response.readEntity(String.class);
        LoginResponse loginResponse=(LoginResponse)CommonLibrary.unmarshalling(StrResponseBody, LoginResponse.class, "json");
        
        return loginResponse;
    }
    
    
    private ChangePinResponse changePin(String oldPin,String newPin){
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap();
        headers.add("Authorization", "Bearer "+this.loginResponse.getBody().getTokens().getAccess_token());
        Response resp=CommonLibrary.sendRESTRequest(dmdProxyUrl+"/dms/users/change/pin?oldPin="+oldPin+"&newPin="+newPin, "", headers,MediaType.APPLICATION_JSON, "POST");
        
        String respStr=resp.readEntity(String.class);
        
        ChangePinResponse changePinResponse=(ChangePinResponse)CommonLibrary.unmarshalling(respStr, ChangePinResponse.class, "json");
        return changePinResponse;
    }
    
    /**
     * get the current hour in form of Integer
     *
     * @return
     */
    public int getCurrentHour() {
        LocalTime localTime = LocalTime.now();
        return localTime.getHour();
    }
    
    /**
     * get a String representing date in form of yyyy-MM-dd
     *
     * @return
     */
    public String getStringDate() {
        LocalDate localTime = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(localTime);
    }
    
    /**
     * this method is used to get the resources bundler bases on chosen local
     * language
     *
     * @param currentLocale
     * @return
     */
    public ResourceBundle getCurrentLocale(Locale currentLocale) {
        ResourceBundle resB = ResourceBundle.getBundle("airtimesales.resources.message", currentLocale);
        return resB;
    }
    
    /**
     * since the remote interface is using the JSon format in its API this
     * method will be used to generate booking request in that format
     *
     * @param ticket
     * @param notToday
     * @return
     */
    
    
    public UssdResponse getUssdResponse() {
        return ussdResponse;
    }
    
    public void setUssdResponse(UssdResponse ussdResponse) {
        this.ussdResponse = ussdResponse;
    }
    
    public void setResumePreviousState(String resumePreviousState) {
        this.resumePreviousState = resumePreviousState;
    }
    
    public String getResumePreviousState() {
        return this.resumePreviousState;
    }
    
    public boolean isValidInteger(String input){
        try
        {
            int x= Integer.parseInt(input);
            return true;
        } catch (Exception ne)
        {
            
            return false;
        }
    }
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.ussdResponse.getMsisdn());
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final UssdManager other = (UssdManager) obj;
        if (!Objects.equals(this.ussdResponse.getMsisdn(), other.ussdResponse.getMsisdn()))
        {
            return false;
        }
        return true;
    }
    
    
    /**
     * @return the resBundle
     */
    public ResourceBundle getResBundle() {
        return resBundle;
    }
    
    /**
     * @param resBundle the resBundle to set
     */
    public void setResBundle(ResourceBundle resBundle) {
        this.resBundle = resBundle;
    }
    
    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        setResBundle(getCurrentLocale(getLocale()));
        
    }
    
    /**
     * @return the previousUssdInput
     */
    public String getPreviousUssdInput() {
        return previousUssdInput;
    }
    
    /**
     * @param previousUssdInput the previousUssdInput to set
     */
    public void setPreviousUssdInput(String previousUssdInput) {
        this.previousUssdInput = previousUssdInput;
    }
    
    /**
     * @return the sessions
     */
    public ConcurrentHashMap<Object, String> getSessions() {
        return sessions;
    }
    
    /**
     * @param sessions the sessions to set
     */
    public void setSessions(ConcurrentHashMap<Object, String> sessions) {
        this.sessions = sessions;
    }
    
    /**
     * @return the ussdInput
     */
    public String getUssdInput() {
        return ussdInput;
    }
    
    /**
     * @param ussdInput the ussdInput to set
     */
    public void setUssdInput(String ussdInput) {
        this.ussdInput = ussdInput;
    }
    
    /**
     * @return the ussdSessionId
     */
    public String getUssdSessionId() {
        return ussdSessionId;
    }
    
    /**
     * @param ussdSessionId the ussdSessionId to set
     */
    public void setUssdSessionId(String ussdSessionId) {
        this.ussdSessionId = ussdSessionId;
    }
    
    /**
     * @return the ussdSessionIdsAsString
     */
    public String getUssdSessionIdsAsString() {
        return ussdSessionIdsAsString;
    }
    
    /**
     * @param ussdSessionIdsAsString the ussdSessionIdsAsString to set
     */
    public void setUssdSessionIdsAsString(String ussdSessionIdsAsString) {
        this.ussdSessionIdsAsString = ussdSessionIdsAsString;
    }
    
    /**
     * @return the MSISDN
     */
    public String getMSISDN() {
        return MSISDN;
    }
    
    /**
     * @param MSISDN the MSISDN to set
     */
    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }
    
    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }
    
    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }
    
    
}
