package org.engim.tss2018;

import java.util.LinkedList;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.engim.tss2018.db.CostoMezzoTrasporto;

public class PaginaCMT extends PaginaBase
{
  public PaginaCMT()
  {
    List<IColumn<CostoMezzoTrasporto, String>> colonne =
      new LinkedList<>();
    
    PropertyColumn<CostoMezzoTrasporto, String> id =
      new PropertyColumn<>(Model.of("ID"), 
        "id");

    PropertyColumn<CostoMezzoTrasporto, String> nome =
      new PropertyColumn<>(Model.of("Nome"), 
        "nomeMezzo");

    PropertyColumn<CostoMezzoTrasporto, String> pesom =
      new PropertyColumn<>(Model.of("Peso Max"), 
        "pesoMassimo");
    
    PropertyColumn<CostoMezzoTrasporto, String> costo =
      new PropertyColumn<>(Model.of("€"), 
        "costo");
    
    colonne.add(id);
    colonne.add(nome);
    colonne.add(pesom);
    colonne.add(costo);
    
    SPDataProvider<CostoMezzoTrasporto> dataprov =
      new SPDataProvider<>(CostoMezzoTrasporto.class);
        
    DefaultDataTable table =
      new DefaultDataTable("cmt", 
        colonne, dataprov, 10); // 10 righe x pag
    
    add(table);
  }
  
}
