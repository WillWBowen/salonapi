package com.salon.www.salonapi.dao.itf;

import com.salon.www.salonapi.exception.RoleUpdateFailedException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id);
    List<T> getAll();
    void save(T t);
    void update(T t) throws RuntimeException;
    void delete(T t);
}
