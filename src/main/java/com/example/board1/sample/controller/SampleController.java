package com.example.board1.sample.controller;

import com.example.board1.sample.dto.SampleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/ex1")
    public void ex1() {
        log.info("SampleController----------ex1----------");
    }

//    Mapping의 주소값을 {"", ""} 이런 식으로 처리하면 두개 이상의 URL 처리 가능
    @GetMapping({"/ex2", "/exLink"})
    public void exModel(Model model) {
        log.info("SampleController----------ex2----------");

        List<SampleDTO> list = IntStream.rangeClosed(1, 20).asLongStream().mapToObj(i -> {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("First_" + i)
                    .last("Last_" + i)
                    .regTime(LocalDateTime.now())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list", list);
    }

    @GetMapping({"/exInline"})
    public String exInline(RedirectAttributes redirectAttributes) {
        log.info("SampleController----------exInline----------");

        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("First_100")
                .last("Last_100")
                .regTime(LocalDateTime.now())
                .build();

        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);

        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3() {
        log.info("SampleController----------ex3----------");
    }

}
