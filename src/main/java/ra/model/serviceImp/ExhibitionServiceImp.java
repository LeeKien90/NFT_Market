package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.dto.response.ExhibitionResponse;
import ra.dto.response.HomeUser;
import ra.dto.response.TagResponse;
import ra.model.entity.Exhibition;
import ra.model.entity.Product;
import ra.model.entity.Tag;
import ra.model.repository.ExhibitionRepository;
import ra.model.service.ExhibitionService;

import java.util.ArrayList;
import java.util.List;
@Service
public class ExhibitionServiceImp implements ExhibitionService {
    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Override
    public List<ExhibitionResponse> getAll(int size) {

        Pageable pageable = PageRequest.of(0, size, Sort.by("startTime").descending());
        Page<Exhibition> exhibitions = exhibitionRepository.findAll(pageable);
        List<ExhibitionResponse> exhibitionResponses = new ArrayList<>();
        for (Exhibition exhibition: exhibitions) {
            ExhibitionResponse exhibitionResponse = data(exhibition);
            exhibitionResponses.add(exhibitionResponse);
        }
        return exhibitionResponses;
    }

    public static ExhibitionResponse data(Exhibition exhibition){
        ExhibitionResponse exhibitionResponse = new ExhibitionResponse();
        exhibitionResponse.setExhibitionId(exhibition.getExhibitionId());
        exhibitionResponse.setExhibitionTitle(exhibition.getExhibitionTitle());
        exhibitionResponse.setExhibitionDescription(exhibition.getExhibitionDescription());
        exhibitionResponse.setImage(exhibition.getImage());
        exhibitionResponse.setStarTime(exhibition.getStartTime());
        exhibitionResponse.setEndTime(exhibition.getEndTime());
        for (Product product: exhibition.getListProduct()) {
            HomeUser user = new HomeUser();
            user.setAuthorId(product.getArtist().getUserId());
            user.setAuthorName(product.getArtist().getFullName());
            user.setAvatar(product.getArtist().getAvatar());
            exhibitionResponse.getListHomeUser().add(user);
        }
        for (Tag tag: exhibition.getListTag()) {
            TagResponse tagNew = new TagResponse();
            tagNew.setTagId(tag.getTagID());
            tagNew.setTagName(tag.getTagName());
            exhibitionResponse.getListTag().add(tagNew);
        }

        return exhibitionResponse;
    }
}
