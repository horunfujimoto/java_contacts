package com.example.contacts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.contacts.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // JpaRepositoryを継承するだけでデータベースから値を取得したり保存する機能が提供される
}
