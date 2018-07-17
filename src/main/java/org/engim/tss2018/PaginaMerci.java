package org.engim.tss2018;

import java.util.LinkedList;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.engim.tss2018.db.Merce;

public class PaginaMerci extends PaginaBase
{
  public PaginaMerci()
  {
    List<IColumn<Merce, String>> colonne =
      new LinkedList<>();
    
    PropertyColumn<Merce, String> id =
      new PropertyColumn<>(Model.of("ID"), 
        "id");

    PropertyColumn<Merce, String> codice =
      new PropertyColumn<>(Model.of("COD."), 
        "codice");

    PropertyColumn<Merce, String> desc =
      new PropertyColumn<>(Model.of("DESC."), 
        "descrizione");
    
    PropertyColumn<Merce, String> peso =
      new PropertyColumn<>(Model.of("PESO"), 
        "peso");

    AbstractColumn<Merce, String> azioni =
      new AbstractColumn<Merce, String>(
        Model.of("Azioni"))
      {
        @Override
        public void populateItem(
          Item<ICellPopulator<Merce>> item, 
          String wicketid, IModel<Merce> imodel)
        {
          item.add(new AzioniPanel(wicketid, 
            imodel.getObject()));          
        }
      };
      
    
    colonne.add(id);
    colonne.add(codice);
    colonne.add(desc);
    colonne.add(peso);
    colonne.add(azioni);
    
    SPDataProvider<Merce> dataprov =
      new SPDataProvider<>(Merce.class);
        
    DefaultDataTable table =
      new DefaultDataTable("merci", 
        colonne, dataprov, 10); // 10 righe x pag
    
    add(table);
    
    add(new FormMerci("insmodmerce"));
    add(new FeedbackPanel("feedback"));
  }
  
}
