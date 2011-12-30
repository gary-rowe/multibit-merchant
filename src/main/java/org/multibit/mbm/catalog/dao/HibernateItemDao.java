package org.multibit.mbm.catalog.dao;

import org.multibit.mbm.catalog.dto.Item;
import org.multibit.mbm.catalog.dao.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hibernateItemDao")
public class HibernateItemDao implements ItemDao {

  @Autowired
  private HibernateTemplate hibernateTemplate = null;

  @Override
  public Item getItemByReference(String reference) throws ItemNotFoundException {
    List items = hibernateTemplate.find("from Item i where i.reference = ?", reference);
    if (items==null || items.isEmpty()) {
      throw new ItemNotFoundException();
    }
    return (Item) items.get(0);
  }

  @Override
  public Item persist(Item item) {
    if (item.getId() != null) {
      item=hibernateTemplate.merge(item);
    }
    hibernateTemplate.persist(item);
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
