package ra.model.service;

import ra.dto.response.ExhibitionResponse;


import java.util.List;

public interface ExhibitionService {
    List<ExhibitionResponse> getAll(int size);
}
