package org.multibit.mbm.api.request.user;

/**
 * <p>Request to provide the following to Resources:</p>
 * <ul>
 * <li>Provision of client state to create an initial bare bones User by an administrator</li>
 * </ul>
 * <p>Note that subsequent updates to the User can set more detail into the User as required</p>
 * <p>When an administrator creates a User there is a lot more detail that can be added that is
 * not available to the general public.</p>
 *
 * @since 0.0.1
 *         
 */
public class AdminCreateUserRequest extends CustomerCreateUserRequest {

  // TODO Add admin specific fields here later

}
