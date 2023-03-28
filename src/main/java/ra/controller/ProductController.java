package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.ProductRequest;
import ra.dto.request.ProductTimeRequest;
import ra.dto.response.ProductResponse;
import ra.model.entity.Product;
import ra.model.entity.Tag;
import ra.model.service.ProductService;
import ra.model.service.ProductTimeService;
import ra.model.service.TagService;
import ra.model.serviceImp.ProductServiceImp;
import ra.security.CustomUserDetails;

import java.util.*;


@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ProductTimeService productTimeService;
    @GetMapping
    List<ProductResponse> getAll(){
        return productService.getAllProduct();
    }


    @GetMapping("findById/{productId}")
    public ResponseEntity<?> findById(@PathVariable("productId") int id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping("create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        productRequest.setArtistId(customUserDetails.getUserId());
        return ResponseEntity.ok(productService.save(productRequest));
    }

    @PutMapping("update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") int productId, @RequestBody ProductRequest productRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        productRequest.setProductId(productId);
        productRequest.setArtistId(customUserDetails.getUserId());
        return ResponseEntity.ok(productService.update(productRequest));
    }

    @GetMapping("findProductByArtistId/{artistId}")
    public ResponseEntity<?> findByArtistId(@PathVariable("artistId") int artistId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.findByArtistId(artistId, pageable));
    }

    @GetMapping("getMostBiddingProduct")
    public ResponseEntity<?> getMostBiddingProduct() {
        return ResponseEntity.ok(productService.getmostBiddingProduct());
    }


    @GetMapping("getShortProductByArtistId")
    public ResponseEntity<?> getShortProductByArtistId(@PathVariable("artistId") int artistId) {
        return ResponseEntity.ok(productService.findShortBidingProduct(artistId));
    }

    @GetMapping("getShortProductByNameOrPriceOrDate")
    public ResponseEntity<?> Sort(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "3") int size,
                                  @RequestParam("direction") String direction,
                                  @RequestParam("sortBy") String sortBy) {
        Pageable pageable;
        if (sortBy.equals("name")) {
            if (direction.equals("asc")) {
                pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by("productName").descending());
            }
        } else if (sortBy.equals("price")) {
            if (direction.equals("asc")) {
                pageable = PageRequest.of(page, size, Sort.by("productPrice").ascending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by("productPrice").descending());
            }
        } else {
            if (direction.equals("asc")) {
                pageable = PageRequest.of(page, size, Sort.by("createDate").ascending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
            }
        }
        return ResponseEntity.ok(productService.findAll(pageable));
    }


    @GetMapping("/getByTagName/{tagName}")
    public ResponseEntity<Map<String, Object>> getByTagName(@PathVariable("tagName") String tagName,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "9") int size,
                                                            @RequestParam String direction) {
        Tag tag = tagService.findByName(tagName);
        Sort.Order order;
        if (direction.equals("asc")) {
            order = new Sort.Order(Sort.Direction.ASC, "productPrice");
        } else {
            order = new Sort.Order(Sort.Direction.DESC, "productPrice");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Product> pageProduct = productService.getProductByTagName(tag, pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("products", pageProduct.getContent());
        data.put("total", pageProduct.getSize());
        data.put("totalItems", pageProduct.getTotalElements());
        data.put("totalPages", pageProduct.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);

    }


    @GetMapping("getLiveAuctionProduct")
    public ResponseEntity<?> getLiveAuctionProduct(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(productService.getLiveAuctionProduct(size, page));
    }

    @GetMapping("get_paging_and_sortBy")
    public ResponseEntity<Map<String, Object>> getPagingAndSortingBy(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam String direction,
            @RequestParam String sortBy) {
        Sort.Order order = null;
        if (direction.equals("asc")) {
            order = new Sort.Order(Sort.Direction.ASC, sortBy);
        } else if (direction.equals("desc")) {
            order = new Sort.Order(Sort.Direction.DESC, sortBy);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Product> pageProduct = productService.getAll(pageable);
        Map<String, Object> data = new HashMap<>();
//        Dữ liệu trả về trên 1 trang
        data.put("products", pageProduct.getContent());
//        Tổng bản ghi trên 1 trang
        data.put("total", pageProduct.getSize());
//        Tổng dữ liệu
        data.put("totalAuthors", pageProduct.getTotalElements());
//        Tổng số trang
        data.put("totalPage", pageProduct.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @GetMapping("getNewCryptoArt")
    public ResponseEntity<?> getNewCryptoArt(@RequestParam(defaultValue = "9") int size) {
        return ResponseEntity.ok(productService.getNewCryptoArt(size));

    }
    @GetMapping("get_all_product_by_fiter")
    public List<ProductResponse> getProductHasListPrice(@RequestParam String filterName) {
        List<ProductResponse> listProduct = new ArrayList<>();
        if (filterName.equals("hasListPrice")) {
            listProduct = productService.getAllProduct();
        } else if (filterName.equals("hasOpenOffer")) {;
           listProduct=  productService.getFromHistory();
        } else if (filterName.equals("OwnedByCreator")) {
            listProduct = productService.getFromOwnedByCreator();
        } else if (filterName.equals("hasSold")) {
            listProduct=  productService.getFromHistory();
        } else if (filterName.equals("ReserverdPrice")) {
            listProduct = productService.getReserverdPrice();
        }
        return listProduct;

    }
    @GetMapping("find")
    public ResponseEntity<?> findBy(@RequestParam String filterName,
                                    @RequestParam String sortBy,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "3") int size,
                                    @RequestParam String direction) {

        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> page1 = null;
        if (filterName.equals("hasSold")) {
            if (sortBy.equals("name")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productName").descending());
                }

            } else if (sortBy.equals("price")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").descending());
                }
            } else {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
                }
            }
            page1 = productService.getFromHistory(pageable);
        } else if (filterName.equals("OwnedByCreator")) {
            if (sortBy.equals("name")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productName").descending());
                }

            } else if (sortBy.equals("price")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").descending());
                }
            } else {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
                }
            }
            page1 = productService.getFromOwnedByCreator(pageable);
        } else if (filterName.equals("ReserverdPrice")) {
            if (sortBy.equals("name")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productName").descending());
                }

            } else if (sortBy.equals("price")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").descending());
                }
            } else {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
                }
            }
            page1 = productService.getReserverdPrice(pageable);
        } else if (filterName.equals("hasListPrice")) {
            if (sortBy.equals("name")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productName").descending());
                }

            } else if (sortBy.equals("price")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").descending());
                }
            } else {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
                }
            }
            page1 = productService.getAllProduct(pageable);
        }
        else if (filterName.equals("hasOpenOffer")) {
            if (sortBy.equals("name")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productName").descending());
                }

            } else if (sortBy.equals("price")) {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").descending());
                }
            } else {
                if (direction.equals("asc")) {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").ascending());
                } else {
                    pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
                }
            }
            page1 = productService.getFromHistory(pageable);
        }
        return ResponseEntity.ok(page1);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByProductNameContainsOrArtist_FullNameOrOwner(@RequestParam(defaultValue = "10") int size,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam("name") String name){
        return ResponseEntity.ok(productService.searchByProductNameContainsOrArtist_FullNameOrOwner(name,size,page));
    }
    @GetMapping("findCreationsArtist/{artistId}")
    public ResponseEntity<?> findCreationsArtist(@PathVariable("artistId") int artistId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.findByArtistCreationsId(artistId, pageable));
    }
    @GetMapping("findCollectionsOwner/{ownerId}")
    public ResponseEntity<?> findCollectionsOwner(@PathVariable("ownerId") int ownerId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.findByOwnerCollectionsId(ownerId, pageable));
    }

    @PostMapping("/createProductTime")
    public ResponseEntity<?> createProductTime(@RequestBody ProductTimeRequest productTimeRequest){
        return ResponseEntity.ok(productTimeService.createProductTime(productTimeRequest));
    }
}
