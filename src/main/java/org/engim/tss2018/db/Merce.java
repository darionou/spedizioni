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
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author svilupposw
 */
@Entity
@Table(name = "merce")
@XmlRootElement
@NamedQueries(
{
  @NamedQuery(name = "Merce.findAll", query = "SELECT m FROM Merce m")
  , @NamedQuery(name = "Merce.findById", query = "SELECT m FROM Merce m WHERE m.id = :id")
  , @NamedQuery(name = "Merce.findByCodice", query = "SELECT m FROM Merce m WHERE m.codice = :codice")
  , @NamedQuery(name = "Merce.findByDescrizione", query = "SELECT m FROM Merce m WHERE m.descrizione = :descrizione")
  , @NamedQuery(name = "Merce.findByPeso", query = "SELECT m FROM Merce m WHERE m.peso = :peso")
})
public class Merce implements Serializable, ChiavePrimaria
{
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @Column(name = "codice")
  private String codice;
  @Column(name = "descrizione")
  private String descrizione;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "peso")
  private BigDecimal peso;
  @OneToMany(mappedBy = "idMerce")
  private Collection<MerceSpedizione> merceSpedizioneCollection;

  public Merce()
  {
  }

  public Merce(Integer id)
  {
    this.id = id;
  }

  public Merce(Integer id, String codice)
  {
    this.id = id;
    this.codice = codice;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public BigDecimal getPeso()
  {
    return peso;
  }

  public void setPeso(BigDecimal peso)
  {
    this.peso = peso;
  }

  @XmlTransient
  public Collection<MerceSpedizione> getMerceSpedizioneCollection()
  {
    return merceSpedizioneCollection;
  }

  public void setMerceSpedizioneCollection(Collection<MerceSpedizione> merceSpedizioneCollection)
  {
    this.merceSpedizioneCollection = merceSpedizioneCollection;
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
    if (!(object instanceof Merce))
    {
      return false;
    }
    Merce other = (Merce) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "org.engim.tss2018.db.Merce[ id=" + id + " ]";
  }
  
}
