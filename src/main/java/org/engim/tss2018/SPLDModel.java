package org.engim.tss2018;

import javax.persistence.EntityManager;
import org.apache.wicket.model.LoadableDetachableModel;

/** Ai fini didattici questa classe è OPZIONALE,
 * al suo posto posso usare un CompoundPropertyModel.
 * Nel caso reale è meglio usarla per non saturare la
 * RAM.
 * @author corso
 */

public class SPLDModel<TIPO_DEL_JAVABEAN> extends 
        LoadableDetachableModel<TIPO_DEL_JAVABEAN>
{
  private Integer id;
  private Class<TIPO_DEL_JAVABEAN> classe;
  
  
  public SPLDModel(int id, 
                   Class<TIPO_DEL_JAVABEAN> classe)
  {
    this.id = id;
    this.classe = classe;
  }
  
  @Override
  protected TIPO_DEL_JAVABEAN load()
  {
    EntityManager db = PM.db();
    try
    {
      TIPO_DEL_JAVABEAN art = db.find(classe, id);
      return art;
    }
    finally
    {
      db.close();
    }
  }
}
