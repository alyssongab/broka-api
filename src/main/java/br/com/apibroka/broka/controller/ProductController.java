package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.product.ProductDTO;
import br.com.apibroka.broka.model.Product;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Produtos")
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/restaurants/{restaurantId}/products")
    @Operation(description = "Lista todos os produtos de um determinado restaurante")
    public List<ProductDTO> getProductsByRestaurant(@PathVariable UUID restaurantId){
        return productService.findProductsByRestaurant(restaurantId);
    }

    @PostMapping("/restaurants/{restaurantId}/products")
    @PreAuthorize("hasRole('ROLE_DONO_RESTAURANTE')")
    @Operation(description = "Adiciona um novo produto para o restaurante especificado")
    public ResponseEntity<ProductDTO> addProductToRestaurant(
        @PathVariable UUID restaurantId,
        @RequestBody ProductDTO dto,
        @AuthenticationPrincipal User owner
    ){
        ProductDTO newProd = productService.addProduct(restaurantId, dto, owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProd);
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_DONO_RESTAURANTE')")
    @Operation(description = "Atualiza dados de um determinado produto através do Id")
    public ResponseEntity<ProductDTO> updateProductFromRestaurant(
        @PathVariable UUID productId,
        @RequestBody ProductDTO dto,
        @AuthenticationPrincipal User owner
    ){
        ProductDTO updated = productService.updateProduct(productId, dto, owner);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_DONO_RESTAURANTE')")
    @Operation(description = "Deleta um produto através do Id")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable UUID productId,
        @AuthenticationPrincipal User owner
    ){
        productService.deleteProduct(productId, owner);
        return ResponseEntity.noContent().build();
    }
}
