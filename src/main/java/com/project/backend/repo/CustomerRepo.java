package com.project.backend.repo;

import com.project.backend.domain.CustomerEty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<CustomerEty, Long> {

}
