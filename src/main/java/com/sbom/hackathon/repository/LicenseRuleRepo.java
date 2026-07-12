package com.sbom.hackathon.repository;

import com.sbom.hackathon.entity.LicenseRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LicenseRuleRepo extends JpaRepository<LicenseRule,String> {
    Optional<LicenseRule> findByLicense(String license);
}