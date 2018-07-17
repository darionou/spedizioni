/*
 * Copyright 2018 svilupposw.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.engim.tss2018.db;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author svilupposw
 */
@Entity
@Table(name = "costo_mezzo_trasporto")
@XmlRootElement
@NamedQueries(
{
  @NamedQuery(name = "CostoMezzoTrasporto.findAll", query = "SELECT c FROM CostoMezzoTrasporto c")
  , @NamedQuery(name = "CostoMezzoTrasporto.findById", query = "SELECT c FROM CostoMezzoTrasporto c WHERE c.id = :id")
  , @NamedQuery(name = "CostoMezzoTrasporto.findByNomeMezzo", query = "SELECT c FROM CostoMezzoTrasporto c WHERE c.nomeMezzo = :nomeMezzo")
  , @NamedQuery(name = "CostoMezzoTrasporto.findByPesoMassimo", query = "SELECT c FROM CostoMezzoTrasporto c WHERE c.pesoMassimo = :pesoMassimo")
  , @NamedQuery(name = "CostoMezzoTrasporto.findByCosto", query = "SELECT c FROM CostoMezzoTrasporto c WHERE c.costo = :costo")
})
public class CostoMezzoTrasporto implements Serializable, ChiavePrimaria
{
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @Column(name = "nome_mezzo")
  private String nomeMezzo;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @Column(name = "peso_massimo")
  private BigDecimal pesoMassimo;
  @Basic(optional = false)
  @Column(name = "costo")
  private BigDecimal costo;

  public CostoMezzoTrasporto()
  {
  }

  public CostoMezzoTrasporto(Integer id)
  {
    this.id = id;
  }

  public CostoMezzoTrasporto(Integer id, String nomeMezzo, BigDecimal pesoMassimo, BigDecimal costo)
  {
    this.id = id;
    this.nomeMezzo = nomeMezzo;
    this.pesoMassimo = pesoMassimo;
    this.costo = costo;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getNomeMezzo()
  {
    return nomeMezzo;
  }

  public void setNomeMezzo(String nomeMezzo)
  {
    this.nomeMezzo = nomeMezzo;
  }

  public BigDecimal getPesoMassimo()
  {
    return pesoMassimo;
  }

  public void setPesoMassimo(BigDecimal pesoMassimo)
  {
    this.pesoMassimo = pesoMassimo;
  }

  public BigDecimal getCosto()
  {
    return costo;
  }

  public void setCosto(BigDecimal costo)
  {
    this.costo = costo;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object)
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof CostoMezzoTrasporto))
    {
      return false;
    }
    CostoMezzoTrasporto other = (CostoMezzoTrasporto) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "org.engim.tss2018.db.CostoMezzoTrasporto[ id=" + id + " ]";
  }
  
}
