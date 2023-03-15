
package com.project.backend.repo;

import com.project.backend.domain.ExchangeEty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepo extends JpaRepository<ExchangeEty, Long> {
}
