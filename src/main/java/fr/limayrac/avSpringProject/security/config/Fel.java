package fr.limayrac.avSpringProject.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.RequestContext;

public class Fel implements FlowExecutionListener {

    static Logger logger = LoggerFactory.getLogger(Fel.class);

    @Override
    public void requestSubmitted(RequestContext context){
        FlowExecutionListener.super.requestSubmitted(context);
        logger.debug("requestSubmitted");
    }
}
