package org.multibit.mbm.catalog.service;

import org.multibit.mbm.catalog.dao.ItemDao;
import org.multibit.mbm.catalog.dto.Item;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>Service to provide the following to Controllers:</p>
 * <ul>
 * <li>Transactional collection of Item entries</li>
 * </ul>
 *
 * @since 1.0.0
 *         
 */
@Service
@Transactional(readOnly = true)
public class CatalogService {

  @Resource(name = "hibernateItemDao")
  private ItemDao itemDao;

  /**
   * Attempts to locate a Item based on the given reference
   * @param reference The reference number for the Item
   * @return An Item or null if not found
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public Item getItemFromReference(String reference) {
    return itemDao.getItemByReference(reference);
  }

  /**
   * Package local to allow testing
   * @return The Item DAO
   */
  ItemDao getItemDao() {
    return itemDao;
  }

  public void setItemDao(ItemDao itemDao) {
    this.itemDao = itemDao;
  }
}
