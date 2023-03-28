package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ra.dto.response.ExhibitionResponse;
import ra.model.service.ExhibitionService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/exhibition")
public class ExhibitionController {
    @Autowired
    private ExhibitionService exhibitionService;

    @GetMapping("/getAll")
    public List<ExhibitionResponse> getAll(@RequestParam(defaultValue = "4") int size){
        return exhibitionService.getAll(size);
    }
}
