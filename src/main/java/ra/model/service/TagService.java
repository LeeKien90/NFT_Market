package ra.model.service;
import ra.model.entity.Tag;

import java.util.List;
public interface TagService {
    Tag findByName(String tagName);
}
