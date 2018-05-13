/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package airtimesales.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import airtimesales.businesslogic.UssdManager;
import airtimesales.entities.UssdRequest;
import airtimesales.entities.UssdResponse;
import airtimesales.entities.utils.CommonLibrary;
import airtimesales.models.retails.Retailer;
import airtimesales.models.validations.LoginRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author aimable
 */
@WebServlet(name = "UssdHandler", urlPatterns = {"/UssdHandler"})
public class UssdHandler extends HttpServlet {
    
    static final long serialVersionUID = 2111740233967932325L;
    
    //private UssdManager ussdManager;
    
    //ConcurrentHashMap<Object, Object> sessions = new ConcurrentHashMap<>();
    private LoadingCache<String, UssdManager> cache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .recordStats()
            .build(new CacheLoader<String, UssdManager>() {
                @Override
                public UssdManager load(String key) throws Exception {
                    return loadObject(key);
                }
                
            });
    private UssdManager loadObject(String key) {
        UssdManager ussdM = new UssdManager(key);   
        return ussdM;
        
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml");
        try (PrintWriter writer = response.getWriter()) {
            InputStream xmlrequest = request.getInputStream();
            String inputStreamString = new Scanner(xmlrequest, "UTF-8").useDelimiter("\\A").next();
            
            UssdRequest ussdReq = getUssdRequest(inputStreamString);
            UssdManager    ussdManager;
            try{
                if(ussdReq.getNewRequest()==1)
                    this.cache.invalidate(ussdReq.getMsisdn());
                
                ussdManager = this.cache.get(ussdReq.getMsisdn());
                
                out.print(" USSD REQUEST: "+CommonLibrary.marshalling(ussdReq, UssdRequest.class));
                UssdResponse ussdResp = ussdManager.getNextMessage(ussdReq);
                
                if(!ussdResp.getFreeFlow().equals("C"))
                    this.cache.invalidate(ussdReq.getMsisdn());
                else
                    this.cache.put(ussdReq.getMsisdn(), ussdManager);
                
                out.print(" USSD Response: "+CommonLibrary.marshalling(ussdResp, UssdManager.class));
                writer.print(getResponse(ussdResp));
            }catch(Exception ex){
                UssdResponse ussdResponse=new UssdResponse();
                ussdResponse.setMsisdn(ussdReq.getMsisdn());
                ussdResponse.setMessageTitle("A problem occured, ^ Contact system admin");
                
                List<String> menus;
                menus = new ArrayList<>();
                
                ussdResponse.setMenus(menus);
                ussdResponse.setFreeFlow("B");
            }
            
        }
        
    }
    /**
     * this method is used create UssdRequest from xml string
     *
     * @param request
     * @return
     */
    public UssdRequest getUssdRequest(String request) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(UssdRequest.class);
            Unmarshaller unMarsh = ctx.createUnmarshaller();
            UssdRequest ussdRequest = (UssdRequest) unMarsh.unmarshal(new StringReader(request));
            return ussdRequest;
        }
        
        catch (JAXBException ex) {
            out.print("KVCS USSD error:"+ex.getMessage());
            return null;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public String getResponse(UssdResponse ussdResponse) {
        
        try {
            JAXBContext ctx = JAXBContext.newInstance(UssdResponse.class);
            Marshaller marshaller = ctx.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(ussdResponse, writer);
            return writer.toString();
        }
        catch (JAXBException ex) {
            out.print("KVCS USSD error:"+ex.getMessage());
            return null;
        }
    }
    
}
