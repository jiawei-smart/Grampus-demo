package org.grampus.core.rest.test;

import org.grampus.core.GCell;
import org.grampus.core.annotation.rest.*;
import org.grampus.log.GLogger;


@GRestGroup(id="rest_test")
public class RestTestCell extends GCell {

    @GRestMethod(path = "/hellword")
    public String test(){
        return "Hello world, welcome grampus rest!";
    }

    @GRestPost(path = "/login")
    public GRestResp testRestPot(@GRestParam(name="username")String username, @GRestParam(name="password")String password){
        GLogger.info("[{}] login successfully with [{}]",username,password);
        return GRestResp.responseResp(username +"login successfully !");
    }

    @GRestPost(path = "/payload")
    public GRestResp testPayload(@GRestParam(name="grade", require = true)Integer grade,
                                 @GRestBody(description = "input what you want to say")String content){
        GLogger.info("[{}] input: [{}]",grade,content);
        return GRestResp.responseResp("success");
    }
}
