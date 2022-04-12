package it.ranauro.beesafe.backend.api;

import it.ranauro.beesafe.backend.model.Report;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class DefaultApiHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultApiHandler.class);

    private final DefaultApi api;

    public DefaultApiHandler(DefaultApi api) {
        this.api = api;
    }

    @Deprecated
    public DefaultApiHandler() {
        this(new DefaultApiImpl());
    }

    public void mount(RouterBuilder builder) {
        builder.operation("addReport").handler(this::addReport);
        builder.operation("filterReportsByGravity").handler(this::filterReportsByGravity);
        builder.operation("filterReportsByUrgency").handler(this::filterReportsByUrgency);
        builder.operation("findReportByKindOfProblem").handler(this::findReportByKindOfProblem);
        builder.operation("findReports").handler(this::findReports);
    }

    private void addReport(RoutingContext routingContext) {
        logger.info("addReport()");

        // Param extraction
        RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String uid = requestParameters.headerParameter("uid") != null ? requestParameters.headerParameter("uid").getString() : null;
        String digest = requestParameters.headerParameter("digest") != null ? requestParameters.headerParameter("digest").getString() : null;
        RequestParameter body = requestParameters.body();
        Report report = body != null ? DatabindCodec.mapper().convertValue(body.get(), new TypeReference<Report>(){}) : null;

        logger.debug("Parameter uid is {}", uid);
        logger.debug("Parameter digest is {}", digest);
        logger.debug("Parameter report is {}", report);

        api.addReport(uid, digest, report)
            .onSuccess(apiResponse -> {
                routingContext.response().setStatusCode(apiResponse.getStatusCode());
                if (apiResponse.hasData()) {
                    routingContext.json(apiResponse.getData());
                } else {
                    routingContext.response().end();
                }
            })
            .onFailure(routingContext::fail);
    }

    private void filterReportsByGravity(RoutingContext routingContext) {
        logger.info("filterReportsByGravity()");

        // Param extraction
        RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String digest = requestParameters.headerParameter("digest") != null ? requestParameters.headerParameter("digest").getString() : null;
        String uid = requestParameters.headerParameter("uid") != null ? requestParameters.headerParameter("uid").getString() : null;

        logger.debug("Parameter digest is {}", digest);
        logger.debug("Parameter uid is {}", uid);

        api.filterReportsByGravity(digest, uid)
            .onSuccess(apiResponse -> {
                routingContext.response().setStatusCode(apiResponse.getStatusCode());
                if (apiResponse.hasData()) {
                    routingContext.json(apiResponse.getData());
                } else {
                    routingContext.response().end();
                }
            })
            .onFailure(routingContext::fail);
    }

    private void filterReportsByUrgency(RoutingContext routingContext) {
        logger.info("filterReportsByUrgency()");

        // Param extraction
        RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String digest = requestParameters.headerParameter("digest") != null ? requestParameters.headerParameter("digest").getString() : null;
        String uid = requestParameters.headerParameter("uid") != null ? requestParameters.headerParameter("uid").getString() : null;

        logger.debug("Parameter digest is {}", digest);
        logger.debug("Parameter uid is {}", uid);

        api.filterReportsByUrgency(digest, uid)
            .onSuccess(apiResponse -> {
                routingContext.response().setStatusCode(apiResponse.getStatusCode());
                if (apiResponse.hasData()) {
                    routingContext.json(apiResponse.getData());
                } else {
                    routingContext.response().end();
                }
            })
            .onFailure(routingContext::fail);
    }

    private void findReportByKindOfProblem(RoutingContext routingContext) {
        logger.info("findReportByKindOfProblem()");

        // Param extraction
        RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String digest = requestParameters.headerParameter("digest") != null ? requestParameters.headerParameter("digest").getString() : null;
        String uid = requestParameters.headerParameter("uid") != null ? requestParameters.headerParameter("uid").getString() : null;
        String kindOfProblem = requestParameters.pathParameter("kindOfProblem") != null ? requestParameters.pathParameter("kindOfProblem").getString() : null;

        logger.debug("Parameter digest is {}", digest);
        logger.debug("Parameter uid is {}", uid);
        logger.debug("Parameter kindOfProblem is {}", kindOfProblem);

        api.findReportByKindOfProblem(digest, uid, kindOfProblem)
            .onSuccess(apiResponse -> {
                routingContext.response().setStatusCode(apiResponse.getStatusCode());
                if (apiResponse.hasData()) {
                    routingContext.json(apiResponse.getData());
                } else {
                    routingContext.response().end();
                }
            })
            .onFailure(routingContext::fail);
    }

    private void findReports(RoutingContext routingContext) {
        logger.info("findReports()");

        // Param extraction
        RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String uid = requestParameters.headerParameter("uid") != null ? requestParameters.headerParameter("uid").getString() : null;
        String digest = requestParameters.headerParameter("digest") != null ? requestParameters.headerParameter("digest").getString() : null;

        logger.debug("Parameter uid is {}", uid);
        logger.debug("Parameter digest is {}", digest);

        api.findReports(uid, digest)
            .onSuccess(apiResponse -> {
                routingContext.response().setStatusCode(apiResponse.getStatusCode());
                if (apiResponse.hasData()) {
                    routingContext.json(apiResponse.getData());
                } else {
                    routingContext.response().end();
                }
            })
            .onFailure(routingContext::fail);
    }

}
