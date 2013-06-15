package org.multibit.mbm.interfaces.rest.links;

/**
 * <p>Interface to provide the following to resources:</p>
 * <ul>
 * <li>Provision of a single point of reference for IETF standard link relations</li>
 * </ul>
 * <h3>A note on relative links</h3>
 * <p>Representations rely on relative links to allow clients to navigate around the application without needing to
 * create internal "magic strings". In HAL, the "_links" section is provided for JSON and XML representations.</p>
 * <p>The standard relative link names have been taken from the <a href="http://www.iana.org/assignments/link-relations/link-relations.xml">IETF link relations</a>
 * list which was put forward in <a href="http://tools.ietf.org/html/rfc5988">RFC 5988: Web linking</a>.</p>
 */
public interface StandardLinkRelations {

  /**
   * Link to this resource
   */
  String SELF_REL = "self";
  /**
   * Link to as index resource (usually containing starting links)
   */
  String INDEX_REL = "index";
  /**
   * Link to first resource (pagination)
   */
  String FIRST_REL = "first";
  /**
   * Link to last resource (pagination)
   */
  String LAST_REL = "last";
  /**
   * Link to next resource (pagination)
   */
  String NEXT_REL = "next";
  /**
   * Link to previous resource (pagination)
   */
  String PREVIOUS_REL = "previous";
  /**
   * Link to an item present in this collection (pagination)
   */
  String ITEM_REL = "item";
  /**
   * Link to a search resource (possibly to an <a href="http://www.opensearch.org/Specifications/OpenSearch/1.1">Open Search</a> resource)
   */
  String SEARCH_REL = "search";
  /**
   * Link to a non-form resource supporting editing of this one (usually through a PUT operation)
   */
  String EDIT_REL = "edit";

}
