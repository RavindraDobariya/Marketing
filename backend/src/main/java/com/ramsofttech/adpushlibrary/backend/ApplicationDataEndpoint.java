package com.ramsofttech.adpushlibrary.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "applicationDataApi",
        version = "v1",
        resource = "applicationData",
        namespace = @ApiNamespace(
                ownerDomain = "backend.adpushlibrary.ramsofttech.com",
                ownerName = "backend.adpushlibrary.ramsofttech.com",
                packagePath = ""
        )
)
public class ApplicationDataEndpoint {

    private static final Logger logger = Logger.getLogger(ApplicationDataEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(ApplicationData.class);
    }

    /**
     * Returns the {@link ApplicationData} with the corresponding ID.
     *
     * @param packageName the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code ApplicationData} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "applicationData/{packageName}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ApplicationData get(@Named("packageName") String packageName) throws NotFoundException {
        logger.info("Getting ApplicationData with ID: " + packageName);
        ApplicationData applicationData = ofy().load().type(ApplicationData.class).id(packageName).now();
        if (applicationData == null) {
            throw new NotFoundException("Could not find ApplicationData with ID: " + packageName);
        }
        return applicationData;
    }

    /**
     * Inserts a new {@code ApplicationData}.
     */
    @ApiMethod(
            name = "insert",
            path = "applicationData",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ApplicationData insert(ApplicationData applicationData) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that applicationData.packageName has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(applicationData).now();
        logger.info("Created ApplicationData with ID: " + applicationData.getPackageName());

        return ofy().load().entity(applicationData).now();
    }

    /**
     * Updates an existing {@code ApplicationData}.
     *
     * @param packageName     the ID of the entity to be updated
     * @param applicationData the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code packageName} does not correspond to an existing
     *                           {@code ApplicationData}
     */
    @ApiMethod(
            name = "update",
            path = "applicationData/{packageName}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public ApplicationData update(@Named("packageName") String packageName, ApplicationData applicationData) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(packageName);
        ofy().save().entity(applicationData).now();
        logger.info("Updated ApplicationData: " + applicationData);
        return ofy().load().entity(applicationData).now();
    }

    /**
     * Deletes the specified {@code ApplicationData}.
     *
     * @param packageName the ID of the entity to delete
     * @throws NotFoundException if the {@code packageName} does not correspond to an existing
     *                           {@code ApplicationData}
     */
    @ApiMethod(
            name = "remove",
            path = "applicationData/{packageName}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("packageName") String packageName) throws NotFoundException {
        checkExists(packageName);
        ofy().delete().type(ApplicationData.class).id(packageName).now();
        logger.info("Deleted ApplicationData with ID: " + packageName);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "applicationData",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ApplicationData> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<ApplicationData> query = ofy().load().type(ApplicationData.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<ApplicationData> queryIterator = query.iterator();
        List<ApplicationData> applicationDataList = new ArrayList<ApplicationData>(limit);
        while (queryIterator.hasNext()) {
            applicationDataList.add(queryIterator.next());
        }
        return CollectionResponse.<ApplicationData>builder().setItems(applicationDataList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String packageName) throws NotFoundException {
        try {
            ofy().load().type(ApplicationData.class).id(packageName).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find ApplicationData with ID: " + packageName);
        }
    }
}