package org.engim.tss2018;

import javax.persistence.PersistenceException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.engim.tss2018.db.ChiavePrimaria;

public class AzioniPanel extends Panel
{
  public AzioniPanel(String id, final ChiavePrimaria p)
  {
    super(id);
    
    add(new AjaxLink("elimina")
    {
      @Override
      public void onClick(AjaxRequestTarget art)
      {
        try
        {
          GenericDAO.elimina(p);
        }
        catch(PersistenceException pe)
        {
          getPage().error(
            "Ci sono relazioni, non puoi eliminare");
        }
        throw new RestartResponseException(getPage());
      }
      
    });
  }
  
}
