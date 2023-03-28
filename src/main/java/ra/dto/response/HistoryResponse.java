package ra.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class HistoryResponse {
    private int historyId;
    private int userId;
    private String userName;
    private float price;
    private Date createdDate;
}
