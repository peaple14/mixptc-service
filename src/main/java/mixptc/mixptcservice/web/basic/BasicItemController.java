package mixptc.mixptcservice.web.basic;


import lombok.RequiredArgsConstructor;
import mixptc.mixptcservice.domain.item.DeliveryType;
import mixptc.mixptcservice.domain.item.GenreCode;
import mixptc.mixptcservice.domain.item.Item;
import mixptc.mixptcservice.domain.item.ItemRepository;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor //생성자 주입용
public class BasicItemController {
    //RequiredArgsConstructor: final이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다
    //1개만 있을경우 @Autowired로 의존관계주입
    private final ItemRepository itemRepository;

    @ModelAttribute("deliveryTypes")
    public DeliveryType[] deliveryTypes() {return DeliveryType.values();}

    @ModelAttribute("GenreCodes")
    public List<GenreCode> genreCodes(){
        List<GenreCode> genreCodes = new ArrayList<>();
        genreCodes.add(new GenreCode("DICTIONARY", "사전"));
        genreCodes.add(new GenreCode("WORK BOOK", "문제집"));
        genreCodes.add(new GenreCode("NOVEL", "소설"));
        genreCodes.add(new GenreCode("LITERATURE", "문학"));
        genreCodes.add(new GenreCode("HISTORY", "역사"));
        genreCodes.add(new GenreCode("ETC", "기타"));
        return genreCodes;
    }

    //가장처음 들어왔을때 상품목록뜨게하기
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    //목록에서 id를 클릭시
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "basic/addForm";
    }

    @PostMapping("/add")
    public String addItem(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
