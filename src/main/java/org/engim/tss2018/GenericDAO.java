package org.engim.tss2018;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.engim.tss2018.db.ChiavePrimaria;

public class GenericDAO
{
  public static void salva(ChiavePrimaria c)
  {
    EntityManager em = PM.db();
    EntityTransaction et = em.getTransaction();
    try
    {
      et.begin();
      if (c.getId() == null)
        em.persist(c); // INSERT
      else
      {
        c = em.find(c.getClass(), c.getId());
        em.merge(c); // UPDATE
      }
      et.commit();
    }
    finally
    {
      if (et.isActive()) et.rollback();
      em.close();
    }
  }

  public static void elimina(ChiavePrimaria p)
  {
    EntityManager em = PM.db();
    EntityTransaction et = em.getTransaction();
    try
    {
      p = em.find(p.getClass(), p.getId());
      et.begin();
      em.remove(p);
      et.commit();
    }
    finally
    {
      if (et.isActive()) et.rollback();
      em.close();
    }
  }
}
