package com.nexio.api.ms.rest;
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
 * @since   2021-11-10 
 */

import com.nexio.api.ms.dto.OrderDTO;
import com.nexio.api.ms.service.IOrderService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
 

/**
 * REST controller for managing {@link com.nexio.api.ms.dto.OrderDTO}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:4200")
@Api(value="Order management",tags="Microservice - Order",description ="The main Microservice")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);
 
    private final IOrderService orderService;
    
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * {@code POST  /orders} : Create a new Order.
     *
     * @param OrderDTO the Order to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new Order, or with status {@code 400 (Bad Request)} if the Order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orders")
    @ApiOperation(value = "Save a new Order")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);

        OrderDTO result = orderService.save(order);
        return ResponseEntity.created(new URI("/api/orders/" + result.getCommande().getId()))
            .body(result);
    }

    /**
     * {@code PUT  /orders} : Updates an existing order.
     *
     * @param order the order to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated order,
     * or with status {@code 400 (Bad Request)} if the order is not valid,
     * or with status {@code 500 (Internal Server Error)} if the order couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orders")
    @ApiOperation(value = "Update an existed Order")
    public ResponseEntity<OrderDTO> updateOrder(@Valid @RequestBody OrderDTO order) throws URISyntaxException {
        log.debug("REST request to update Order : {}", order);
        OrderDTO result = orderService.save(order);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * {@code GET  /orders} : get all the orders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
     */
    @GetMapping("/orders")
    @ApiOperation(value = "Get all the orders - paginable")
    public ResponseEntity<List<OrderDTO>> getAllOrders(Pageable pageable) {
        log.debug("REST request to get a page of Orders");
        Page<OrderDTO> page = orderService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }
    
    /**
     * {@code GET  /orders} : get all the orders.
     *
     * @param.
     * @return the the List of Orders with status {@code 200 (OK)} and the list of orders in body.
     */ 
    @GetMapping("/orders/all")
    @ApiOperation(value = "Get all the orders - List")
    public  List<OrderDTO> getAllOrders() {
        log.debug("REST request to get all Orders");
        return orderService.findAll();
    }
    
    /**
     * {@code GET  /orders} : get all the orders of a client by his id.
     *
	 * @param clientId
     * @return the List of Orders with status {@code 200 (OK)} and the list of orders in body.
     */ 
    @GetMapping("/orders/all-commandes/client/id")
    @ApiOperation(value = "Get all the orders of a client by his id")
	public List<OrderDTO> getOrderDTOByClientId(
		    @ApiParam(
		    	    name =  "clientId",
		    	    value = "ID Client",
		    	    example = "1",
		    	    required = true)
			@RequestParam Long clientId) {
    	
    	 log.debug("REST request to get of a client by id");
  
	return orderService.getAllOrderByClientId(clientId);
	
	}
    
    /**
     * {@code GET  /orders} : get all the orders of a client, between two dates, by his id.
     *
	 * @param clientId
	 * @param dateDebut
	 * @param dateFin
     * @return the List of Orders with status {@code 200 (OK)} and the list of orders in body.
     */ 
    @GetMapping("/orders/all-commandes/client/between-dates")
    @ApiOperation(value = "Get all the orders of a client between two dates (Format Date must be : yyyy-MM-dd)")

	public List<OrderDTO> getOrderDTOByClientIdAndDateValeurBetween(
			@RequestParam Long clientId,
		    @ApiParam(
		    	    name =  "dateDebut",
		    	    value = "Date Début",
		    	    example = "2020-01-01",
		    	    required = true)
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateDebut,
			
		    @ApiParam(
		    	    name =  "dateFin",
		    	    value = "Date Fin",
		    	    example = "2021-12-31",
		    	    required = true)
			@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dateFin) {
    	
    	 log.debug("REST request to orders  get of a client between two dates.");
  
	return orderService.getAllOrderByClientIdAndDateValeurBetween(clientId, dateDebut, dateFin);
	
	}
    
    /**
     * {@code GET  /orders} : get all the orders between two dates.
     *
	 * @param dateDebut
	 * @param dateFin
     * @return the List of Orders with status {@code 200 (OK)} and the list of orders in body.
     */ 
    @GetMapping("/orders/all-commandes/between-dates")
    @ApiOperation(value = "Get all the orders between two dates (Format Date must be : yyyy-MM-dd)")
	public List<OrderDTO> getOrderDTOByDateBetween(
		    @ApiParam(
		    	    name =  "dateDebut",
		    	    value = "Date Début",
		    	    example = "2020-01-01",
		    	    required = true)
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateDebut,
			
		    @ApiParam(
		    	    name =  "dateFin",
		    	    value = "Date Fin",
		    	    example = "2021-12-31",
		    	    required = true)
			@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dateFin) {
    	
    	 log.debug("REST request to get orders between two dates.");
  
	return orderService.getAllOrderByDateBetween(dateDebut, dateFin);
	
	}
    
    /**
     * {@code GET  /orders} : get all the orders by PrixTotal GreaterThan.
     *
	 * @param prixTotal
     * @return the List of Orders with status {@code 200 (OK)} and the list of orders in body.
     */ 
    @GetMapping("/orders/all-commandes/prix-greater-than")
    @ApiOperation(value = "Get all the orders that price is greater than the price entred")
	public List<OrderDTO> getOrderDTOByByPrixTotalGreaterThan(
		    @ApiParam(
		    	    name =  "prixTotal",
		    	    value = "Prix Total Cde",
		    	    example = "100",
		    	    required = true)
			BigDecimal prixTotal) {
    	
    log.debug("REST request to get all the orders that its price greater than the price entred");
    
  	return orderService.getAllOrderByPrixTotalGreaterThan(prixTotal);
	
	}
    
    /**
     * {@code GET  /orders} : get all the orders by PrixTotal less.
     *
	 * @param prixTotal
     * @return the List of Orders with status {@code 200 (OK)} and the list of orders in body.
     */ 
    @GetMapping("/orders/all-commandes/prix-less-than")
    @ApiOperation(value = "Get all the orders that price is less than the price entred")
	public List<OrderDTO> getOrderDTOByPrixTotalLess(
		    @ApiParam(
		    	    name =  "prixTotal",
		    	    value = "Prix Total Cde",
		    	    example = "100",
		    	    required = true)
			BigDecimal prixTotal) {
    	
    log.debug("REST request to get all the orders that its price less than the price entred");
    
  	return orderService.getAllOrderByPrixTotalLess(prixTotal);
	
	}
  
    /**
     * {@code GET  /orders} : get all the orders by PrixTotal is between Min and Max.
     *
	 * @param prixMin
	 * @param prixMax
     * @return the List of Orders with status {@code 200 (OK)} and the list of orders in body.
     */ 
    @GetMapping("/orders/all-commandes/prix-between")
    @ApiOperation(value = "Get all the orders that price is between Min and Max")
	public List<OrderDTO> getOrderDTOByByPrixTotalGreaterThan(
			@ApiParam(
		    	    name =  "prixMin",
		    	    value = "Prix Min",
		    	    example = "10",
		    	    required = true)
			BigDecimal prixMin, 
			
			@ApiParam(
		    	    name =  "prixMax",
		    	    value = "Prix Max",
		    	    type= "number",
		    	    example = "100000",
		    	    required = true)
			BigDecimal prixMax) {
    	
    log.debug("REST request to get all the orders that its price is between Min and Max");
    
  	return orderService.getAllOrderByPrixTotalBetween(prixMin, prixMax);
	
	}

    /**
     * {@code GET  /orders/:id} : get the "id" order.
     *
     * @param id the id of the order to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the order, or with status {@code 404 (Not Found)}.
     */
    
    @GetMapping("/orders/{id}")
    @ApiOperation(value = "Get Order by id")
    public ResponseEntity<OrderDTO> getOrder(
			@ApiParam(
		    	    name =  "id",
		    	    value = "ID",
		    	    example = "53",
		    	    required = true)
    		@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        Optional<OrderDTO> order = orderService.findOne(id);
        return ResponseEntity.ok().body(order.get());
    }
    
   
    /**
     * {@code DELETE  /orders/:id} : delete the "id" order.
     *
     * @param id the id of the order to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    @ApiOperation(value = "Delete Order by id")
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(
			@ApiParam(
		    	    value = "ID",
		    	    example = "53",
		    	    required = true)
    		@PathVariable Long id) {
        log.debug("REST request to delete Order : {}", id);
        
        orderService.delete(id);
        
        return ResponseEntity.noContent().build();
    }
}
