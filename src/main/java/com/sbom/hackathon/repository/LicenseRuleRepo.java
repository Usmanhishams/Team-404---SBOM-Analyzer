package com.sbom.hackathon.repository;

import com.sbom.hackathon.entity.LicenseRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRuleRepo extends JpaRepository<LicenseRule,String> {

}