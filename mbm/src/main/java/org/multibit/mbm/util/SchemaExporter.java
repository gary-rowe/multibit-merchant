package org.multibit.mbm.util;

import com.google.common.collect.Lists;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SchemaExporter {
  
  private static final Logger log = LoggerFactory.getLogger(SchemaExporter.class);
  
  private AnnotationConfiguration cfg;

  public SchemaExporter(List<String> packageNames) throws ClassNotFoundException {
    cfg = new AnnotationConfiguration();
    cfg.setProperty("hibernate.hbm2ddl.auto", "create");

    for (String packageName : packageNames) {
      for (Class<Object> clazz : getClasses(packageName)) {
        cfg.addAnnotatedClass(clazz);
      }
    }

  }

  /**
   * Method that actually creates the file.
   *
   * @param revision The revision number of the database (e.g. 0, 1, 2 etc)
   * @throws java.io.IOException If something goes wrong
   */
  private void execute(int revision) throws IOException {
    
    log.info("Starting schema export process");
    
    cfg.setProperty("hibernate.dialect", H2Dialect.class.getCanonicalName());

    File patchSchema = new File("src/main/resources/sql/" + revision + "/patch-schema.sql");
    if (!patchSchema.exists()) {
      patchSchema.getParentFile().mkdirs();
      patchSchema.createNewFile();
    }
    
    SchemaExport export = new SchemaExport(cfg);
    export.setDelimiter(";");
    export.setOutputFile(patchSchema.getAbsolutePath());
    // Generates alter table drops which should be removed at version 0
    export.execute(true, false, false, false);

    log.info("Schema export complete. Results in '{}'",patchSchema.getAbsolutePath());
  }

  /**
   * @param args Command line args (ignored)
   * @throws Exception If something goes wrong
   */
  public static void main(String[] args) throws Exception {
    // The target revision based on the current domain model
    int revision = 0;

    List<String> packages = Lists.newArrayList(
      "org.multibit.mbm.cart.dto",
      "org.multibit.mbm.catalog.dto",
      "org.multibit.mbm.customer.dto",
      "org.multibit.mbm.i18n.dto",
      "org.multibit.mbm.security.dto",
      "");

    SchemaExporter schemaExporter = new SchemaExporter(packages);
    schemaExporter.execute(revision);
  }

  /**
   * Utility method used to fetch Class list based on a package name.
   *
   * @param packageName The package name containing the annotated classes
   * @return A list of classes within the given package
   * @throws ClassNotFoundException If something goes wrong
   */
  private List<Class> getClasses(String packageName) throws ClassNotFoundException {
    List<Class> classes = new ArrayList<Class>();
    File directory = null;
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null) {
        throw new ClassNotFoundException("No class loader");
      }
      String path = packageName.replace('.', '/');
      URL resource = classLoader.getResource(path);
      if (resource == null) {
        throw new ClassNotFoundException("No resource for " + path);
      }
      directory = new File(resource.getFile());
    } catch (NullPointerException x) {
      throw new ClassNotFoundException(packageName + " (" + directory
        + ") does not appear to be a valid package");
    }
    if (directory.exists()) {
      String[] files = directory.list();
      for (int i = 0; i < files.length; i++) {
        if (files[i].endsWith(".class")) {
          // removes the .class extension
          classes.add(Class.forName(packageName + '.'
            + files[i].substring(0, files[i].length() - 6)));
        }
      }
    } else {
      throw new ClassNotFoundException(packageName
        + " is not a valid package");
    }

    return classes;
  }
}
