import org.grampus.GAssert;
import org.grampus.core.GCell;
import org.grampus.core.GConstant;
import org.grampus.core.demo.helloword.HelloWordWorkflow;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class HelloWordWorkflowTest {

    @Test
    public void test(){
        HelloWordWorkflow workflow = new HelloWordWorkflow(){
            @Override
            public void buildWorkflow() {
                super.buildWorkflow();

                //replace the sink cell to get the source message and assert it.
                service(TARGET_SERVICE).cell(GConstant.DEFAULT_EVENT,0,new GCell<String>(){
                    @Override
                    public void handle(String payload, Map meta) {
                        GAssert.assertTrue(payload != null);
                    }
                });
            }
        };

        // the test(count) indicates how many asserts were expected to execute,
        // default value is one, and it need to declare the value when here assert more than one.
        workflow.test();
    }
}
