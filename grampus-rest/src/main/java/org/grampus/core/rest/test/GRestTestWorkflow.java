package org.grampus.core.rest.test;

import org.grampus.core.GWorkflow;

public class GRestTestWorkflow {
    public static void main(String[] args) {
        GWorkflow workflow = new GWorkflow(){
            @Override
            public void buildWorkflow() {
                service("REST_TEST_SERVICE")
                        .cell(new RestTestCell());
            }
        };
        workflow.start();
    }
}
