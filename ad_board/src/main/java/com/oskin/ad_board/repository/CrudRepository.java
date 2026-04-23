package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.IIdentified;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrudRepository<T extends IIdentified> {

    boolean delete(int id);

    List<T> findAll();

    Optional<T> findById(int id);

    boolean create(T object);

    boolean update(T object);
}
