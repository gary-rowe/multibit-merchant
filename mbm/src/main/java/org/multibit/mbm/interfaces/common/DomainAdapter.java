package org.multibit.mbm.interfaces.common;

/**
 * <p>Interface to provide the following to resources:</p>
 * <ul>
 * <li>Structure for domain adapters to use when representing use cases</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public interface DomainAdapter<DTO, Domain> {

  public DTO on(Domain domain);

}
