package ra.dto.response;

import lombok.Data;
import ra.model.entity.Tag;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ExhibitionResponse {
    private int exhibitionId;
    private String exhibitionTitle;
    private String exhibitionDescription;
    private String image;
    private Date starTime;
    private Date endTime;
    private Set<HomeUser> listHomeUser = new HashSet<>();
    private Set<TagResponse> listTag = new HashSet<>();
}