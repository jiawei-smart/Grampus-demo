package org.grampus.core.demo.helloword;

import org.grampus.core.GCell;
import org.grampus.core.GWorkflow;
import org.grampus.log.GLogger;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Workflow extends GWorkflow {
    public static String HELLO_WORD_EVENT = "HELLO_WORD_EVENT";
    public static String SOURCE_SERVICE = "SourceService";
    public static String TARGET_SERVICE = "TargetService";
    @Override
    public void buildWorkflow() {
        service(SOURCE_SERVICE).cell(new GCell(){
            @Override
            public void start() {
                getController()
                        .createTimer(()->onEvent(HELLO_WORD_EVENT, "Hello world, Grampus !"))
                        .schedule(5l, TimeUnit.SECONDS);
            }
        }).openEvent(HELLO_WORD_EVENT);

        service(TARGET_SERVICE).cell(new GCell(){
            @Override
            public void handle(Object payload, Map meta) {
                GLogger.info("Received: "+payload);
            }
        });

        chain("SourceService->TargetService");
    }

    public static void main(String[] args) {
        new Workflow().start();
    }
}
