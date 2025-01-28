package com.example.contacts.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Person {
  // 文字列の入力必須のバリデーション
  @NotBlank
  // 文字列の長さが指定の文字数
  @Size(max=120)
  private String name;

  @NotNull
  @Min(0)
  @Max(120)
  private Integer age;

  @NotBlank
  @Email
  @Size(max = 254)
  private String email;
}