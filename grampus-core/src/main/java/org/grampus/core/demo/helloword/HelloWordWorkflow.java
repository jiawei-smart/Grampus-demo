package org.grampus.core.demo.helloword;

import org.grampus.core.GCell;
import org.grampus.core.GWorkflow;
import org.grampus.core.annotation.config.GEnvValue;
import org.grampus.log.GLogger;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HelloWordWorkflow extends GWorkflow {
    public static String HELLO_WORD_EVENT = "HELLO_WORD_EVENT";
    public static String SOURCE_SERVICE = "SourceService";
    public static String TARGET_SERVICE = "TargetService";

    @Override
    public void buildWorkflow() {
        service(SOURCE_SERVICE).cell(new GCell(){

            @GEnvValue("user.name") // inject a config var from env
            String user;

            @Override
            public void start() {
                //in start fuc, trigger a time to build and send hello word message to target service
                getController()
                        .createTimer(()->onEvent(HELLO_WORD_EVENT, "Hi "+ user+", Welcome to Grampus !"))
                        .schedule(5l, TimeUnit.SECONDS,1);
            }
        }).openEvent(HELLO_WORD_EVENT);

        service(TARGET_SERVICE).cell(new GCell(){
            @Override
            public void handle(Object payload, Map meta) {
                GLogger.info("Received Msg: "+payload);
            }
        });

        chain("SourceService->TargetService");
    }

    public static void main(String[] args) {
        new HelloWordWorkflow().start();
    }
}
