package it.ranauro.backend.api;

import it.ranauro.backend.model.Error;
import it.ranauro.backend.model.NewPet;
import it.ranauro.backend.model.Pet;

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
        builder.operation("addPet").handler(this::addPet);
        builder.operation("deletePet").handler(this::deletePet);
        builder.operation("findPetById").handler(this::findPetById);
        builder.operation("findPets").handler(this::findPets);
    }

    private void addPet(RoutingContext routingContext) {
        logger.info("addPet()");

        // Param extraction
        RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        RequestParameter body = requestParameters.body();
        Pet newPet = body != null ? DatabindCodec.mapper().convertValue(body.get(), new TypeReference<Pet>(){}) : null;

        logger.debug("Parameter newPet is {}", newPet);

        api.addPet(newPet)
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

    private void deletePet(RoutingContext routingContext) {
        logger.info("deletePet()");

        // Param extraction
        RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        Long id = requestParameters.pathParameter("id") != null ? requestParameters.pathParameter("id").getLong() : null;

        logger.debug("Parameter id is {}", id);

        api.deletePet(id)
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

    private void findPetById(RoutingContext routingContext) {
        logger.info("findPetById()");

        // Param extraction
        RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        Long id = requestParameters.pathParameter("id") != null ? requestParameters.pathParameter("id").getLong() : null;

        logger.debug("Parameter id is {}", id);

        api.findPetById(id)
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

    private void findPets(RoutingContext routingContext) {
        logger.info("findPets()");

        // Param extraction
        RequestParameters requestParameters = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        List<String> tags = requestParameters.queryParameter("tags") != null ? DatabindCodec.mapper().convertValue(requestParameters.queryParameter("tags").get(), new TypeReference<List<String>>(){}) : null;
        Integer limit = requestParameters.queryParameter("limit") != null ? requestParameters.queryParameter("limit").getInteger() : null;

        logger.debug("Parameter tags is {}", tags);
        logger.debug("Parameter limit is {}", limit);

        api.findPets(tags, limit)
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
