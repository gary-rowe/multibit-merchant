package org.multibit.mbm.interfaces.rest.auth.annotation;

import java.lang.annotation.*;

/**
 * <p>Annotation to provide the following to application:</p>
 * <ul>
 * <li>Concise type-safe reference to {@link org.multibit.mbm.interfaces.rest.auth.Authority}</li>
 * <li>Binds to parameter to assist injection of User</li>
 * </ul>
 * <p>Example:</p>
 * {@code
 * public void doSomething(
 * &#064;RememberMe )
 * T rememberedUser
 * )
 * }
 * <p>Will inject a <code>T</code> if the remember me cookie points to a valid API key.</p>
 *
 * @since 0.0.1
 *         
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RememberMe {
}