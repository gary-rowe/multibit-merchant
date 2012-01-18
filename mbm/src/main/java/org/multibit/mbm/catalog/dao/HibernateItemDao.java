package org.multibit.mbm.catalog.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dto.ItemField;
import org.multibit.mbm.catalog.dto.ItemFieldDetail;
import org.multibit.mbm.web.rest.v1.catalog.ItemPagedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    Assert.notNull(itemPagedQuery, "itemPagedQuery cannot be null");

    return hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {

        // Examine the example object to determine the query
        String hql = buildHql(itemPagedQuery);
        
        // Do the work now that the HQL is created
        return (List<Item>) session
          .createQuery(hql)
          .setFirstResult(itemPagedQuery.getFirstResult())
          .setMaxResults(itemPagedQuery.getMaxResults())
          .list();
      }

      /**
       * @param itemPagedQuery The query containing an example entity
       * @return The HQL required to locate matching entities
       */
      private String buildHql(ItemPagedQuery itemPagedQuery) {
        Assert.notNull(itemPagedQuery, "itemPagedQuery cannot be null");

        // The basic starting point
        String hql = "select i from Item i ";
        Item item = itemPagedQuery.getItem();
        
        if (item != null) {
          // Check for simple inline fields (if applicable)
          
          // Check for complex joined fields (if applicable)
          if (!item.getItemFieldMap().isEmpty()) {
            // Potential for a complex query
            // Require an inner join on the item field map since there is at least one entry using it
            hql += "inner join i.itemFieldMap ifm where ";
            
            // Currently restrict searching against primary field entries (makes code and query simpler)
            boolean first=true;
            for (Map.Entry<ItemField, ItemFieldDetail> entry: item.getItemFieldMap().entrySet()) {
              ItemFieldDetail itemFieldDetail = entry.getValue();
              // Compare against the ItemField ordinal and apply a wildcard using a disjunction (OR) between fields
              if (!first) {
                hql += "or ";
              }
              hql += "(ifm.itemField = "+ itemFieldDetail.getItemField().ordinal();
              hql += " and lower(ifm.primaryDetail.content) like lower('%"+itemFieldDetail.getPrimaryDetail().getContent()+"%')) ";
              first=false;
            }
          }
        }

        return hql;
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
