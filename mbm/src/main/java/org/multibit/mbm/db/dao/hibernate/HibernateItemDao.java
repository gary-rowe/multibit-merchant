package org.multibit.mbm.db.dao.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.multibit.mbm.api.response.ItemPagedQueryResponse;
import org.multibit.mbm.db.dao.ItemDao;
import org.multibit.mbm.db.dao.ItemNotFoundException;
import org.multibit.mbm.db.dto.Item;
import org.multibit.mbm.db.dto.ItemField;
import org.multibit.mbm.db.dto.ItemFieldDetail;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository("hibernateItemDao")
public class HibernateItemDao extends BaseHibernateDao implements ItemDao {

  @Override
  public Optional<Item> getById(Long id) throws ItemNotFoundException {
    return getById(Item.class,id);
  }

  @Override
  public Optional<Item> getBySKU(String sku) {
    List items = hibernateTemplate.find("from Item i where i.sku = ?", sku);

    return first(Item.class, items);
  }


  @Override
  public Optional<Item> getByGTIN(String gtin) {
    List items = hibernateTemplate.find("from Item i where i.gtin = ?", gtin);

    return first(Item.class, items);
  }

  @SuppressWarnings("unchecked")
  public List<Item> getAllByPage(final int pageSize, final int pageNumber) {
    return (List<Item>) hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session.createQuery("from Item");
        query.setMaxResults(pageSize);
        query.setFirstResult(pageSize * pageNumber);
        return query.list();
      }
    });
  }

  // TODO Review the ItemPagedQueryResponse requirement
  // Consider a more generic search mechanism
  @SuppressWarnings("unchecked")
  @Override
  @Deprecated
  public List<Item> getPagedItems(final ItemPagedQueryResponse itemPagedQueryResponse) {
    Preconditions.checkNotNull(itemPagedQueryResponse, "itemPagedQueryResponse cannot be null");

    return hibernateTemplate.executeFind(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {

        // Examine the example object to determine the query
        String hql = buildHql(itemPagedQueryResponse);
        
        // Do the work now that the HQL is created
        return (List<Item>) session
          .createQuery(hql)
          .setFirstResult(itemPagedQueryResponse.getFirstResult())
          .setMaxResults(itemPagedQueryResponse.getMaxResults())
          .setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE)
          .list();
      }

      /**
       * @param itemPagedQueryResponse The query containing an example entity
       * @return The HQL required to locate matching entities
       */
      private String buildHql(ItemPagedQueryResponse itemPagedQueryResponse) {
        Preconditions.checkNotNull(itemPagedQueryResponse, "itemPagedQueryResponse cannot be null");

        // The basic starting point
        String hql = "select i from Item i ";
        Item item = itemPagedQueryResponse.getItem();
        
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
