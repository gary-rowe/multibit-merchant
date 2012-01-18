package org.multibit.mbm.catalog.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.web.rest.v1.catalog.ItemPagedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository("hibernateItemDao")
public class HibernateItemDao implements ItemDao {

  @Autowired
  private HibernateTemplate hibernateTemplate = null;

  @Override
  public Item getById(Long id) throws ItemNotFoundException {
    List items = hibernateTemplate.find("from Item i where i.id = ?", id);
    if (items == null || items.isEmpty()) {
      // Failing to find using a known primary key is exceptional
      throw new ItemNotFoundException();
    }
    return (Item) items.get(0);
  }

  @Override
  public Item getBySKU(String sku) {
    List items = hibernateTemplate.find("from Item i where i.sku = ?", sku);
    if (items == null || items.isEmpty()) {
      // No matching item
      return null;
    }
    return (Item) items.get(0);
  }


  @Override
  public Item getByGTIN(String gtin) {
    List items = hibernateTemplate.find("from Item i where i.gtin = ?", gtin);
    if (items == null || items.isEmpty()) {
      // No matching item
      return null;
    }
    return (Item) items.get(0);
  }


  @SuppressWarnings("unchecked")
  @Override
  public List<Item> getPagedItems(final ItemPagedQuery itemPagedQuery) {

    return hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {

        // TODO Consider a HQL query builder for Item
        // All this string manipulation is just wrong

        String hql = "select i from Item i ";
        if (itemPagedQuery.getTitle() != null || itemPagedQuery.getSummary() != null) {
          // Inner join on the item field map
          hql += "inner join i.itemFieldMap ifm ";
        }

        if (itemPagedQuery.getTitle() != null ) {
          // Compare against the TITLE field
          hql += "where ifm.itemField = "+ItemField.TITLE.ordinal()+" and lower(ifm.primaryDetail.content) like lower('%"+itemPagedQuery.getTitle()+"%') ";
        }

        // Do the work now that the HQL is created
        return (List<Item>) session
          .createQuery(hql)
          .setFirstResult(itemPagedQuery.getFirstResult())
          .setMaxResults(itemPagedQuery.getMaxResults())
          .list();
      }
    });

  }

  @Override
  public Item saveOrUpdate(Item item) {
    hibernateTemplate.saveOrUpdate(item);
    return item;
  }

  /**
   * Force an immediate in-transaction flush (normally only used in test code)
   */
  public void flush() {
    hibernateTemplate.flush();
  }


  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }
}
