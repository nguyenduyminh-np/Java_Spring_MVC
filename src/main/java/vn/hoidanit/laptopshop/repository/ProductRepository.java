package vn.hoidanit.laptopshop.repository;

import vn.hoidanit.laptopshop.domain.Product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(long id);

    void deleteById(long id);

    Product save(Product product);
}
