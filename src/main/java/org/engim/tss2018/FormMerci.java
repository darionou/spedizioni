package org.engim.tss2018;

import java.math.BigDecimal;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.engim.tss2018.db.Merce;

public class FormMerci extends Form<Merce>
{
  private Merce m;
  
  public FormMerci(String id)
  {
    super(id);
    
    TextField txtCodice = new TextField("codice");
    txtCodice.setRequired(true);
    add(txtCodice);
    
    TextField txtDesc = new TextField("descrizione");
    txtDesc.setRequired(true);
    add(txtDesc);
    
    NumberTextField txtPeso = new NumberTextField("peso");
    txtPeso.setStep(0.1);
    txtPeso.setRequired(true);
    txtPeso.add(new RangeValidator<>(
      BigDecimal.valueOf(0.1), 
      BigDecimal.valueOf(100000)));
    add(txtPeso);
  }

  @Override
  protected void onBeforeRender()
  {
    m = new Merce();    
    setDefaultModel(new CompoundPropertyModel<Merce>(m));

    super.onBeforeRender(); 
  }
  

  @Override
  protected void onSubmit()
  {
    GenericDAO.salva(m);
  }
}
