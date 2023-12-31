package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	@Query("SELECT p FROM Product p WHERE p.enabled = true AND p.category.enable = true " +
			"AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%" + 
			") ORDER BY p.name ASC")
	public Page<Product> findAllByCategories(Integer catId, String catMatchId, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true AND " + "p.alias = ?1")		
	public Product findByAliasByEnabled(String alias);
	
	@Query(value = "SELECT * FROM products AS p WHERE p.enabled AND " +
			"MATCH(name, short_description, full_description) AGAINST (?1)", nativeQuery = true)
	public Page<Product> search(String keyword, Pageable pageable);
}
