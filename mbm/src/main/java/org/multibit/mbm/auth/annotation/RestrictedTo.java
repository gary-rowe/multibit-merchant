package org.multibit.mbm.auth.annotation;

import org.multibit.mbm.db.dto.Authority;

import java.lang.annotation.*;

/**
 * <p>Annotation to provide the following to application:</p>
 * <ul>
 * <li>Concise type-safe reference to {@link org.multibit.mbm.db.dto.Authority}</li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 *   @RestrictedTo({CREATE_INVOICES})
 *   public void doSomething()
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