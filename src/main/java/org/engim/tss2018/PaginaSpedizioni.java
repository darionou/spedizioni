package org.engim.tss2018;

import java.util.LinkedList;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.engim.tss2018.db.CostoMezzoTrasporto;
import org.engim.tss2018.db.Spedizione;

public class PaginaSpedizioni extends PaginaBase
{
  public PaginaSpedizioni()
  {
    List<IColumn<Spedizione, String>> colonne =
      new LinkedList<>();
    
    PropertyColumn<Spedizione, String> id =
      new PropertyColumn<>(Model.of("ID"), 
        "id");

    PropertyColumn<Spedizione, String> numero =
      new PropertyColumn<>(Model.of("NUM."), 
        "numero");

    PropertyColumn<Spedizione, String> data =
      new PropertyColumn<>(Model.of("DATA"), 
        "data");
    
    AbstractColumn<Spedizione, String> pesotot =
      new AbstractColumn<Spedizione, String>
                     (Model.of("Peso tot"))
    {
      @Override
      public void populateItem(
          Item<ICellPopulator<Spedizione>> item, 
          String wicketid, 
          IModel<Spedizione> rowModel)
      {
        String s_pesotot = 
          String.valueOf(
          SpedizioniDAO.pesoTotale(
            rowModel.getObject()));
        Label l_pesotot = new Label(wicketid,
                                    s_pesotot);
        item.add(l_pesotot);
      }
    };
    
    AbstractColumn<Spedizione, String> mdt =
      new AbstractColumn<Spedizione, String>
                     (Model.of("Mezzo"))
    {
      @Override
      public void populateItem(
          Item<ICellPopulator<Spedizione>> item, 
          String wicketid, 
          IModel<Spedizione> rowModel)
      {
        CostoMezzoTrasporto cmt = 
          SpedizioniDAO.mezzoEconomico(
            rowModel.getObject());
        String mezzo = "€" + 
          cmt.getCosto().floatValue() +
          " (" + cmt.getNomeMezzo() + ")";
        Label l_mezzo = new Label(wicketid,
                                    mezzo);
        item.add(l_mezzo);
      }
    };
    colonne.add(id);
    colonne.add(numero);
    colonne.add(data);
    colonne.add(pesotot);
    colonne.add(mdt);
    
    SPDataProvider<Spedizione> dataprov =
      new SPDataProvider<>(Spedizione.class);
        
    DefaultDataTable table =
      new DefaultDataTable("spedizioni", 
        colonne, dataprov, 10); // 10 righe x pag
    
    add(table);
  }
  
}
