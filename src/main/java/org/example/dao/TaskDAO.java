package org.example.dao;

import org.example.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDAO extends CrudRepository<Task, Long> {

    Page<Task> findAll(Pageable pageable);
}
