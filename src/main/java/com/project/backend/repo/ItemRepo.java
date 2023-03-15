package com.project.backend.repo;

import com.project.backend.domain.ItemEty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepo extends JpaRepository<ItemEty, Long> {

}
