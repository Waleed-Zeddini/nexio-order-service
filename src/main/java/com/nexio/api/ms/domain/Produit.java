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




import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value="Produit",description="Produit est un bean non persistant, il appartient au deuxième MS,  il sert pour les lignes commandes")
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    
    private Long id;

    
    private String code;

    private String marque;

    private String modele;

    private String caracteristiques;

    private BigDecimal prixUnitaire;

    private Long quantite;

 


   
}
