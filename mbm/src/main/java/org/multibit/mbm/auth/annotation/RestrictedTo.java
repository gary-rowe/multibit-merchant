package org.multibit.mbm.auth.annotation;

import org.multibit.mbm.db.dto.Authority;

import java.lang.annotation.*;

/**
 * <p>Annotation to provide the following to application:</p>
 * <ul>
 * <li>Concise type-safe reference to {@link org.multibit.mbm.db.dto.Authority}</li>
 * <li>Binds to parameter to assist injection of User</li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 *   public void doSomething(
 *     @RestrictedTo({CREATE_INVOICES})
 *     User user
 *   )
 * </pre>
 *
 * @since 0.0.1
 *         
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RestrictedTo {
  Authority[] value() default Authority.ROLE_ADMIN;
}