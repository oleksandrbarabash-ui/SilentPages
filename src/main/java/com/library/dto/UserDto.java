package com.library.dto;

/**
 * DTO для безпечного повернення даних користувача клієнту.
 * Не містить поля пароля, що виключає можливість випадкового витоку хешу у відповіді API.
 */
public class UserDto {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String roleName;

    public UserDto(int id, String firstname, String lastname, String email, String phone, String roleName) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.roleName = roleName;
    }

    public int getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRoleName() { return roleName; }
}