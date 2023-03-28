package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.dto.request.ProductRequest;
import ra.dto.response.*;
import ra.model.entity.*;
import ra.model.repository.*;
import ra.model.service.ProductService;

import java.net.Authenticator;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private BidTimeRepository bidTimeRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private BidRepository bIdRepository;
    @Autowired
    private ProductTimeRepository productTimeRepository;

    @Override
    public String save(ProductRequest productRequest) {
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setProductPrice(productRequest.getPrice());
        product.setCreateDate(new Date());
        product.setProductImage(productRequest.getProductImage());
        product.setArtist(userRepository.findById(productRequest.getArtistId()).get());
        product.setOwner(userRepository.findById(productRequest.getArtistId()).get());
        for (Integer id : productRequest.getTagId()) {
            product.getListTag().add(tagRepository.findById(id).get());
        }
        try {
            productRepository.save(product);
            return "Đã thêm sản phẩm thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Thêm sản phẩm thất bại!";
        }
    }

    @Override
    public String update(ProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getProductId()).get();
        product.setProductName(productRequest.getProductName());
        product.setProductPrice(productRequest.getPrice());
        product.setArtist(userRepository.findById(productRequest.getArtistId()).get());
        product.setOwner(userRepository.findById(productRequest.getOwnerId()).get());
        product.getListTag().removeAll(product.getListTag());
        for (Integer id : productRequest.getTagId()) {
            product.getListTag().add(tagRepository.findById(id).get());
        }
        try {
            productRepository.save(product);
            return "Đã sửa sản phẩm thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Sửa sản phẩm thất bại!";
        }
    }

    @Override
    public ProductResponse findById(int id) {
        ProductResponse productResponse = changeData(productRepository.findById(id).get());
        BidTime bidTime = bidTimeRepository.checkNow();
        if (bidTime != null) {
            productResponse.setEndTime(bidTime.getEndTime());
        }
        return productResponse;
    }

    @Override
    public Map<String, Object> findByArtistId(int id, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAllByArtist_UserId(id, pageable);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : pageProduct) {
            ProductResponse productResponse = changeData(product);
            BidTime bidTime = bidTimeRepository.checkNow();
            productResponse.setEndTime(bidTime.getEndTime());
            list.add(productResponse);
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listOrder", list);
        listResponse.put("total", pageProduct.getSize());
        listResponse.put("totalItems", pageProduct.getTotalElements());
        listResponse.put("totalPage", pageProduct.getTotalPages());
        return listResponse;
    }


    @Override
    public List<HomeProduct> getmostBiddingProduct() {
        List<Product> listProduct = productRepository.findMostBidding();
        List<HomeProduct> list = new ArrayList<>();
        BidTime bidTime = bidTimeRepository.checkNow();
        for (Product product : listProduct) {
            HomeProduct homeProduct = new HomeProduct();
            homeProduct.setProductId(product.getProductId());
            homeProduct.setAuthorName(product.getArtist().getUserName());
            homeProduct.setAuthorId(product.getArtist().getUserId());
//            History history = historyRepository.findByProduct_ProductIdOrderByHistoryTimeDesc(product.getProductId());
//            homeProduct.setReservePrice(history.getHistoryPrice());
            homeProduct.setEndtime(bidTime.getEndTime());
            List<Bid> bid = bIdRepository.findByProductTime_ProductTimeIdOrderByBidMoneyDesc(productTimeRepository.findByProduct_ProductIdAndBidTime_BidTimeId(product.getProductId(),bidTime.getBidTimeId()).getProductTimeId());
            homeProduct.setCurentBid(bid.get(0).getBidMoney());
            list.add(homeProduct);
        }
        return list;
    }


    @Override
    public List<HomeProduct> getLiveAuctionProduct(int size, int page) {
        List<Product> listProduct = productRepository.getLiveAuctionProduct(size, page * size);
        List<HomeProduct> list = new ArrayList<>();
        BidTime bidTime = bidTimeRepository.checkNow();
        for (Product product : listProduct) {
            HomeProduct homeProduct = new HomeProduct();
            homeProduct.setProductId(product.getProductId());
            homeProduct.setAuthorName(product.getArtist().getUserName());
            homeProduct.setAuthorId(product.getArtist().getUserId());
            homeProduct.setProductName(product.getProductName());
//            History history = historyRepository.findByProduct_ProductIdOrderByHistoryTimeDesc(product.getProductId());
//            homeProduct.setReservePrice(history.getHistoryPrice());
            homeProduct.setEndtime(bidTime.getEndTime());
            List<Bid> bid = bIdRepository.findByProductTime_ProductTimeIdOrderByBidMoneyDesc(productTimeRepository.findByProduct_ProductIdAndBidTime_BidTimeId(product.getProductId(),bidTime.getBidTimeId()).getProductTimeId());
            homeProduct.setCurentBid(bid.get(0).getBidMoney());
            list.add(homeProduct);
        }
        return list;
    }

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<ProductResponse> getNewCryptoArt(int size) {
        Pageable pageable = PageRequest.of(0,size, Sort.by("createDate").descending());
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product: productPage) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductId(product.getProductId());
            productResponse.setProductImage(product.getProductImage());
            productResponse.setProductName(product.getProductName());
            productResponse.setPrice(product.getProductPrice());
            productResponse.setArtistId(product.getArtist().getUserId());
            productResponse.setArtistName(product.getArtist().getFullName());
            productResponses.add(productResponse);
        }
        return productResponses;
    }


    public List<ProductResponse> getFromHistory() {
        return productRepository.getALLProductFormHistory().stream().map(this::changeData).collect(Collectors.toList());
    }


    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> listProduct = productRepository.findAll();
        List<ProductResponse> listRP = new ArrayList<>();
        for (Product product : listProduct) {
            ProductResponse productResponse = changeData(product);
            listRP.add(productResponse);
        }
        return listRP;
    }

    @Override
    public List<ProductResponse> getFromOwnedByCreator() {
        return productRepository.getAllOwnedByCreator().stream().map(this::changeData).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getReserverdPrice() {
        return productRepository.findAllReserverdPrice().stream().map(this::changeData).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> searchByProductNameContainsOrArtist_FullNameOrOwner(String name, int size, int page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> productPage = productRepository.searchByProductNameContainsOrArtist_FullNameOrOwner_UserName(name,name,name,pageable);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product :productPage) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductName(product.getProductName());
            productResponse.setArtistName(product.getArtist().getFullName());
            productResponse.setProductImage(product.getProductImage());
            productResponse.setPrice(product.getProductPrice());
            list.add(productResponse);
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listSearch", list);
        listResponse.put("total", productPage.getSize());
        listResponse.put("totalItems", productPage.getTotalElements());
        listResponse.put("totalPage", productPage.getTotalPages());
        return listResponse;

    }


    public Map<String, Object> getFromHistory(Pageable pageable) {
        Page<Product> pageProduct = productRepository.getALLProductFormHistory(pageable);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : pageProduct) {
            ProductResponse productResponse = changeData(product);
            BidTime bidTime = bidTimeRepository.checkNow();
            productResponse.setEndTime(bidTime.getEndTime());
            list.add(productResponse);
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listOrder", list);
        listResponse.put("total", pageProduct.getSize());
        listResponse.put("totalItems", pageProduct.getTotalElements());
        listResponse.put("totalPage", pageProduct.getTotalPages());
        return listResponse;
    }

    @Override
    public Map<String, Object> getAllProduct(Pageable pageable) {
        Page<Product> pageProduct = productRepository.getALlPro(pageable);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : pageProduct) {
            ProductResponse productResponse = changeData(product);
            BidTime bidTime = bidTimeRepository.checkNow();
            productResponse.setEndTime(bidTime.getEndTime());
            list.add(productResponse);
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listOrder", list);
        listResponse.put("total", pageProduct.getSize());
        listResponse.put("totalItems", pageProduct.getTotalElements());
        listResponse.put("totalPage", pageProduct.getTotalPages());
        return listResponse;
    }

    @Override
    public Map<String, Object> getFromOwnedByCreator(Pageable pageable) {
        Page<Product> pageProduct = productRepository.getAllOwnedByCreator(pageable);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : pageProduct) {
            ProductResponse productResponse = changeData(product);
            BidTime bidTime = bidTimeRepository.checkNow();
            productResponse.setEndTime(bidTime.getEndTime());
            list.add(productResponse);
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listOrder", list);
        listResponse.put("total", pageProduct.getSize());
        listResponse.put("totalItems", pageProduct.getTotalElements());
        listResponse.put("totalPage", pageProduct.getTotalPages());
        return listResponse;
    }

    @Override
    public Map<String, Object> getReserverdPrice(Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAllReserverdPrice(pageable);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : pageProduct) {
            ProductResponse productResponse = changeData(product);
            BidTime bidTime = bidTimeRepository.checkNow();
            productResponse.setEndTime(bidTime.getEndTime());
            list.add(productResponse);
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listOrder", list);
        listResponse.put("total", pageProduct.getSize());
        listResponse.put("totalItems", pageProduct.getTotalElements());
        listResponse.put("totalPage", pageProduct.getTotalPages());
        return listResponse;
    }

    @Override
    public Map<String, Object> findByArtistCreationsId(int id, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAllByArtist_UserId(id, pageable);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : pageProduct) {
            ProductResponse productResponse = changeData(product);
            BidTime bidTime = bidTimeRepository.checkNow();
            productResponse.setEndTime(bidTime.getEndTime());
            list.add(productResponse);
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listOrder", list);
        listResponse.put("total", pageProduct.getSize());
        listResponse.put("totalItems", pageProduct.getTotalElements());
        listResponse.put("totalPage", pageProduct.getTotalPages());
        return listResponse;
    }

    @Override
    public Map<String, Object> findByOwnerCollectionsId(int id, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAllByOwner_UserId(id, pageable);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : pageProduct) {
            ProductResponse productResponse = changeData(product);
            BidTime bidTime = bidTimeRepository.checkNow();
            productResponse.setEndTime(bidTime.getEndTime());
            list.add(productResponse);
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listOrder", list);
        listResponse.put("total", pageProduct.getSize());
        listResponse.put("totalItems", pageProduct.getTotalElements());
        listResponse.put("totalPage", pageProduct.getTotalPages());
        return listResponse;
    }


    public List<ProductResponse> findBidingProduct(int artistId) {
        List<Product> listProduct = productRepository.findAllBidding(artistId);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : listProduct) {
            ProductResponse productResponse = changeData(product);
            list.add(productResponse);
        }
        return list;
    }

    @Override
    public List<ProductResponse> findShortBidingProduct(int artistId) {
        List<Product> listProduct = productRepository.findSortBidding(artistId);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : listProduct) {
            ProductResponse productResponse = changeData(product);

            list.add(productResponse);
        }
        return list;
    }


    @Override
    public Map<String, Object> findAll(Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAll(pageable);
        List<ProductResponse> list = new ArrayList<>();
        for (Product product : pageProduct) {
            list.add(changDataSoft(product));
        }
        Map<String, Object> listResponse = new HashMap<>();
        listResponse.put("listOrder", list);
        listResponse.put("total", pageProduct.getSize());
        listResponse.put("totalItems", pageProduct.getTotalElements());
        listResponse.put("totalPage", pageProduct.getTotalPages());
        return listResponse;
    }

    @Override
    public Page<Product> getProductByTagName(Tag tag, Pageable pageable) {
        return productRepository.findProductByListTagContaining(tag, pageable);
    }


    public  ProductResponse changeData(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(product.getProductId());
        productResponse.setProductName(product.getProductName());
        productResponse.setProductImage(product.getProductImage());
        productResponse.setArtistId(product.getArtist().getUserId());
        productResponse.setArtistName(product.getArtist().getUserName());
        productResponse.setOwnerId(product.getOwner().getUserId());
        productResponse.setOwnerName(product.getOwner().getUserName());
        productResponse.setPrice(product.getProductPrice());
        for (Tag tag :product.getListTag()) {
            TagResponse tagResponse = new TagResponse();
            tagResponse.setTagId(tag.getTagID());
            tagResponse.setTagName(tag.getTagName());
            productResponse.getListTag().add(tagResponse);
        }
        for (History history : product.getListHistory()) {
            HistoryResponse historyResponse = new HistoryResponse();
            historyResponse.setHistoryId(history.getHistoryId());
            historyResponse.setPrice(historyResponse.getPrice());
            historyResponse.setUserId(history.getUsers().getUserId());
            historyResponse.setUserName(history.getUsers().getUserName());
            historyResponse.setCreatedDate(history.getHistoryTime());
            productResponse.getListHistory().add(historyResponse);
        }
        return productResponse;
    }

    private static BidResponse changeData(Bid bid) {
        BidResponse bidResponse = new BidResponse();
        bidResponse.setUserId(bid.getUsers().getUserId());
        bidResponse.setBidId(bid.getBidID());
        bidResponse.setPrice(bid.getBidMoney());
        bidResponse.setUserName(bid.getUsers().getUserName());
        bidResponse.setCreatedDate(bid.getBidDateTime());
        return bidResponse;
    }

    private static ProductResponse changDataSoft(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(product.getProductId());
        productResponse.setProductName(product.getProductName());
        productResponse.setProductImage(product.getProductImage());
        productResponse.setArtistId(product.getArtist().getUserId());
        productResponse.setArtistName(product.getArtist().getUserName());
        productResponse.setOwnerId(product.getOwner().getUserId());
        productResponse.setOwnerName(product.getOwner().getUserName());
        productResponse.setPrice(product.getProductPrice());
        return productResponse;
    }

}

