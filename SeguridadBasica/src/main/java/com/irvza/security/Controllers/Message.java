package com.irvza.security.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class Message {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/m1")
    public String getMethodName() {
        return "Mensaje 1";
    }
    

    @GetMapping("/m2")
    public String getMethodName1() {
        return "Mensaje 2";
    }

    @GetMapping("/m3")
    public ResponseEntity<?> login() {
        String sessionId="";
        User userObject=null;

        List<Object> sessions= sessionRegistry.getAllPrincipals();

        for (Object session : sessions) {
            if(session instanceof User){
                userObject=(User) session;
            }

            List<SessionInformation> sessionsInformations= sessionRegistry.getAllSessions(sessions, false);

            for (SessionInformation sessionInformation : sessionsInformations) {
                sessionId=sessionInformation.getSessionId();
                
            }
        }

        Map<String, Object> response=new HashMap<>();
        response.put("response", "Hello world");
        response.put("sessiodId", sessionId);
        response.put("sessionUser", userObject);




        return ResponseEntity.status(HttpStatus.OK).body(String.format("Estas logeado" + response));
    }



    
}
