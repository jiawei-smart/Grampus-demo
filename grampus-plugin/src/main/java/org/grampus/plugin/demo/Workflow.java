package org.grampus.plugin.demo;

import org.grampus.core.GCell;
import org.grampus.core.GWorkflow;
import org.grampus.core.annotation.plugin.GPluginAutowired;
import org.grampus.core.plugin.GPromise;
import org.grampus.log.GLogger;

import java.util.Random;

public class Workflow extends GWorkflow {
    @Override
    public void buildWorkflow() {
        service("TEST_service").cell(new GCell(){

            /**
             * inject the plugin api to call the plugin service
             * */
           @GPluginAutowired
           MessageHandler messageHandler;

           Random random = new Random();
            @Override
            public void start() {
                /*
                 * call the plugin service by the api
                 * */
                messageHandler.onAdmin("start");
                int count = 0;
                while (true){
                    try {
                        Thread.sleep(random.nextInt(5000));
                        count++;
                        /*
                         * call the plugin service by the api with a promise to receive the result by promise
                         * */
                        messageHandler.onMessage(count,"msg "+count,
                                GPromise.newInstance().onSuccess((result)->GLogger.info("acked [{}]",result)));
                    } catch (InterruptedException e) {
                        GLogger.error("Plugin task simulator interrupt, with {}",e);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        GWorkflow workflow = new Workflow();
        workflow.start();
    }

}
