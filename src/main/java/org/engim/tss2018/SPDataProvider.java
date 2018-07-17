package org.engim.tss2018;

import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.engim.tss2018.db.ChiavePrimaria;


public class SPDataProvider<T extends ChiavePrimaria> 
        extends SortableDataProvider<T, String>
{
  private Class<T> classe;
  
  public SPDataProvider(Class<T> classe)
  {
    this.classe = classe;
  }
  
  @Override
  public Iterator<? extends T> 
        iterator(long start, long num)
  {
    EntityManager db = PM.db();
    try
    {
      Query q = 
       db.createNamedQuery(classe.getSimpleName() + 
                           ".findAll");
      q.setFirstResult((int)start);
      q.setMaxResults((int)num);
      List<T> result = q.getResultList();
      return result.iterator();
    }
    finally
    {
      db.close();
    }
  }

  @Override
  public long size()
  {
    EntityManager db = PM.db();
    try
    {
      CriteriaBuilder cb = db.getCriteriaBuilder();
      CriteriaQuery<Long> cq = 
        cb.createQuery(Long.class);
      Root<T> root = cq.from(classe);
      cq.select(cb.count(root));
      return db.createQuery(cq).getSingleResult();
    }
    finally
    {
      db.close();
    }
  }

  @Override
  public IModel<T> model(T object)
  {
    return new SPLDModel<T>(object.getId(), classe);
  }

}
