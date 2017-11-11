package daggerok.spring;

import daggerok.user.User;
import daggerok.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IndexPage {

  final UserRepository userRepository;

  @RequestMapping("/")
  public String index(Model model) {
    model.addAttribute("message", "this is a groovy template engine from spting-boot!");
    model.addAttribute("users", userRepository.findAll()
                                              .stream()
                                              .map(User::toString)
                                              .collect(toList()));
    return "index";
  }
}
