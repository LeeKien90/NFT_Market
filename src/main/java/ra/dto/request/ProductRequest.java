package ra.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    private int productId;
    private String productName;
    private String productImage;
    private float price;
    private int artistId;
    private int ownerId;
    private List<Integer> tagId;
}
