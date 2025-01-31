package com.example.contacts;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.contacts.controllers.PersonController;
import com.example.contacts.models.Person;
import com.example.contacts.repository.PersonRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvcのインジェクションを確認

    @SuppressWarnings("removal")
    @MockBean
    private PersonRepository repository;

    @InjectMocks
    private PersonController personController;

    private Person person;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setId(1L);
        person.setName("鈴木");
        person.setAge(23);
        person.setEmail("suzuki@email.com");
    }

    @Test
    public void testIndex() throws Exception {
        when(repository.findAll()).thenReturn(Arrays.asList(person));

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("person/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("people"));
    }

    @Test
    public void testCreatePerson() throws Exception {
        // person オブジェクトの初期化
        Person person = new Person();
        person.setName("鈴木");
        person.setAge(23);
        person.setEmail("suzuki@email.com");

        // saveAndFlushをモックする
        when(repository.saveAndFlush(any(Person.class))).thenReturn(person);

        // /createエンドポイントに対するPOSTリクエストを送信
        mockMvc.perform(MockMvcRequestBuilders.post("/create")
                           .param("name", "鈴木")
                           .param("age", "23")
                           .param("email", "suzuki@email.com"))
                   .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                   .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        // saveAndFlushが1回呼ばれたことを確認
        verify(repository, times(1)).saveAndFlush(any(Person.class));
    }

    @Test
    public void testDeletePerson() throws Exception {
        doNothing().when(repository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/delete/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void testEditPerson() throws Exception {
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        mockMvc.perform(MockMvcRequestBuilders.get("/edit/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("person/edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"));
    }

    @Test
    public void testUpdatePerson() throws Exception {
        when(repository.save(any(Person.class))).thenReturn(person);

        mockMvc.perform(MockMvcRequestBuilders.post("/update/1")
                        .param("name", "鈴木")
                        .param("age", "23")
                        .param("email", "suzuki@email.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        verify(repository, times(1)).save(any(Person.class));
    }
}
