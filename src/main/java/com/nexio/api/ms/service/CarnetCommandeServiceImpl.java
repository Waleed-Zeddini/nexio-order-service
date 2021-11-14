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
 * @since   2021-11-07 
 */
import com.nexio.api.ms.domain.CarnetCommande;
import com.nexio.api.ms.repository.CarnetCommandeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for implementing methods declared in Interface linked to  {@link CarnetCommande}.
 */
@Service
@Transactional
public class CarnetCommandeServiceImpl implements ICarnetCommandeService {

    private final Logger log = LoggerFactory.getLogger(CarnetCommandeServiceImpl.class);

    private final CarnetCommandeRepository carnetCommandeRepository;

    public CarnetCommandeServiceImpl(CarnetCommandeRepository carnetCommandeRepository) {
        this.carnetCommandeRepository = carnetCommandeRepository;
    }

    /**
     * Save a carnetCommande.
     *
     * @param carnetCommande the entity to save.
     * @return the persisted entity.
     */
    public CarnetCommande save(CarnetCommande carnetCommande) {
        log.debug("Request to save CarnetCommande : {}", carnetCommande);
        return carnetCommandeRepository.save(carnetCommande);
    }

    /**
     * Get all the carnetCommandes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CarnetCommande> findAll(Pageable pageable) {
        log.debug("Request to get all CarnetCommandes");
        return carnetCommandeRepository.findAll(pageable);
    }


    /**
     * Get one carnetCommande by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CarnetCommande> findOne(Long id) {
        log.debug("Request to get CarnetCommande : {}", id);
        return carnetCommandeRepository.findById(id);
    }

    /**
     * Delete the carnetCommande by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CarnetCommande : {}", id);
        carnetCommandeRepository.deleteById(id);
    }
}
