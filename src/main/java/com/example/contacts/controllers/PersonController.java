package com.example.contacts.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.contacts.models.Person;
import com.example.contacts.repository.PersonRepository;

@Controller
public class PersonController {

    //Personクラスのフィールドをfinalにする。
    private final PersonRepository repository;
    public PersonController(PersonRepository repository){
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(@ModelAttribute Person person, Model model){
        //一覧用データの用意(データを全件取得する)
        model.addAttribute("people", repository.findAll());
        return "person/index";
    }

    @PostMapping("/create")
    // エラーの内容はBindingResultに入る
    public String create(@Validated @ModelAttribute Person person, BindingResult result, Model model){
        // バリデーションエラーがある場合はindex.htmlを表示
        if(result.hasErrors()){
            model.addAttribute("people", repository.findAll());
            return "person/index";
        }
        //データを直ちに保存する
        repository.saveAndFlush(person);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    // パスに含まれるパラメータ{id}を変数に格納するアノテーション
    public String remove(@PathVariable Long id) {
        // JPA Repositoryによって提供されたidでデータを削除する機能
        repository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        // idでデータを検索する機能
        model.addAttribute("person", repository.findById(id));
        return "person/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Validated  @ModelAttribute Person person, BindingResult result) {
        if(result.hasErrors()){
            return "person/edit";
        }
        repository.save(person);
        return "redirect:/";
    }

//     //初期データの投入
//   @PostConstruct
//   public void dataInit(){
//     Person suzuki = new Person();
//     suzuki.setName("鈴木");
//     suzuki.setAge(23);
//     suzuki.setEmail("suzuki@email.com");
//     repository.saveAndFlush(suzuki);
 
//     Person sato = new Person();
//     sato.setName("佐藤");
//     sato.setAge(20);
//     sato.setEmail("sato@email.com");
//     repository.saveAndFlush(sato);
//    }

}
