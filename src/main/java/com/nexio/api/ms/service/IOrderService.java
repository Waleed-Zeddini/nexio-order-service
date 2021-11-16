package com.nexio.api.ms.service;
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
 * @since   2021-11-09 
 */
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nexio.api.ms.dto.OrderDTO;

public interface IOrderService {

	OrderDTO save(OrderDTO commande);
	
	Page<OrderDTO> findAll(Pageable pageable);

	List<OrderDTO> getAllOrderByClientId(Long clientId);

	List<OrderDTO> getAllOrderByPrixTotalGreaterThan(BigDecimal prixTotal);
	
	List<OrderDTO> getAllOrderByPrixTotalLess(BigDecimal prixTotal);

	
	List<OrderDTO> getAllOrderByDateBetween(LocalDate dateDebut, LocalDate dateFin);
	
	List<OrderDTO> getAllOrderByClientIdAndDateValeurBetween(Long clientId, LocalDate dateDebut, LocalDate dateFin);
	
	List<OrderDTO> findAll();

	Optional<OrderDTO> findOne(Long id);

	void delete(Long id);

	List<OrderDTO> getAllOrderByPrixTotalBetween(BigDecimal prixMin, BigDecimal prixMax);

}
