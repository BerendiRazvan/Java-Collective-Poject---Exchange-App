package com.project.backend.repo;


import com.project.backend.domain.DonationEty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepo extends JpaRepository<DonationEty, Long> {

}