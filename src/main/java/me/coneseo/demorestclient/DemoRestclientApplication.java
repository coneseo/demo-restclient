package me.coneseo.demorestclient;

import me.coneseo.demorestclient.dto.BookDTO;
import me.coneseo.demorestclient.dto.GithubCommit;
import me.coneseo.demorestclient.dto.GithubRepository;
import me.coneseo.demorestclient.dto.SearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@SpringBootApplication
public class DemoRestclientApplication {

    @Autowired
    WebClient.Builder webClientBuild;
    //RestTemplateBuilder restTemplateBuilder;
    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    public static void main(String[] args) {
        SpringApplication.run(DemoRestclientApplication.class, args);
    }
    private final String word = "java";
    private final String key = "dc0d381e1832f6eaeeb3659950e4d2ac";
    @Bean
    public ApplicationRunner applicationRunner(){
        return args -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            WebClient webClient = webClientBuild.baseUrl("https://dapi.kakao.com").build();
            //TODO : reactor와 none-blocking 공부
            //Flux는 데이터를 한건씩 가져오는 것
            //Mono는 요청 한번에 데이터를 배열로 가져오는 것
            Mono<SearchData> repos = webClient.get().uri("/v3/search/book?query="+word)
                    .header("Authorization","KakaoAK "+key )
                    .header("Accept", "application/json")
                    .retrieve()
                    .bodyToMono(SearchData.class);

//            Mono<GithubCommit[]> commitMono = webClient.get().uri("/repos/coneseo/twelvebooks/commits")
//                    .retrieve()
//                    .bodyToMono(GithubCommit[].class);


            //Flux라서 하나씩 찍어주는 것
//            repos.doOnNext(r->{
//                    System.out.println("repos : " + r.getUrl());
//            }).subscribe();
            //위에거랑 아래거랑 똑같긴한데, subscribe를 하면서 뿌려주는 메소드가 존재한다.
            //아래거를 쓰는게 더 편할듯
            repos.subscribe(b->{
                System.out.println("repos : " + b.getDocuments());
                System.out.println("repos : " + b.getMeta());
            });
            //Mono라서 배열을 stream으로 돌려서 보여주는 것
//            commitMono.doOnSuccess(ca ->{
//                Arrays.stream(ca).forEach(c->{
//                    System.out.println("commit : "+ c.getSha());
//                });
//            }).subscribe();

            //            RestTemplate resttemplate = restTemplateBuilder.build();
//
//            GithubRepository[] result = resttemplate.getForObject("https://api.github.com/users/coneseo/repos", GithubRepository[].class);
//            Arrays.stream(result).forEach(r ->{
//                System.out.println("repo : " + r.getUrl());
//            });
//
//            GithubCommit[] commits =  resttemplate.getForObject("https://api.github.com/repos/coneseo/twelvebooks/commits", GithubCommit[].class);
//            Arrays.stream(commits).forEach(c ->{
//                System.out.println("url : " + c.getUrl());
//                System.out.println("sha : "+ c.getSha());
//            });


            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        };
    }
}
/*

    오늘 한거 요약 : 리모트에 있는 rest 서비스를 사용하는 방법
    1) resttemplate을 사용하는 방법 : 스프링은 resttemplatebuilder를 자동으로 bean으로 등록해준다.
       그렇기때문에 builder를 받아서 build를 해서 resttemplate을 쓰면된다.
       다 블럭킹 콜이라는 점! 그 다음에 어떤 코드가 오든 뒤에 있는 코드들은 실행이 끝날때까지 기다려야한다.
       이걸 해결하기 위해 스프링4에서 asyncresttemplate이라는 것이 등록이 되엇는데 지금은 deprecate됐다.
       이유는 webClient로 대체가 되었기 때문이다.
    2) webClient를 사용하는 방법 :   resttemplate과 마찬가지로 webClient를 bean으로 등록하지 않는다.
       webClinet를 만들수있는 webClient.Builder를 빈으로 등록해준다.
       WebClient.Builder를 bean으로 받아서 얘를 빌드해서 웹 클라이언트를 만들어서 쓰면된다. 아예 빈으로 등록해서 쓰던가
       전부다 넌 블럭킹하다는 것이 주의할 점이다. 코드가 응답을 다 안받아와도 코드는 쭉 간다.
       받아올때 Mono로 받느냐 Flux로 받느냐가 차이점
       응답 본문 자체가 어떤 특정한 타입의  array인 경우는 flux로 받으면 섞여서 처리가 될 수 있는 것이고,
       mono로 받으면 응답 뭉탱이 뭉탱이 로 처리가 되는 것
 */
