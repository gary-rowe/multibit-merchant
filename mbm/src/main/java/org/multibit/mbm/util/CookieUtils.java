package org.multibit.mbm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 *  <p>Utility to provide the following to application:</p>
 *  <ul>
 *  <li>Provision of web cookie interface</li>
 *  </ul>
 *
 * @since 1.0.0
 *         
 */
public class CookieUtils {

  private static final Logger log = LoggerFactory.getLogger(CookieUtils.class);

  /**
   * Default is to last for 2 years
   */
  private static int DEFAULT_AGE =365*2;

  /**
   * @param request The request
   * @param name    The cookie name
   *
   * @return The value of the matching cookie (or null)
   */
  public static String getCookieValue(HttpServletRequest request, String name) {
    String value = null;
    Cookie cookie = getCookie(request, name);
    if (cookie != null) {
      value = getCookieValue(cookie);
    } else {
      log.debug("No Cookie in the request");
    }

    return value;
  }

  /**
   * @param request The request
   * @param name    The cookie name
   *
   * @return The matching Cookie (or null)
   */
  private static Cookie getCookie(HttpServletRequest request, String name) {
    Cookie cookies[] = request.getCookies();
    if (cookies != null) {
      for (int i  = 0; i < cookies.length; i++) {
        if (cookies[i].getName().equalsIgnoreCase(name)) {
          return cookies[i];
        }
      }
    }
    return null;
  }

  /**
   * @param cookie The cookie
   * @return A clean value (URL decoded and stripped of double quotes)
   */
  private static String getCookieValue(Cookie cookie) {
    String rawCookieValue;
    try {
      rawCookieValue=URLDecoder.decode(cookie.getValue(),"UTF-8");
    } catch (UnsupportedEncodingException e) {
      log.warn("Platform does not support UTF-8");
      rawCookieValue = cookie.getValue();
    }
    
    return unquote(rawCookieValue);
  }

  /**
   * IE 1-9 only supports cookie version 0, everyone else version 1
   * Version 0 has no quotes and uses URL encoding
   * Version 1 has quotes and does not use URL encoding
   * 
   * See http://stackoverflow.com/a/7233959/396747
   * @param rawCookieValue The raw cookie value
   *
   * @return cookie value without double quote
   */
  private static String unquote(String rawCookieValue) {
    // Check for version 0 (quoted and no URL encoding typically from IE)
    if ((rawCookieValue != null)
      && rawCookieValue.startsWith("\"")
      && rawCookieValue.endsWith("\"")) {
      int last = rawCookieValue.length() - 1;
      rawCookieValue = rawCookieValue.substring(1, last);
    }
    return rawCookieValue;
  }
  

  /**
   * Provides a fluent interface to build a Cookie
   */
  public static class CookieBuilder {
    private String name;
    private String value;
    private int maxAge=DEFAULT_AGE;
    private String path;
    private String domain;
    private boolean isBuilt = false;

    private CookieBuilder() {
    }
    
    public static CookieBuilder getInstance() {
      return new CookieBuilder();
    }

    public CookieBuilder setName(String name) {
      validateState();
      this.name = name;
      return this;
    }
    
    public CookieBuilder setValue(String value) {
      validateState();
      this.value = value;
      return this;
    }

    public CookieBuilder setMaxAge(int maxAge) {
      validateState();
      this.maxAge = maxAge;
      return this;
    }

    public CookieBuilder setPath(String path) {
      validateState();
      this.path = path;
      return this;
    }

    public CookieBuilder setDomain(String domain) {
      validateState();
      this.domain = domain;
      return this;
    }
    
    public Cookie build() {
      validateState();
      if (name==null) {
        throw new IllegalStateException("Name is a mandatory field");
      }
      if (value==null) {
        throw new IllegalStateException("Value is a mandatory field");
      }
      Cookie cookie = null;

      cookie = new Cookie(name, value);
      cookie.setMaxAge(maxAge);

      if ((path != null) && (path.length() > 0)) {
        cookie.setPath(path);
      } else {
        cookie.setPath("/");
      }

      if ((domain != null) && (domain.length() > 0)) {
        cookie.setDomain(domain);
      }

      return cookie;
        
    }

    private void validateState() {
      if (isBuilt) {
        throw new IllegalStateException("Cookie has been built");
      }
    }
  }

}
