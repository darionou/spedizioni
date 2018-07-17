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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author svilupposw
 */
@Entity
@Table(name = "merce_spedizione")
@XmlRootElement
@NamedQueries(
{
  @NamedQuery(name = "MerceSpedizione.findAll", query = "SELECT m FROM MerceSpedizione m")
  , @NamedQuery(name = "MerceSpedizione.findById", query = "SELECT m FROM MerceSpedizione m WHERE m.id = :id")
  , @NamedQuery(name = "MerceSpedizione.findByQuantita", query = "SELECT m FROM MerceSpedizione m WHERE m.quantita = :quantita")
})
public class MerceSpedizione implements Serializable, ChiavePrimaria
{
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @Column(name = "quantita")
  private int quantita;
  @JoinColumn(name = "id_merce", referencedColumnName = "id")
  @ManyToOne(optional = false)
  private Merce idMerce;
  @JoinColumn(name = "id_spedizione", referencedColumnName = "id")
  @ManyToOne(optional = false)
  private Spedizione idSpedizione;

  public MerceSpedizione()
  {
  }

  public MerceSpedizione(Integer id)
  {
    this.id = id;
  }

  public MerceSpedizione(Integer id, int quantita)
  {
    this.id = id;
    this.quantita = quantita;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public int getQuantita()
  {
    return quantita;
  }

  public void setQuantita(int quantita)
  {
    this.quantita = quantita;
  }

  public Merce getIdMerce()
  {
    return idMerce;
  }

  public void setIdMerce(Merce idMerce)
  {
    this.idMerce = idMerce;
  }

  public Spedizione getIdSpedizione()
  {
    return idSpedizione;
  }

  public void setIdSpedizione(Spedizione idSpedizione)
  {
    this.idSpedizione = idSpedizione;
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
    if (!(object instanceof MerceSpedizione))
    {
      return false;
    }
    MerceSpedizione other = (MerceSpedizione) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "org.engim.tss2018.db.MerceSpedizione[ id=" + id + " ]";
  }
  
}
