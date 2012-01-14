package org.multibit.mbm.catalog.dao;

import org.multibit.mbm.catalog.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hibernateItemDao")
public class HibernateItemDao implements ItemDao {

  @Autowired
  private HibernateTemplate hibernateTemplate = null;

  @Override
  public Item getById(Long id) throws ItemNotFoundException {
    List items = hibernateTemplate.find("from Item i where i.id = ?", id);
    if (items==null || items.isEmpty()) {
      // Failing to find using a known primary key is exceptional
      throw new ItemNotFoundException();
    }
    return (Item) items.get(0);
  }

  @Override
  public Item getBySKU(String sku) {
    List items = hibernateTemplate.find("from Item i where i.sku = ?", sku);
    if (items==null || items.isEmpty()) {
      // No matching item
      return null;
    }
    return (Item) items.get(0);
  }


  @Override
  public Item getByGTIN(String gtin) {
    List items = hibernateTemplate.find("from Item i where i.gtin = ?", gtin);
    if (items==null || items.isEmpty()) {
      // No matching item
      return null;
    }
    return (Item) items.get(0);
  }


  // TODO Remove support for this
  @Override
  public List<Item> getAllItems() {
    return hibernateTemplate.find("from Item");
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
