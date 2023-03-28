package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Tag;
import ra.model.repository.TagRepository;
import ra.model.service.TagService;

import java.util.List;

@Service
public class TagServiceImp implements TagService {
    @Autowired
    private TagRepository tagRepository;
    @Override
    public Tag findByName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }
}
