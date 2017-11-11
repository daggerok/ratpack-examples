package daggerok.web;

import daggerok.data.Item;
import daggerok.data.ItemRestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexPage {

    final ItemRestRepository itemRestRepository;

    @GetMapping("/")
    String index(final Model model) {

        model.addAttribute(Item.MODEL_LIST_NAME, itemRestRepository.findAll());
        return "index";
    }
}
