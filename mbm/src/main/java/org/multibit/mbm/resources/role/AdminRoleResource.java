package org.multibit.mbm.resources.role;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import org.multibit.mbm.api.hal.HalMediaType;
import org.multibit.mbm.api.request.AdminDeleteEntityRequest;
import org.multibit.mbm.api.request.role.AdminCreateRoleRequest;
import org.multibit.mbm.api.request.role.AdminUpdateRoleRequest;
import org.multibit.mbm.api.response.hal.role.AdminRoleBridge;
import org.multibit.mbm.api.response.hal.role.AdminRoleCollectionBridge;
import org.multibit.mbm.auth.annotation.RestrictedTo;
import org.multibit.mbm.db.dao.RoleDao;
import org.multibit.mbm.auth.Authority;
import org.multibit.mbm.db.dto.Role;
import org.multibit.mbm.db.dto.RoleBuilder;
import org.multibit.mbm.db.dto.User;
import org.multibit.mbm.resources.BaseResource;
import org.multibit.mbm.resources.ResourceAsserts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>Resource to provide the following to application:</p>
 * <ul>
 * <li>Provision of REST endpoints to manage CRUD operations by an administrator against a collection of {@link org.multibit.mbm.db.dto.User} entities</li>
 * </ul>
 *
 * @since 0.0.1
 */
@Component
@Path("/admin/role")
@Produces({HalMediaType.APPLICATION_HAL_JSON, HalMediaType.APPLICATION_HAL_XML})
public class AdminRoleResource extends BaseResource {

  @Resource(name = "hibernateRoleDao")
  RoleDao roleDao;

  /**
   * Create a new Role from the given mandatory fields
   *
   * @param adminUser A user with administrator rights
   *
   * @return A response containing the minimum details of the created entity
   */
  @POST
  @Timed
  public Response create(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    AdminCreateRoleRequest createRoleRequest) {

    // Build a role from the given request information
    Role role = RoleBuilder.newInstance()
      .withName(createRoleRequest.getName())
      .withDescription(createRoleRequest.getDescription())
      .build();

    // Perform basic verification
    Optional<Role> verificationRole = roleDao.getByName(role.getName());
    ResourceAsserts.assertNotConflicted(verificationRole,"role");

    // Persist the role
    Role persistentRole = roleDao.saveOrUpdate(role);

    // Provide a representation to the client
    AdminRoleBridge bridge = new AdminRoleBridge(uriInfo, Optional.of(adminUser));
    URI location = uriInfo.getAbsolutePathBuilder().path(persistentRole.getId().toString()).build();

    return created(bridge, persistentRole, location);

  }

  /**
   * Provide a paged response of all roles in the system
   *
   * @param adminUser     A user with administrator rights
   * @param rawPageSize   The unvalidated page size
   * @param rawPageNumber The unvalidated page number
   *
   * @return A response containing a paged list of all roles
   */
  @GET
  @Timed
  @CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
  public Response retrieveAllByPage(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @QueryParam("ps") Optional<String> rawPageSize,
    @QueryParam("pn") Optional<String> rawPageNumber) {

    // Validation
    int pageSize = Integer.valueOf(rawPageSize.get());
    int pageNumber = Integer.valueOf(rawPageNumber.get());

    List<Role> roles = roleDao.getAllByPage(pageSize, pageNumber);

    // Provide a representation to the client
    AdminRoleCollectionBridge bridge = new AdminRoleCollectionBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, roles);

  }

  /**
   * Update an existing Role with the populated fields
   *
   * @param adminUser A user with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @PUT
  @Timed
  @Path("/{roleId}")
  public Response update(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @PathParam("roleId") Long roleId,
    AdminUpdateRoleRequest updateRoleRequest) {

    // Retrieve the role
    Optional<Role> role = roleDao.getById(roleId);
    ResourceAsserts.assertPresent(role, "role");

    // Verify and apply any changes to the Role
    Role persistentRole = role.get();
    persistentRole.setName(updateRoleRequest.getName());
    persistentRole.setDescription(updateRoleRequest.getDescription());
    // TODO Re-instate this (needs an update to the request)
//    for (String authorityName : updateRoleRequest.getAuthorities()) {
//      try {
//        Authority authority = Authority.valueOf(authorityName.toUpperCase());
//        persistentRole.getAuthorities().add(authority);
//      } catch (IllegalArgumentException e) {
//        throw new WebApplicationException(Response.Status.BAD_REQUEST);
//      }
//    }

    // Persist the updated role
    persistentRole = roleDao.saveOrUpdate(role.get());

    // Provide a representation to the client
    AdminRoleBridge bridge = new AdminRoleBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, persistentRole);

  }

  /**
   * Delete an existing Role (usually meaning set flag to deleted)
   *
   * @param adminUser A user with administrator rights
   *
   * @return A response containing the full details of the updated entity
   */
  @DELETE
  @Timed
  @Path("/{roleId}")
  public Response delete(
    @RestrictedTo({Authority.ROLE_ADMIN})
    User adminUser,
    @PathParam("roleId") Long roleId,
    AdminDeleteEntityRequest deleteEntityRequest) {

    // Retrieve the role
    Optional<Role> role = roleDao.getById(roleId);
    ResourceAsserts.assertPresent(role,"role");

    // Verify and apply any changes to the Role
    Role persistentRole = role.get();
    persistentRole.setDeleted(true);
    persistentRole.setReasonForDelete(deleteEntityRequest.getReason());

    // Persist the updated role
    persistentRole = roleDao.saveOrUpdate(role.get());

    // Provide a representation to the client
    AdminRoleBridge bridge = new AdminRoleBridge(uriInfo, Optional.of(adminUser));

    return ok(bridge, persistentRole);

  }

  public void setRoleDao(RoleDao roleDao) {
    this.roleDao = roleDao;
  }
}