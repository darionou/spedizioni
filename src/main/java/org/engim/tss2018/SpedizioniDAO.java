package org.engim.tss2018;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.engim.tss2018.db.CostoMezzoTrasporto;
import org.engim.tss2018.db.MerceSpedizione;
import org.engim.tss2018.db.Spedizione;

public class SpedizioniDAO
{
  public static float pesoTotale(Spedizione s)
  {
    EntityManager db = PM.db();
    try
    {
      Spedizione reloaded = db.find(Spedizione.class, 
        s.getId());
      Collection<MerceSpedizione> merci =
        reloaded.getMerceSpedizioneCollection();
      float tot = 0;
      for (MerceSpedizione ms: merci)
      {
        tot = tot + ms.getQuantita() * 
          ms.getIdMerce().getPeso().floatValue();
      }
      return tot;
    }
    finally
    {
      db.close();
    }
  }

  public static CostoMezzoTrasporto 
            mezzoEconomico(Spedizione s)
  {
    float pesoSpedizione = pesoTotale(s);
    EntityManager db = PM.db();
    try
    {
      TypedQuery<CostoMezzoTrasporto> tutti = 
        db.createNamedQuery(
          "CostoMezzoTrasporto.findAll", 
          CostoMezzoTrasporto.class);
      List<CostoMezzoTrasporto> results = 
        tutti.getResultList();
      CostoMezzoTrasporto min = null;
      for (CostoMezzoTrasporto cmt: results)
      {
        if (cmt.getPesoMassimo().floatValue()
             >= pesoSpedizione)
        {
          if (min == null)
          {
            min = cmt;
          }
          else
          {
            if (cmt.getCosto().floatValue() < 
                min.getCosto().floatValue())
              min = cmt;
          }
        }
      }
      return min;
    }
    finally
    {
      db.close();
    }
  }
}
