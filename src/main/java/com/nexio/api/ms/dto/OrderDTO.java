package com.nexio.api.ms.dto;

import com.nexio.api.ms.domain.*;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Client client;
	
	private Commande commande;
	
	private List<CarnetCommande> carnetCommandes;
	
	public List<CarnetCommande> getCarnetCommandes() {
        return carnetCommandes;
    }

    public void setCarnetCommandes(List<CarnetCommande> carnetCommandes) {
        this.carnetCommandes = carnetCommandes;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}