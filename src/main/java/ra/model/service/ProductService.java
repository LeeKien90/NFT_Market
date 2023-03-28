package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.dto.request.ProductRequest;
import ra.dto.response.HomeProduct;
import ra.dto.response.ProductResponse;
import ra.model.entity.Product;
import ra.model.entity.Tag;

import java.util.List;
import java.util.Map;

public interface ProductService {
    String save(ProductRequest productRequest);
    String update(ProductRequest productRequest);
    ProductResponse findById(int id);
    Map<String,Object> findByArtistId(int id, Pageable pageable);

    List<ProductResponse> findBidingProduct(int artistId);

    List<ProductResponse> findShortBidingProduct(int artistId);
    Map<String,Object> findAll(Pageable pageable);
    Page<Product> getProductByTagName(Tag tag , Pageable pageable);


    List<HomeProduct> getmostBiddingProduct();

    List<HomeProduct> getLiveAuctionProduct(int size,int page);
    Page<Product> getAll(Pageable pageable);
    List<ProductResponse> getNewCryptoArt(int size);

    List<ProductResponse> getFromHistory();
    List<ProductResponse> getAllProduct();
    List<ProductResponse> getFromOwnedByCreator();
    List<ProductResponse> getReserverdPrice();
    Map<String,Object> searchByProductNameContainsOrArtist_FullNameOrOwner(String name,int size, int page);

    Map<String, Object> getFromHistory(Pageable pageable);
    Map<String, Object> getAllProduct(Pageable pageable);
    Map<String, Object> getFromOwnedByCreator(Pageable pageable);
    Map<String, Object> getReserverdPrice(Pageable pageable);

    Map<String,Object> findByArtistCreationsId(int id, Pageable pageable);
    Map<String,Object> findByOwnerCollectionsId(int id, Pageable pageable);

}
