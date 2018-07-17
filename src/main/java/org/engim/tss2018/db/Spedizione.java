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
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author svilupposw
 */
@Entity
@Table(name = "spedizione")
@XmlRootElement
@NamedQueries(
{
  @NamedQuery(name = "Spedizione.findAll", query = "SELECT s FROM Spedizione s")
  , @NamedQuery(name = "Spedizione.findById", query = "SELECT s FROM Spedizione s WHERE s.id = :id")
  , @NamedQuery(name = "Spedizione.findByNumero", query = "SELECT s FROM Spedizione s WHERE s.numero = :numero")
  , @NamedQuery(name = "Spedizione.findByData", query = "SELECT s FROM Spedizione s WHERE s.data = :data")
})
public class Spedizione implements Serializable, ChiavePrimaria
{
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Basic(optional = false)
  @Column(name = "numero")
  private int numero;
  @Column(name = "data")
  @Temporal(TemporalType.DATE)
  private Date data;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSpedizione")
  private Collection<MerceSpedizione> merceSpedizioneCollection;

  public Spedizione()
  {
  }

  public Spedizione(Integer id)
  {
    this.id = id;
  }

  public Spedizione(Integer id, int numero)
  {
    this.id = id;
    this.numero = numero;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public int getNumero()
  {
    return numero;
  }

  public void setNumero(int numero)
  {
    this.numero = numero;
  }

  public Date getData()
  {
    return data;
  }

  public void setData(Date data)
  {
    this.data = data;
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
    if (!(object instanceof Spedizione))
    {
      return false;
    }
    Spedizione other = (Spedizione) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "org.engim.tss2018.db.Spedizione[ id=" + id + " ]";
  }

}
