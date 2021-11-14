package com.nexio.api.ms.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.nexio.api.ms.dto.CommandeDTO;
import com.nexio.api.ms.dto.mapper.CommandeMapper;
import com.nexio.api.ms.config.Constants;
import com.nexio.api.ms.domain.CarnetCommande;
import com.nexio.api.ms.domain.Client;
import com.nexio.api.ms.domain.Commande;
import com.nexio.api.ms.domain.Produit;
import com.nexio.api.ms.dto.OrderDTO;

import com.nexio.api.ms.repository.ClientRepository;
import com.nexio.api.ms.repository.CommandeRepository;
import com.nexio.api.ms.repository.CarnetCommandeRepository;

/**
 * Service Implementation for implementing methods declared in Interface linked to {@link OrderDTO}.
 */
@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final ClientRepository clientRepository;
    private final CommandeRepository commandeRepository;
    private final CarnetCommandeRepository carnetCommandeRepository;
    
    private final CommandeMapper commandeMapper;
    
    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;
    
    private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	public OrderServiceImpl (CommandeRepository commandeRepository, ClientRepository clientRepository,
			CarnetCommandeRepository carnetCommandeRepository, CommandeMapper commandeMapper) {
		this.commandeRepository = commandeRepository;
		this.clientRepository = clientRepository;
		this.carnetCommandeRepository = carnetCommandeRepository;
		
		this.commandeMapper = commandeMapper;

	}

	 /**
     * Create an order.
     *
     * @param order the entity to save.
     * @return the persisted entity.
     */
	
 
	 
	    /**
	     * Save a order.
	     *
	     * @param order the entity to save.
	     * @return the persisted entity.
	     */
			 
	    public Commande save(Commande commande) {
	        log.debug("Request to save CommandeDTO : {}", commande);
	        
	       
 	       
	        if(commande!=null) {


	        setClientFakeOrder(commande); // NOTA : just for test without View(Frontend), in order to avoid exception
	        
	        commande = commandeRepository.save(commande);
	        
	        if(commande.getId()!=null) {
	        	for (CarnetCommande ligneCde : commande.getCarnets()) {
	        		ligneCde.setCommande(commande);
	        		
	        		if(ligneCde.getPrixUnitaire()==null) { // NOTA : To test without View (Frontend), in order to avoid exception
	        		ligneCde.setEtat(new Long(1));
	        		ligneCde.setPrixUnitaire(new BigDecimal(10));
	        		ligneCde.setPrixTotal(new BigDecimal(10));
	        		ligneCde.setQte(new Long(1));
	        		}
	        		
	        		ligneCde = carnetCommandeRepository.save(ligneCde);
				}
	        	
 	        }

	    } 
	        return commande;
	    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Commande> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
   
        Page<Commande> commandes = commandeRepository.findAll(pageable);
        
         	
        	for (Commande commande : commandes) {
        		
        		if (commande != null) {
        			CommandeDTO orderDTO = commandeMapper.orderToCommandeDTO(commande);
        			
    	            Set<CarnetCommande> carnets = orderDTO.getCarnets().stream()
    	                .map(carnetCommandeRepository::findById)
    	                .filter(Optional::isPresent)
    	                .map(Optional::get)
    	                .collect(Collectors.toSet());
    	            
    	            for (Iterator<CarnetCommande> iterator = carnets.iterator(); iterator.hasNext();) {
   						CarnetCommande carnetCommande = (CarnetCommande) iterator.next();
   			        	carnetCommande.setProduit((getProduitByCde(carnetCommande.getProduitId())));
	   		   }
    	            
    	            commande.setCarnets(carnets);
    	        }
			}
        	
      
        	Page<Commande> pageOrders = new PageImpl<Commande>(commandes.getContent(), pageable, Integer.valueOf(commandes.getSize()).longValue());
        
        return pageOrders;
    }


    @Transactional(readOnly = true)
    public List<Commande> findAll() {
        log.debug("Request to get all Orders");
   
        List<Commande> commandes = commandeRepository.findAll();
        
         	
        	for (Commande commande : commandes) {
        		
        		if (commande != null) {
        			CommandeDTO orderDTO = commandeMapper.orderToCommandeDTO(commande);
        			
    	            Set<CarnetCommande> carnets = orderDTO.getCarnets().stream()
    	                .map(carnetCommandeRepository::findById)
    	                .filter(Optional::isPresent)
    	                .map(Optional::get)
    	                .collect(Collectors.toSet());
    	            
    	           	
    	            for (Iterator<CarnetCommande> iterator = carnets.iterator(); iterator.hasNext();) {
    	   						CarnetCommande carnetCommande = (CarnetCommande) iterator.next();
    	   			        	carnetCommande.setProduit((getProduitByCde(carnetCommande.getProduitId())));
    	   		   }
    	            
    	            commande.setCarnets(carnets);
    	        }
			}
         return commandes;
    }

    public Produit getProduitByCde(Long produitId){
    	Produit produit = circuitBreakerFactory.create("produit-details").run(()->{
                    ResponseEntity<Produit> produitEntity = restTemplate.exchange(Constants.PRODUIT_API_URL + produitId, HttpMethod.GET,null,new ParameterizedTypeReference<Produit>(){});
                    return produitEntity.getBody();
                }, throwable -> new Produit()
        );
        return produit;
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Commande> findOne(Long id) {
        log.debug("Request to get CommandeDTO : {}", id);
         
        Commande commande = new Commande();
      
        if(commandeRepository.existsById(id))
        {
        commande = commandeRepository.getById(id);
 	        	
		CommandeDTO orderDTO = commandeMapper.orderToCommandeDTO(commande);
		
        Set<CarnetCommande> carnets = orderDTO.getCarnets().stream()
            .map(carnetCommandeRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
        
        for (Iterator<CarnetCommande> iterator = carnets.iterator(); iterator.hasNext();) {
			CarnetCommande carnetCommande = (CarnetCommande) iterator.next();
	        	carnetCommande.setProduit((getProduitByCde(carnetCommande.getProduitId())));
		   }
        
        commande.setCarnets(carnets);
	       
        } 
        
        return Optional.ofNullable(commande) ;
    }
    
  

    /**
     * Delete the order by id: Commande and its children : CarnetCommande
     * 
     * @param id the id of the entity.
     * 
     * THE SECURE MODE FOR DDELETING IS TO ENSURE 
     * OF EXISTENCE LIST OF CHILDREN BY FETCING THEM
     * AFTER THAT DELETE EVERY EXISTED ONE
     */
    public void delete(Long id) {
        log.debug("Request to delete CommandeDTO : {}", id);
        
        Commande commande = new Commande();
        
        if(commandeRepository.existsById(id))
        {
        commande = commandeRepository.getById(id);
        
		CommandeDTO orderDTO = commandeMapper.orderToCommandeDTO(commande);
			
            Set<CarnetCommande> carnets = orderDTO.getCarnets().stream()
                .map(carnetCommandeRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            
            for (Iterator<CarnetCommande> iterator = carnets.iterator(); iterator.hasNext();) {
					CarnetCommande carnetCommande = (CarnetCommande) iterator.next();
					carnetCommandeRepository.deleteById(carnetCommande.getId());
		   }
            
            commandeRepository.deleteById(id);
        }
    }
    
    /**
     * fake Data for client and Etat
     * in order to insert Order in DB
     * @return
     */
    Commande setClientFakeOrder(Commande commande) {
		Client client = new Client();
		client.setId(new Long(1)); // Client exists in DB
		commande.setClient(client);
		commande.setEtat(new Long(1));
		return commande;
	}
}