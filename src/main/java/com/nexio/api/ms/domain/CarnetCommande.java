package com.nexio.api.ms.domain;
/*
 * Copyright 2021 Zeddini, as indicated by the @author tags.
 *
 * Licensed under the zeddini License; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.zeddini.com/licenses/LICENSE-2.0
 *
 * @author  Zeddini Walid
 * @version 1.0.0
 * @since   2021-11-05 
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "carnet_commande")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
@ApiModel(value="LigneCommande",description="Ligne Commande est géré par le client, elle contient les produits à commander")
public class CarnetCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "qte", nullable = false)
    private Long qte;

    @NotNull
    @Column(name = "prix_unitaire", precision = 21, scale = 2, nullable = false)
    private BigDecimal prixUnitaire;

    @NotNull
    @Column(name = "prix_total", precision = 21, scale = 2, nullable = false)
    private BigDecimal prixTotal;

    @NotNull
    @Column(name = "etat", nullable = false)
    private Long etat;

	@JoinColumn(name = "produit_id",  nullable = false)
	private Long produitId;
	
	@Transient
	private Produit produit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commande_id",  nullable = false)
    @JsonIgnoreProperties(value = "carnets", allowSetters = true)
    private Commande commande;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQte() {
		return qte;
	}

	public void setQte(Long qte) {
		this.qte = qte;
	}

	public BigDecimal getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(BigDecimal prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	public BigDecimal getPrixTotal() {
		return prixTotal;
	}

	public void setPrixTotal(BigDecimal prixTotal) {
		this.prixTotal = prixTotal;
	}

	public Long getEtat() {
		return etat;
	}

	public void setEtat(Long etat) {
		this.etat = etat;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public Commande getCommande() {
		return commande;
	}

	public void setCommande(Commande commande) {
		this.commande = commande;
	}

	public Long getProduitId() {
		return produitId;
	}

	public void setProduitId(Long produitId) {
		this.produitId = produitId;
	}
	
	
}