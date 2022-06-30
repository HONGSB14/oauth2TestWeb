package oauth2web.controller;

import oauth2web.dto.MemberDto;
import oauth2web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // SpringBootApplication 에서 설정된 어노테이션으로 해당 어노테이션이 클래스에 붙으면  그 클래스는 컨트롤러임을 나타낸다.
                      // @Controller에는 @Component 가 달려있어 @ComponentScan을 통해 자동스캔되며 클래스경로 스캐닝을 통해 자동감지가 되도록 한다.
public class IndexController {  //인덱스 컨트롤러 클래스
    @Autowired  //Bean을 주입한다.
    private IndexService indexService;  //IndexSerive 에 빈을 주입하여 메모리 할당을 한다.

    @GetMapping("/") //클라이언트가 요청한 URL을 연결하기 위하여 쓴다.
    public String main(){
        return "main";
    }

    @GetMapping("/member/login")
    public String login(){
        return "login";
    }
    @GetMapping("/member/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/member/signupcontroller")                          //HTTP 요청을 POST방식으로  처리하기 위한 매핑 어노테이션이다.
    public String signupcontroller(MemberDto memberDto){    //indexSerivce 로 값을 넘기기위한 메소드 ( MemberDto 파라미터값을 가지고 있다. )
        indexService.signup(memberDto);                                       //indexService에 있는 signup 메소드로  meberDto값을 넘긴다.
        return"redirect:/";                                                                    //"redirect:/" 로 값을 리턴한다.
    }
}
